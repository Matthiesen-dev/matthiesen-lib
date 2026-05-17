package dev.matthiesen.neoforge.matthiesen_lib;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLibClient;
import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibConstants;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;

/**
 * MatthiesenLibNeoForgeClientBusEvents is a client-side event subscriber class for the NeoForge mod loader.
 */
@EventBusSubscriber(modid = MatthiesenLibConstants.MOD_ID, value = Dist.CLIENT)
public class MatthiesenLibNeoForgeClientBusEvents {
    /**
     * Default constructor for MatthiesenLibNeoForgeClientBusEvents.
     */
    public MatthiesenLibNeoForgeClientBusEvents() {}

    /**
     * Event handler for registering custom menu screens. This method listens for the RegisterMenuScreensEvent and applies any screen registrations
     * defined in MatthiesenLibClient.
     * @param event The event object containing the context for menu screen registration. This method is called with the lowest priority to ensure that
     *              it runs after all others screen registrations have been processed, allowing MatthiesenLibClient to add its screens without
     *              interfering with other mods' registrations.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerScreens(RegisterMenuScreensEvent event) {
        MatthiesenLibClient.applyScreenRegistrations(event::register);
    }

    /**
     * Event handler for registering custom entity renderers. This method listens for the EntityRenderersEvent.RegisterRenderers event and allows for the
     * registration of custom entity renderers. Currently, this method is empty, but it can be filled in with calls to register custom renderers as needed.
     * @param event The event object containing the context for entity renderer registration. This method is called with the lowest priority to ensure that
     *              it runs after all other renderer registrations, allowing MatthiesenLib to add its renderers without interfering with other mods' registrations.
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        MatthiesenLibClient.applyEntityRendererRegistrations(event::registerEntityRenderer, event::registerBlockEntityRenderer);
    }

    /**
     * Event handler for customizing block outline rendering. This method listens for the RenderHighlightEvent.Block event and allows registered block outline listeners
     * to modify or cancel the rendering of block outlines. If any listener returns false, the vanilla block outline rendering will be canceled for that event.
     * @param event The event object containing the context for block outline rendering, including the target block, pose stack, camera, and buffer source.
     *              This method is called with the lowest priority to ensure that it runs after all other block outline listeners have been processed, allowing
     *              MatthiesenLib's listeners to override vanilla behavior without interfering with other mods' listeners.
     */
    @SubscribeEvent
    public static void onHighlightBlock(RenderHighlightEvent.Block event) {
        if (Minecraft.getInstance().level == null) {
            return;
        }

        boolean shouldRenderVanillaOutline = MatthiesenLibClient.applyBlockOutlineListeners(
                Minecraft.getInstance().level,
                event.getTarget(),
                event.getPoseStack(),
                event.getCamera(),
                event.getMultiBufferSource()
        );

        if (!shouldRenderVanillaOutline) {
            event.setCanceled(true);
        }
    }
}
