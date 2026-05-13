package dev.matthiesen.neoforge.matthiesen_lib.permission;

import dev.matthiesen.common.matthiesen_lib.Constants;
import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import dev.matthiesen.common.matthiesen_lib.interfaces.Permission;
import dev.matthiesen.common.matthiesen_lib.interfaces.PermissionValidator;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.server.permission.PermissionAPI;
import net.neoforged.neoforge.server.permission.events.PermissionGatherEvent;
import net.neoforged.neoforge.server.permission.nodes.PermissionNode;
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NeoForgePermissionValidator implements PermissionValidator {
    private final Map<ResourceLocation, PermissionNode<Boolean>> nodes = new HashMap<>();

    public NeoForgePermissionValidator() {
        NeoForge.EVENT_BUS.addListener(this::onPermissionGatherNodes);
    }

    @SubscribeEvent
    public void onPermissionGatherNodes(PermissionGatherEvent.Nodes event) {
        Constants.LOGGER.info("Starting Forge permission node registry");
        event.addNodes(this.createNodes());
        Constants.LOGGER.debug("Finished Forge permission node registry");
    }

    @Override
    public void initialize() {
        Constants.LOGGER.info("Booting ForgePermissionApiPermissionValidator, player permissions will be checked using MinecraftForge' PermissionAPI, non player command sources will use Minecraft' permission level system, see https://docs.minecraftforge.net/en/latest/ and https://minecraft.fandom.com/wiki/Permission_level");
    }

    @Override
    public boolean hasPermission(ServerPlayer player, Permission permission) {
        PermissionNode<Boolean> node = this.findNode(permission);
        if (node == null) {
            return player.hasPermissions(permission.getLevel().getNumericalValue());
        }
        return PermissionAPI.getPermission(player, node);
    }

    @Override
    public boolean hasPermission(ServerPlayer player, String permission, int level) {
        String namespace = permission.split("\\.")[0];
        String path = permission.substring(permission.indexOf(".") + 1);
        PermissionNode<Boolean> node = new PermissionNode<>(
                namespace,
                path,
                PermissionTypes.BOOLEAN,
                (p, uuid, context) -> p != null && p.hasPermissions(level)
        );
        return PermissionAPI.getPermission(player, node);
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, Permission permission) {
        ServerPlayer player = this.extractPlayerFromSource(source);
        if (player == null) {
            return source.hasPermission(permission.getLevel().getNumericalValue());
        }
        PermissionNode<Boolean> node = this.findNode(permission);
        if (node == null) {
            return source.hasPermission(permission.getLevel().getNumericalValue());
        }
        return PermissionAPI.getPermission(player, node);
    }

    @Override
    public boolean hasPermission(CommandSourceStack source, String permission, int level) {
        ServerPlayer player = this.extractPlayerFromSource(source);
        if (player == null) {
            return source.hasPermission(4);
        }
        String namespace = permission.split("\\.")[0];
        String path = permission.substring(permission.indexOf(".") + 1);
        PermissionNode<Boolean> node = new PermissionNode<>(
                namespace,
                path,
                PermissionTypes.BOOLEAN,
                (p, uuid, context) -> p != null && p.hasPermissions(level)
        );
        return PermissionAPI.getPermission(player, node);
    }

    private List<PermissionNode<?>> createNodes() {
        return MatthiesenLib.getPermissionsManager().all().stream().map(permission -> {
            PermissionNode<Boolean> node = new PermissionNode<>(
                    permission.getIdentifier(),
                    PermissionTypes.BOOLEAN,
                    (player, uuid, context) -> player != null && player.hasPermissions(permission.getLevel().getNumericalValue())
            );
            this.nodes.put(permission.getIdentifier(), node);
            Constants.LOGGER.debug("Registered Forge permission node {}", node.getNodeName());
            return node;
        }).collect(Collectors.toList());
    }

    private PermissionNode<Boolean> findNode(Permission permission) {
        return this.nodes.get(permission.getIdentifier());
    }

    private ServerPlayer extractPlayerFromSource(CommandSourceStack source) {
        return source.getPlayer();
    }
}
