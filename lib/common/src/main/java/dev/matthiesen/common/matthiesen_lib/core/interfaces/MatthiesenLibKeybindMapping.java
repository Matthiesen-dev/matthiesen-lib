package dev.matthiesen.common.matthiesen_lib.core.interfaces;

import net.minecraft.client.KeyMapping;

public interface MatthiesenLibKeybindMapping {
    KeyMapping getKeybind();
    void onClientTick();
}
