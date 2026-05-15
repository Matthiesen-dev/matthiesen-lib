package dev.matthiesen.common.matthiesen_lib.registry;

import dev.matthiesen.common.matthiesen_lib.MatthiesenLib;
import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibConfigurableRegistry;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Abstract base class for registries that can be configured with extra data.
 * The supported registry category is provided up front so subclasses are limited to the registry types currently exposed by
 * {@link MatthiesenLib.RegistryBuilder}.
 *
 * @param <T> The type of objects being registered.
 */
@SuppressWarnings("unused")
public abstract class AbstractRegistry<T> implements MatthiesenLibConfigurableRegistry<T> {
    private final MatthiesenLib.RegistryBuilder registryBuilder;
    private final SupportedRegistries<T> supportedRegistry;

    /**
     * Creates a registry wrapper backed by a {@link MatthiesenLib.RegistryBuilder} created from the supplied mod ID.
     *
     * @param modId             the mod ID used for registration namespacing
     * @param supportedRegistry the supported registry category this registry can target
     */
    protected AbstractRegistry(String modId, SupportedRegistries<T> supportedRegistry) {
        this(new MatthiesenLib.RegistryBuilder(modId), supportedRegistry);
    }

    /**
     * Creates a registry wrapper backed by an existing {@link MatthiesenLib.RegistryBuilder}.
     *
     * @param registryBuilder   the registry builder used to perform registrations
     * @param supportedRegistry the supported registry category this registry can target
     */
    protected AbstractRegistry(MatthiesenLib.RegistryBuilder registryBuilder, SupportedRegistries<T> supportedRegistry) {
        this.registryBuilder = Objects.requireNonNull(registryBuilder, "registryBuilder");
        this.supportedRegistry = Objects.requireNonNull(supportedRegistry, "supportedRegistry");
    }

    @Override
    public final <T1 extends T> Supplier<T1> register(String name, Supplier<T1> entry) {
        return supportedRegistry.register(registryBuilder, name, entry);
    }

    /**
     * @return the registry builder backing this registry
     */
    protected final MatthiesenLib.RegistryBuilder getRegistryBuilder() {
        return registryBuilder;
    }

    /**
     * @return the supported registry category used by this registry
     */
    protected final SupportedRegistries<T> getSupportedRegistry() {
        return supportedRegistry;
    }
}
