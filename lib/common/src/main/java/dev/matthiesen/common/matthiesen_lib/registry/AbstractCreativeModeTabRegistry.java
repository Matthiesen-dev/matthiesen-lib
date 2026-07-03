package dev.matthiesen.common.matthiesen_lib.registry;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibCreativeModeTabSectionsManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Convenience base class for registries that register {@link CreativeModeTab} instances.
 *
 * <p>This type locks registration to the creative mode tab registry category by wiring
 * {@link SupportedRegistries#CREATIVE_MODE_TAB} into {@link AbstractRegistry}.</p>
 */
@SuppressWarnings("unused")
public abstract class AbstractCreativeModeTabRegistry extends AbstractRegistry<CreativeModeTab> {
    private String modId;
    /**
     * Creates a creative mode tab registry using the given mod ID.
     *
     * @param modId the mod ID used to namespace all registrations
     */
    protected AbstractCreativeModeTabRegistry(String modId) {
        super(modId, SupportedRegistries.CREATIVE_MODE_TAB);
        this.modId = modId;
    }

    /**
     * Creates a creative mode tab registry using an existing {@link MatthiesenLib.RegistryBuilder}.
     *
     * @param registryBuilder the builder used to perform creative mode tab registrations
     */
    protected AbstractCreativeModeTabRegistry(MatthiesenLib.RegistryBuilder registryBuilder) {
        super(registryBuilder, SupportedRegistries.CREATIVE_MODE_TAB);
    }

    /**
     * Creates a new {@link CreativeModeTab.Builder} instance for use in registrations. This is a convenience method that simply delegates to the underlying registry builder's {@code newCreativeTabBuilder()} method, but it can be overridden by subclasses if they need to customize the builder creation process.
     * @return A new instance of {@link CreativeModeTab.Builder} for use in creative mode tab registrations.
     */
    protected final CreativeModeTab.Builder newCreativeModeTabBuilder() {
        return this.getRegistryBuilder().newCreativeTabBuilder();
    }

    /**
     * Registers creative mode tab sections using the provided {@link Consumer} to configure the section builder. This
     * method delegates to {@link MatthiesenLibCreativeModeTabSectionsManager#registerCreativeModeTabSections(String, String, Consumer)} with the mod ID and the provided consumer.
     * @param builderConsumer A {@link Consumer} that configures the section builder for creative mode tab sections.
     */
    protected final void registerCreativeModeTabSections(String creativeModeTabId, Consumer<MatthiesenLibCreativeModeTabSectionsManager.SectionBuilder> builderConsumer) {
        MatthiesenLibCreativeModeTabSectionsManager.registerCreativeModeTabSections(modId, creativeModeTabId, builderConsumer);
    }
}

