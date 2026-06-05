package dev.matthiesen.neoforge.matthiesen_lib_api.helper;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.function.Consumer;

/**
 * Helper class for registering client-side events and listeners in a NeoForge mod. This class provides methods to register startup events and other event listeners on the mod event bus. It allows for deferred execution of client-specific code during the client's initialization phase by registering a listener for the FMLClientSetupEvent, which is triggered when the client is being set up. The registerBusListener method can be used to register any event listener on the mod event bus, enabling the mod to respond to various events during its lifecycle. This design helps to keep client-specific code organized and ensures that it is executed at the appropriate time during the client's lifecycle.
 */
public final class MatthiesenLibClientNeoForgeHelper {
    private static volatile IEventBus modBus;

    private MatthiesenLibClientNeoForgeHelper() {}

    /**
     * Initializes the MatthiesenLibClientNeoForgeHelper by setting the mod event bus. This method should be called during the mod's initialization phase to ensure that the helper has access to the event bus for registering listeners. The event bus is used to register client-side events and listeners, allowing the mod to respond to various events during its lifecycle. By initializing the helper with the event bus, it can properly register listeners for client setup and other events as needed.
     * @param eventBus The mod event bus to be used for registering client-side events and listeners. This should be the event bus provided by the NeoForge mod loader during the mod's initialization phase.
     */
    public static void init(IEventBus eventBus) {
        modBus = eventBus;
    }

    /**
     * Registers a listener for the FMLClientSetupEvent, which is triggered during the client's initialization phase. This method allows for deferred execution of client-specific code by accepting a Consumer that will be called when the FMLClientSetupEvent is fired. The eventConsumer parameter should contain any client-specific initialization code that needs to be executed when the client is being set up. By registering this listener, the mod can ensure that client-specific code is executed at the appropriate time during the client's lifecycle, allowing for proper setup of client-side features and functionality.
     * @param eventConsumer A Consumer that accepts an FMLClientSetupEvent. This consumer will be called when the FMLClientSetupEvent is fired, allowing for execution of client-specific initialization code during the client's setup phase. The eventConsumer should contain any code that needs to run when the client is being initialized, such as registering client-side event handlers, setting up client-specific configurations, or performing any other necessary client-side setup tasks.
     */
    public static void registerStartupEvent(Consumer<FMLClientSetupEvent> eventConsumer) {
        registerBusListener(eventConsumer);
    }

    /**
     * Registers a listener for any event on the mod event bus. This method allows for registering listeners for various events that may occur during the mod's lifecycle. The listener parameter should be a Consumer that accepts an event of type T, where T extends Event. By registering this listener, the mod can respond to specific events by executing the provided consumer when those events are fired. This method provides flexibility in handling different types of events and allows for organized management of event listeners within the mod.
     * @param listener A Consumer that accepts an event of type T, where T extends Event. This consumer will be called when an event of the specified type is fired on the mod event bus, allowing for execution of custom logic in response to that event. The listener should contain any code that needs to run when the specified event occurs, such as updating game state, responding to player actions, or performing any other necessary tasks in response to the event.
     * @param <T> The type of event that the listener will respond to. This should be a class that extends Event, representing the specific event that the listener is interested in. By specifying the type parameter T, the method can ensure that the listener is registered for the correct type of event and can provide type safety when handling events on the mod event bus.
     */
    public static <T extends Event> void registerBusListener(Consumer<T> listener) {
        modBus.addListener(listener);
    }
}
