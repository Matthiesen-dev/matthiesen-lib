package dev.matthiesen.common.matthiesen_lib.core.interfaces;

/** @deprecated Use {@link dev.matthiesen.api.matthiesen_lib.core.interfaces.MatthiesenLibBuiltInTextParsers} instead. */
@Deprecated
@SuppressWarnings("unused")
public enum MatthiesenLibBuiltInTextParsers {
    VANILLA(dev.matthiesen.api.matthiesen_lib.core.interfaces.MatthiesenLibBuiltInTextParsers.VANILLA),
    EMBER(dev.matthiesen.api.matthiesen_lib.core.interfaces.MatthiesenLibBuiltInTextParsers.EMBER);

    private final dev.matthiesen.api.matthiesen_lib.core.interfaces.MatthiesenLibBuiltInTextParsers apiType;

    MatthiesenLibBuiltInTextParsers(dev.matthiesen.api.matthiesen_lib.core.interfaces.MatthiesenLibBuiltInTextParsers apiType) {
        this.apiType = apiType;
    }

    public String getName() {
        return apiType.getName();
    }

    public dev.matthiesen.api.matthiesen_lib.core.interfaces.MatthiesenLibBuiltInTextParsers toApi() {
        return apiType;
    }

    public static MatthiesenLibBuiltInTextParsers fromApi(dev.matthiesen.api.matthiesen_lib.core.interfaces.MatthiesenLibBuiltInTextParsers type) {
        for (MatthiesenLibBuiltInTextParsers value : values()) {
            if (value.apiType == type) {
                return value;
            }
        }
        throw new IllegalArgumentException("No mapping for api parser type: " + type);
    }
}
