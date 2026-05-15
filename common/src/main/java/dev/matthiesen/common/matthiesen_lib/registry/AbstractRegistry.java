package dev.matthiesen.common.matthiesen_lib.registry;

import dev.matthiesen.common.matthiesen_lib.core.interfaces.MatthiesenLibConfigurableRegistry;

import java.util.function.Supplier;

/**
 * Abstract base class for registries that can be configured with extra data. Provides a default implementation of the init method and requires subclasses to implement the register method.
 * @param <T> The type of objects being registered.
 */
@SuppressWarnings("unused")
public abstract class AbstractRegistry<T> implements MatthiesenLibConfigurableRegistry<T> {
    /**
     * Default constructor for the AbstractRegistry class. No initialization is required as setup is handled in the init method.
     */
    public AbstractRegistry() {}

    @Override
    public abstract <T1 extends T> Supplier<T1> register(String name, Supplier<T1> entry);
}
