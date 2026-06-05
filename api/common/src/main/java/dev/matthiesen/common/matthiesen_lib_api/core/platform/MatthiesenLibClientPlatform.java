package dev.matthiesen.common.matthiesen_lib_api.core.platform;

import java.util.List;

/**
 * Interface for client-side initialization across mod loaders.
 */
public interface MatthiesenLibClientPlatform {
    /**
     * Registers a list of Runnables to be executed during client initialization.
     * @param runnables The list of Runnables to execute on client load. Implementations should ensure these are run at the appropriate time during the client lifecycle.
     */
    void onClientLoad(List<Runnable> runnables);
}
