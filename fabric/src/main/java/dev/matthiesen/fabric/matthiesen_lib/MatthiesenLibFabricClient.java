package dev.matthiesen.fabric.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLibClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

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
        MatthiesenLibClient.applyScreenRegistrations(MenuScreens::register);
        MatthiesenLibClient.applyEntityRendererRegistrations(EntityRendererRegistry::register, BlockEntityRenderers::register);
    }
}
