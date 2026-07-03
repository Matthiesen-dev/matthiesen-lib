package dev.matthiesen.common.matthiesen_lib.registry;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibCreativeModeTabSectionsManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
     * Registers a simple creative mode tab with the specified name, title, display icon, and display items. This method creates a new
     * {@link CreativeModeTab} instance using the provided parameters and registers it with the registry.
     * @param name The name of the creative mode tab to register. This will be used to construct the resource location for the tab.
     * @param title The title of the creative mode tab, represented as a {@link Component}.
     * @param displayIcon A {@link Supplier} that provides the display icon for the creative mode tab as an {@link ItemStack}.
     * @param displayItems A list of {@link ItemStack} instances that will be displayed in the creative mode tab.
     * @return A {@link Supplier} that provides the registered {@link CreativeModeTab} instance.
     */
    protected final Supplier<CreativeModeTab> registerSimpleCreativeTab(String name, Component title, Supplier<ItemStack> displayIcon, Supplier<List<ItemStack>> displayItems) {
        return register(name, () -> newCreativeModeTabBuilder()
                .title(title)
                .icon(displayIcon)
                .displayItems((parameters, output) -> displayItems.get().forEach(output::accept))
                .build()
        );
    }

    /**
     * Registers a sectioned creative mode tab with the specified name, title, display icon, and a builder consumer for configuring the sections.
     * This method registers the creative mode tab and delegates the section registration to {@link MatthiesenLibCreativeModeTabSectionsManager}.
     * @param name The name of the creative mode tab to register. This will be used to construct the resource location for the tab.
     * @param title The title of the creative mode tab, represented as a {@link Component}.
     * @param displayIcon A {@link Supplier} that provides the display icon for the creative mode tab as an {@link ItemStack}.
     * @param builderConsumer A {@link Consumer} that configures the section builder for creative mode tab sections.
     * @return A {@link Supplier} that provides the registered {@link CreativeModeTab} instance.
     */
    protected final Supplier<CreativeModeTab> registerSectionedCreativeTab(String name, Component title, Supplier<ItemStack> displayIcon, Consumer<MatthiesenLibCreativeModeTabSectionsManager.SectionBuilder> builderConsumer) {
        MatthiesenLibCreativeModeTabSectionsManager.addAutoRegistration(modId, () -> registerCreativeModeTabSections(name, builderConsumer));
        return register(name, () -> newCreativeModeTabBuilder()
                .title(title)
                .icon(displayIcon)
                .displayItems((parameters, output) -> getCreativeModeTabSectionItems(name).forEach(output::accept))
                .build()
        );
    }

    /**
     * Registers creative mode tab sections using the provided {@link Consumer} to configure the section builder. This
     * method delegates to {@link MatthiesenLibCreativeModeTabSectionsManager#registerCreativeModeTabSections(ResourceLocation, Consumer)} with the mod ID and the provided consumer.
     * @param builderConsumer A {@link Consumer} that configures the section builder for creative mode tab sections.
     */
    protected final void registerCreativeModeTabSections(String creativeModeTabId, Consumer<MatthiesenLibCreativeModeTabSectionsManager.SectionBuilder> builderConsumer) {
        MatthiesenLibCreativeModeTabSectionsManager.registerCreativeModeTabSections(ResourceLocation.fromNamespaceAndPath(modId, creativeModeTabId), builderConsumer);
    }

    /**
     * Retrieves the list of {@link ItemStack} instances associated with the specified creative mode tab ID. This method delegates to {@link MatthiesenLibCreativeModeTabSectionsManager#getTabSections(ResourceLocation)} to obtain the sections and then flattens the resulting lists of items into a single list.
     * @param creativeModeTabId The ID of the creative mode tab for which to retrieve the associated items.
     * @return A list of {@link ItemStack} instances associated with the specified creative mode tab ID.
     */
    protected final List<ItemStack> getCreativeModeTabSectionItems(String creativeModeTabId) {
        return MatthiesenLibCreativeModeTabSectionsManager.getTabSections(ResourceLocation.fromNamespaceAndPath(modId, creativeModeTabId))
                .sections()
                .values().
                stream()
                .flatMap(List::stream)
                .toList();
    }
}

