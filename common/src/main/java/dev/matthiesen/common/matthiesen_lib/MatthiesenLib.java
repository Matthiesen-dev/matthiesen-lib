package dev.matthiesen.common.matthiesen_lib;

/**
 * Main class for the MatthiesenLib mod. This class is responsible for initializing the mod and setting up any necessary
 * configurations or resources. It serves as the entry point for the mod's functionality and can be used to register common
 * features that are shared across different platforms (e.g., Fabric, Forge). The initialize method is called during the
 * mod's initialization phase to perform any necessary setup tasks.
 */
public class MatthiesenLib {

    /**
     * Initializes the MatthiesenLib mod.
     */
    public static void initialize() {
        Constants.createInfoLog("Initialized common");
    }
}
