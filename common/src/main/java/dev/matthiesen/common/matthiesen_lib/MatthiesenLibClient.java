package dev.matthiesen.common.matthiesen_lib;

/**
 * Client-side initialization class for MatthiesenLib. This class is responsible for setting up any client-specific features
 * or configurations required by the library. It is called during the client initialization phase of the mod loading process,
 * allowing for the registration of client-only components such as menu screens, renderers, and other visual elements.
 */
public class MatthiesenLibClient {

    /**
     * Initializes the client-side components of MatthiesenLib.
     */
    public static void initialize() {
        Constants.createInfoLog("Initialized client");
    }
}
