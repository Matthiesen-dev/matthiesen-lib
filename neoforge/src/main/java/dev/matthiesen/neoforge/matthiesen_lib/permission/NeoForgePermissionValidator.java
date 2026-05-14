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

/**
 * Implementation of the PermissionValidator interface that checks permissions using NeoForge's PermissionAPI.
 * This validator will check if a player has the required permission level to execute a command or perform an action using NeoForge's PermissionAPI,
 * which allows for integration with various permissions mods that support the API. For more information on NeoForge's PermissionAPI,
 * see <a href="https://docs.minecraftforge.net/en/latest/">...</a> and <a href="https://minecraft.fandom.com/wiki/Permission_level">...</a>
 */
public class NeoForgePermissionValidator implements PermissionValidator {
    private final Map<ResourceLocation, PermissionNode<Boolean>> nodes = new HashMap<>();

    /**
     * Creates a new instance of the NeoForgePermissionValidator. This constructor registers an event listener for the PermissionGatherEvent.Nodes event,
     * which is used to gather permission nodes from the mod and register them with NeoForge's PermissionAPI. When the PermissionGatherEvent.Nodes event is
     * fired, the onPermissionGatherNodes method will be called, which will create permission nodes for all permissions registered in the MatthiesenLib
     * PermissionsManager and add them to the event. This allows the permissions to be registered with NeoForge's PermissionAPI and used for permission checks
     * when validating permissions for players and command sources.
     */
    public NeoForgePermissionValidator() {
        NeoForge.EVENT_BUS.addListener(this::onPermissionGatherNodes);
    }

    /**
     * Event handler for the PermissionGatherEvent.Nodes event. This method is called when NeoForge is gathering permission nodes from mods to register with the PermissionAPI.
     * @param event The PermissionGatherEvent.Nodes event, which contains a method for adding permission nodes to be registered with the PermissionAPI.
     */
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

    /**
     * Creates permission nodes for all permissions registered in the MatthiesenLib PermissionsManager and adds them to the internal nodes map for lookup
     * when validating permissions.
     * @return A list of PermissionNode objects representing the permissions registered in the MatthiesenLib PermissionsManager, which will be added to the
     * PermissionGatherEvent.Nodes event for registration with NeoForge's PermissionAPI.
     */
    private List<PermissionNode<?>> createNodes() {
        var permManager = MatthiesenLib.getPermissionsManager();
        Constants.createInfoLog("Trying to Register " + permManager.getPendingPermissionCount() + " NeoForge permission nodes");
        return permManager.all().stream().map(permission -> {
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

    /**
     * Finds the PermissionNode associated with the given Permission. This method looks up the permission node in the internal nodes map using the permission's
     * ResourceLocation identifier.
     * @param permission The Permission for which to find the associated PermissionNode.
     * @return The PermissionNode associated with the given Permission, or null if no node is found for the permission's identifier.
     */
    private PermissionNode<Boolean> findNode(Permission permission) {
        return this.nodes.get(permission.getIdentifier());
    }

    /**
     * Extracts the ServerPlayer from a CommandSourceStack. This method checks if the command source is a player and returns the associated ServerPlayer
     * object, or null if the source is not a player.
     * @param source The CommandSourceStack from which to extract the ServerPlayer.
     * @return The ServerPlayer associated with the command source, or null if the source is not a player.
     */
    private ServerPlayer extractPlayerFromSource(CommandSourceStack source) {
        return source.getPlayer();
    }
}
