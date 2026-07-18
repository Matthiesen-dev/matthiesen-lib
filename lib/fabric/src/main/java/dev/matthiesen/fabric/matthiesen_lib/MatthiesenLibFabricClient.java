package dev.matthiesen.fabric.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLibClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Client-side initialization class for MatthiesenLib on the Fabric platform.
 */
public class MatthiesenLibFabricClient implements ClientModInitializer {
    /**
     * Default constructor for the MatthiesenLibFabricClient class. No initialization is required as setup is handled in the onInitializeClient method.
     */
    public MatthiesenLibFabricClient() {}

    /**
     * Initializes the client-side components of MatthiesenLib for the Fabric platform.
     */
    @Override
    public void onInitializeClient() {
        MatthiesenLibClient.modInitializer();

        // Apply Registrations
        MatthiesenLibClient.applyScreenRegistrations(MenuScreens::register);
        MatthiesenLibClient.applyEntityRendererRegistrations(EntityRendererRegistry::register, BlockEntityRenderers::register);
        MatthiesenLibClient.applyKeybindRegistrations(KeyBindingHelper::registerKeyBinding);

        // Setup Listeners
        HudRenderCallback.EVENT.register(MatthiesenLibClient::applyFabricHudRendering);
        WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register((context, hitResult) -> {
            if (!(hitResult instanceof BlockHitResult blockHitResult)) {
                return true;
            }

            return MatthiesenLibClient.applyBlockOutlineListeners(
                    context.world(),
                    blockHitResult,
                    context.matrixStack(),
                    context.camera(),
                    context.consumers()
            );
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                MatthiesenLibClient.applyKeybindTicks();
            }
        });
    }
}
