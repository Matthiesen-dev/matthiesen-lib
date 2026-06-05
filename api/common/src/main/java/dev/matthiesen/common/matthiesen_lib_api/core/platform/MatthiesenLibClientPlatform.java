package dev.matthiesen.common.matthiesen_lib_api.core.platform;

import java.util.List;

public interface MatthiesenLibClientPlatform {
    void onClientLoad(List<Runnable> runnable);
}
