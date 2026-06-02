package dev.matthiesen.common.matthiesen_lib_api.core.interfaces;

public interface MatthiesenLibModContainer {
    String getModName();
    String getModVersion();
    String getPlatform();

    default String getModMetricId() {
        return getPlatform() + ":" + getModName().toLowerCase().replaceAll("\\s+", "_");
    }

    enum Platform {
        FABRIC("fabric"),
        NEOFORGE("neoforge");

        private final String label;

        Platform(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
}
