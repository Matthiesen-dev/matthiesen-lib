package dev.matthiesen.common.matthiesen_lib.core;

import dev.matthiesen.common.matthiesen_lib.core.item.InternalRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class MatthiesenLibCreativeModeTabSectionsManager {
    private static final Map<ResourceLocation, CreativeModeTabSectionRegistration> MOD_TAB_SECTIONS = new HashMap<>();

    public static void init() {
        InternalRegistry.init();
    }

    public static void registerCreativeModeTabSections(String ModId, String creativeModeTabID, Consumer<SectionBuilder> builderConsumer) {
        SectionBuilder builder = new SectionBuilder();
        builderConsumer.accept(builder);
        ResourceLocation creativeTabResource = ResourceLocation.fromNamespaceAndPath(ModId, creativeModeTabID);
        MOD_TAB_SECTIONS.put(creativeTabResource, new CreativeModeTabSectionRegistration(builder.getSections(), builder.getMetadata()));
    }

    public static CreativeModeTabSectionRegistration getTabSections(ResourceLocation creativeModeTabID) {
        return MOD_TAB_SECTIONS.get(creativeModeTabID);
    }

    public static boolean hasTabSections(ResourceLocation creativeModeTabId) {
        return MOD_TAB_SECTIONS.containsKey(creativeModeTabId);
    }

    public static SectionData getTabMetaData(ResourceLocation creativeModeTabId, ResourceLocation sectionId) {
        CreativeModeTabSectionRegistration registration = MOD_TAB_SECTIONS.get(creativeModeTabId);
        if (registration != null) {
            return registration.metadata().get(sectionId);
        }
        return null;
    }

    public record CreativeModeTabSectionRegistration(Map<ResourceLocation, List<ItemStack>> sections, Map<ResourceLocation, SectionData> metadata) {}

    public record SectionData(Component title, int priority, SectionDataMeta meta) {}

    public static class SectionBuilder {
        private final Map<ResourceLocation, List<ItemStack>> sections = new HashMap<>();
        private final Map<ResourceLocation, SectionData> metadata = new HashMap<>();

        public SectionBuilder() {}

        public void registerSection(ResourceLocation id, Component title, int priority) {
            metadata.put(id, new SectionData(title, priority, SectionDataMeta.defaults()));
        }

        public void registerSection(ResourceLocation id, Component title, int priority, Consumer<SectionDataMeta> metaConsumer) {
            SectionDataMeta meta = SectionDataMeta.defaults();
            metaConsumer.accept(meta);
            metadata.put(id, new SectionData(title, priority, meta));
        }

        public void addItemToSection(ResourceLocation sectionId, ItemStack item) {
            sections.computeIfAbsent(sectionId, k -> new java.util.ArrayList<>()).add(item);
        }

        public Map<ResourceLocation, List<ItemStack>> getSections() {
            return sections;
        }

        public Map<ResourceLocation, SectionData> getMetadata() {
            return metadata;
        }
    }

    public static class SectionDataMeta {
        private int sectionTitleColor;
        private int sectionTitleAccentColor;
        private int sectionBackgroundColor;

        public SectionDataMeta(int sectionTitleColor, int sectionTitleAccentColor, int sectionBackgroundColor) {
            this.sectionTitleColor = sectionTitleColor;
            this.sectionTitleAccentColor = sectionTitleAccentColor;
            this.sectionBackgroundColor = sectionBackgroundColor;
        }

        public SectionDataMeta setSectionTitleColor(int value) {
            this.sectionTitleColor = value;
            return this;
        }

        public SectionDataMeta setSectionTitleAccentColor(int value) {
            this.sectionTitleAccentColor = value;
            return this;
        }

        public SectionDataMeta setSectionBackgroundColor(int value) {
            this.sectionBackgroundColor = value;
            return this;
        }

        public int getSectionTitleColor() {
            return sectionTitleColor;
        }

        public int getSectionTitleAccentColor() {
            return sectionTitleAccentColor;
        }

        public int getSectionBackgroundColor() {
            return sectionBackgroundColor;
        }

        public static SectionDataMeta defaults() {
            return new SectionDataMeta(0xFFFFFF, 0xFF5050B8, 0xCC1A1A1A);
        }
    }

}
