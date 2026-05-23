package dev.matthiesen.common.matthiesen_lib.utility;

import net.minecraft.sounds.SoundEvent;

/** @deprecated Use {@link dev.matthiesen.common.matthiesen_lib_api.utility.SoundsPlayer} instead. */
@Deprecated(forRemoval = true)
@SuppressWarnings("unused")
public class SoundsPlayer extends dev.matthiesen.common.matthiesen_lib_api.utility.SoundsPlayer {
    public SoundsPlayer(SoundEvent sound) {
        super(sound);
    }
}
