package dev.matthiesen.neoforge.matthiesen_lib.helper;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * NeoForgeRegistryHelper is a utility class that provides static methods for registering various types of game content
 * (such as blocks, items, sounds, etc.) using the NeoForge mod loader's DeferredRegister system. It maintains a cache of DeferredRegister
 * instances to ensure that each registry is only created once per namespace, and it requires initialization with an IEventBus to function
 * properly. This class serves as a centralized helper for mod developers to easily register their content without having to manage multiple
 * DeferredRegister instances themselves.
 */
public final class NeoForgeRegistryHelper {
    private static final Map<String, DeferredRegister<?>> DEFERRED_REGISTERS = new ConcurrentHashMap<>();
    private static volatile IEventBus modBus;

    /**
     * Private constructor to prevent instantiation of this utility class. All methods are static and should be accessed directly through the class name.
     */
    private NeoForgeRegistryHelper() {
    }

    /**
     * Initializes the NeoForgeRegistryHelper with the given IEventBus. This method must be called before any registration methods are used, as
     * it sets up the event bus that will be used for all DeferredRegister instances created by this helper.
     * @param eventBus The IEventBus instance to use for registering DeferredRegister instances. This is typically the mod event bus provided during mod initialization.
     */
    public static void init(IEventBus eventBus) {
        modBus = eventBus;
    }

    /**
     * Registers a BlockEntityType with the given ResourceLocation ID and supplier. This method uses the DeferredRegister system to ensure that the registration occurs at
     * the correct time during mod loading. The returned Supplier can be used to retrieve the registered BlockEntityType after it has been registered. The registration is
     * cached based on the registry key and namespace to ensure that multiple registrations for the same registry and namespace reuse the same DeferredRegister instance.
     * @param id The ResourceLocation ID to register the BlockEntityType under. This should be unique within the mod's namespace to avoid conflicts with other mods.
     * @param blockEntityType A Supplier that provides the BlockEntityType instance to be registered. This supplier will be called during the registration process
     *                        to create the actual BlockEntityType object.
     * @return A Supplier that can be used to retrieve the registered BlockEntityType after registration. This allows for lazy retrieval of the registered object,
     * which is useful in cases where the registration may not have occurred yet at the time of calling this method.
     * @param <T> The type of BlockEntity being registered. This is a generic type parameter that allows this method to be used for any specific BlockEntity type,
     *           ensuring type safety when retrieving the registered BlockEntityType later on.
     */
    public static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerBlockEntity(ResourceLocation id, Supplier<BlockEntityType<T>> blockEntityType) {
        return registerDeferred(Registries.BLOCK_ENTITY_TYPE, id, blockEntityType);
    }

    /**
     * Registers a Block with the given ResourceLocation ID and supplier. This method uses the DeferredRegister system to ensure that the registration
     * occurs at the correct time during mod loading. The returned Supplier can be used to retrieve the registered Block after it has been registered.
     * The registration is cached based on the registry key and namespace to ensure that multiple registrations for the same registry and namespace
     * reuse the same DeferredRegister instance.
     * @param id The ResourceLocation ID to register the Block under. This should be unique within the mod's namespace to avoid conflicts with other mods.
     * @param block A Supplier that provides the Block instance to be registered. This supplier will be called during the registration process to
     *              create the actual Block object.
     * @return A Supplier that can be used to retrieve the registered Block after registration. This allows for lazy retrieval of the registered
     * object, which is useful in cases where the registration may not have occurred yet at the time of calling this method.
     * @param <T> The type of Block being registered. This is a generic type parameter that allows this method to be used for any specific Block
     *           type, ensuring type safety when retrieving the registered Block later on.
     */
    public static <T extends Block> Supplier<T> registerBlock(ResourceLocation id, Supplier<T> block) {
        return registerDeferred(Registries.BLOCK, id, block);
    }

    /**
     * Registers an Item with the given ResourceLocation ID and supplier. This method uses the DeferredRegister system to ensure that the registration
     * occurs at the correct time during mod loading. The returned
     * Supplier can be used to retrieve the registered Item after it has been registered. The registration is cached based on the registry key and
     * namespace to ensure that multiple registrations for the same registry and namespace reuse the same DeferredRegister instance.
     * @param id The ResourceLocation ID to register the Item under. This should be unique within the mod's namespace to avoid conflicts with other mods.
     * @param item A Supplier that provides the Item instance to be registered. This supplier will be called during the registration process to
     *             create the actual Item object.
     * @return A Supplier that can be used to retrieve the registered Item after registration. This allows for lazy retrieval of the registered object, which is useful in cases where the registration may not have occurred yet at the time of calling this method.
     * @param <T> The type of Item being registered. This is a generic type parameter that allows this method to be used for any specific Item
     *           type, ensuring type safety when retrieving the registered Item later on.
     */
    public static <T extends Item> Supplier<T> registerItem(ResourceLocation id, Supplier<T> item) {
        return registerDeferred(Registries.ITEM, id, item);
    }

    /**
     * Registers a SoundEvent with the given ResourceLocation ID and supplier. This method uses the DeferredRegister system to ensure that the
     * registration occurs at the correct time during mod loading. The returned
     * Supplier can be used to retrieve the registered SoundEvent after it has been registered. The registration is cached based on the registry key and
     * namespace to ensure that multiple registrations for the same registry and namespace reuse the same DeferredRegister instance.
     * @param id The ResourceLocation ID to register the SoundEvent under. This should be unique within the mod's namespace to avoid conflicts with other mods.
     * @param sound A Supplier that provides the SoundEvent instance to be registered. This supplier will be called during the registration process to
     *              create the actual SoundEvent object.
     * @return A Supplier that can be used to retrieve the registered SoundEvent after registration. This allows for lazy retrieval of the registered
     * object, which is useful in cases where the registration may not have occurred yet at the time of calling this method.
     * @param <T> The type of SoundEvent being registered. This is a generic type parameter that allows this method to be used for any specific SoundEvent
     *           type, ensuring type safety when retrieving the registered SoundEvent later on.
     */
    public static <T extends SoundEvent> Supplier<T> registerSound(ResourceLocation id, Supplier<T> sound) {
        return registerDeferred(Registries.SOUND_EVENT, id, sound);
    }

    /**
     * Registers a CreativeModeTab with the given ResourceLocation ID and supplier. This method uses the DeferredRegister system to ensure that the
     * registration occurs at the correct time during mod loading. The returned
     * Supplier can be used to retrieve the registered CreativeModeTab after it has been registered. The registration is cached based on the registry key
     * and namespace to ensure that multiple registrations for the same registry and namespace reuse the same DeferredRegister instance.
     * @param id The ResourceLocation ID to register the CreativeModeTab under. This should be unique within the mod's namespace to avoid conflicts with other mods.
     * @param tab A Supplier that provides the CreativeModeTab instance to be registered. This supplier will be called during the registration process to
     *            create the actual CreativeModeTab object.
     * @return A Supplier that can be used to retrieve the registered CreativeModeTab after registration. This allows for lazy retrieval of the registered
     * object, which is useful in cases where the registration may not have occurred yet at the time of calling this method.
     * @param <T> The type of CreativeModeTab being registered. This is a generic type parameter that allows this method to be used for any specific
     *           CreativeModeTab type, ensuring type safety when retrieving the registered CreativeModeTab later on.
     */
    public static <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(ResourceLocation id, Supplier<T> tab) {
        return registerDeferred(Registries.CREATIVE_MODE_TAB, id, tab);
    }

    /**
     * Registers a CriterionTrigger with the given ResourceLocation ID and supplier. This method uses the DeferredRegister system to ensure that the registration
     * occurs at the correct time during mod loading. The returned
     * Supplier can be used to retrieve the registered CriterionTrigger after it has been registered. The registration is cached based on the registry key and
     * namespace to ensure that multiple registrations for the same registry and namespace reuse the same DeferredRegister instance.
     * @param id The ResourceLocation ID to register the CriterionTrigger under. This should be unique within the mod's namespace to avoid conflicts with other mods.
     * @param criterionTrigger A Supplier that provides the CriterionTrigger instance to be registered. This supplier will be called during the registration
     *                         process to create the actual CriterionTrigger object.
     * @return A Supplier that can be used to retrieve the registered CriterionTrigger after registration. This allows for lazy retrieval of the registered
     * object, which is useful in cases where the registration may not have occurred yet at the time of calling this method.
     * @param <T> The type of CriterionTrigger being registered. This is a generic type parameter that allows this method to be used for any specific
     *           CriterionTrigger type, ensuring type safety when retrieving the registered CriterionTrigger later on.
     */
    public static <T extends CriterionTrigger<?>> Supplier<T> registerCriteriaTriggers(ResourceLocation id, Supplier<T> criterionTrigger) {
        return registerDeferred(Registries.TRIGGER_TYPE, id, criterionTrigger);
    }

    /**
     * Registers a custom stat with the given ResourceLocation ID and supplier. This method uses the DeferredRegister system to ensure that the registration
     * occurs at the correct time during mod loading. The returned
     * Supplier can be used to retrieve the registered stat after it has been registered. The registration is cached based on the registry key and namespace to
     * ensure that multiple registrations for the same registry and namespace reuse the same DeferredRegister instance.
     * @param id The ResourceLocation ID to register the stat under. This should be unique within the mod's namespace to avoid conflicts with other mods.
     * @param stats A Supplier that provides the stat instance to be registered. This supplier will be called during the registration process to create the
     *              actual stat object.
     * @return A Supplier that can be used to retrieve the registered stat after registration. This allows for lazy retrieval of the registered object, which
     * is useful in cases where the registration may not have occurred yet at the time of calling this method.
     * @param <T> The type of stat being registered. This is a generic type parameter that allows this method to be used for any specific stat type, ensuring
     *           type safety when retrieving the registered stat later on.
     */
    public static <T extends ResourceLocation> Supplier<T> registerStats(ResourceLocation id, Supplier<T> stats) {
        return registerDeferred(Registries.CUSTOM_STAT, id, stats);
    }

    /**
     * Registers a MenuType with the given ResourceLocation ID and supplier. This method uses the DeferredRegister system to ensure that the registration occurs at
     * the correct time during mod loading. The returned
     * Supplier can be used to retrieve the registered MenuType after it has been registered. The registration is cached based on the registry key and namespace to
     * ensure that multiple registrations for the same registry and namespace reuse the same DeferredRegister instance.
     * @param id The ResourceLocation ID to register the MenuType under. This should be unique within the mod's namespace to avoid conflicts with other mods.
     * @param menuType A Supplier that provides the MenuType instance to be registered. This supplier will be called during the registration process to create
     *                 the actual MenuType object.
     * @return A Supplier that can be used to retrieve the registered MenuType after registration. This allows for lazy retrieval of the registered object,
     * which is useful in cases where the registration may not have occurred yet at the time of calling this method.
     * @param <T> The type of MenuType being registered. This is a generic type parameter that allows this method to be used for any specific MenuType type,
     *           ensuring type safety when retrieving the registered MenuType later on.
     */
    public static <T extends MenuType<?>> Supplier<T> registerMenuType(ResourceLocation id, Supplier<T> menuType) {
        return registerDeferred(Registries.MENU, id, menuType);
    }

    /**
     * Registers a DataComponentType with the given ResourceLocation ID and supplier. This method uses the DeferredRegister system to ensure that the registration occurs
     * at the correct time during mod loading. The returned
     * Supplier can be used to retrieve the registered DataComponentType after it has been registered. The registration is cached based on the registry key and namespace
     * to ensure that multiple registrations for the same registry and namespace reuse the same DeferredRegister instance. Note that DataComponentType registration is handled
     * separately from other registries due to its unique nature in the Minecraft codebase, and thus it uses a dedicated method to manage its DeferredRegister instances.
     * @param id The ResourceLocation ID to register the DataComponentType under. This should be unique within the mod's namespace to avoid conflicts with other mods.
     * @param component A Supplier that provides the DataComponentType instance to be registered. This supplier will be called during the registration process to create
     *                  the actual DataComponentType object.
     * @return A Supplier that can be used to retrieve the registered DataComponentType after registration. This allows for lazy retrieval of the registered object,
     * which is useful in cases where the registration may not have occurred yet at the time of calling this method.
     * @param <T> The type of DataComponentType being registered. This is a generic type parameter that allows this method to be used for any specific DataComponentType
     *           type, ensuring type safety when retrieving the registered DataComponentType later on.
     */
    public static <T extends DataComponentType<?>> Supplier<T> registerDataComponentType(ResourceLocation id, Supplier<T> component) {
        return registerDataComponentDeferred(id, component);
    }

    /**
     * Registers an EnchantmentEntityEffect MapCodec with the given ResourceLocation name and supplier. This method uses the DeferredRegister system to ensure that
     * the registration occurs at the correct time during mod loading.
     * The returned Supplier can be used to retrieve the registered MapCodec after it has been registered. The registration is cached based on the registry key and
     * namespace to ensure that multiple registrations for the same registry and namespace reuse the same DeferredRegister instance.
     * Note that EnchantmentEntityEffect MapCodec registration is handled separately from other registries due to its unique nature in the Minecraft codebase, and thus
     * it uses a dedicated method to manage its DeferredRegister instances.
     * @param name The ResourceLocation name to register the EnchantmentEntityEffect MapCodec under. This should be unique within the mod's namespace to
     *             avoid conflicts with other mods.
     * @param codec A Supplier that provides the MapCodec instance to be registered. This supplier will be called during the registration process to create
     *              the actual MapCodec object.
     * @return A Supplier that can be used to retrieve the registered MapCodec after registration. This allows for lazy retrieval of the registered object,
     * which is useful in cases where the registration may not have occurred yet at the time of calling this method.
     * @param <T> The type of MapCodec being registered, which must be a MapCodec that produces an EnchantmentEntityEffect. This is a generic type parameter
     *           that allows this method to be used for any specific MapCodec type that meets this requirement, ensuring type safety when retrieving the registered
     *           MapCodec later on.
     */
    public static <T extends MapCodec<? extends EnchantmentEntityEffect>> Supplier<T> registerEntityEffects(ResourceLocation name, Supplier<T> codec) {
        return registerDeferred(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, name, codec);
    }

    /**
     * Internal helper method to handle the common logic of registering entries using DeferredRegister. This method checks the cache for an existing
     * DeferredRegister for the given registry key and namespace, creates one if it doesn't exist, and then registers the entry using that DeferredRegister.
     * This ensures that all registrations for a given registry and namespace share the same DeferredRegister instance, which is important for proper registration
     * timing and event handling in NeoForge.
     * @param registryKey The registry key to register the entry under. This is typically a ResourceKey from the Registries class, such as Registries.BLOCK
     *                    or Registries.ITEM, which identifies the type of content being registered.
     * @param id The ResourceLocation ID to register the entry under. This should be unique within the mod's namespace to avoid conflicts with other mods.
     *           The ID is used as the path for registration, while the namespace is derived from the ID's namespace.
     * @param entrySupplier A Supplier that provides the instance to be registered. This supplier will be called during the registration process to create
     *                      the actual object being registered (e.g., a Block, Item, SoundEvent, etc.).
     * @return A Supplier that can be used to retrieve the registered object after registration. This allows for lazy retrieval of the registered object,
     * which is useful in cases where the registration may not have occurred yet at the time of calling this method.
     * @param <T> The type of object being registered. This is a generic type parameter that allows this method to be used for any specific type of content,
     *           ensuring type safety when retrieving the registered object later on.
     */
    private static <T> Supplier<T> registerDeferred(Object registryKey, ResourceLocation id, Supplier<T> entrySupplier) {
        DeferredRegister<T> deferredRegister = getOrCreateDeferredRegister(registryKey, id.getNamespace());
        return deferredRegister.register(id.getPath(), entrySupplier);
    }

    /**
     * Internal helper method specifically for registering DataComponentType entries using DeferredRegister. This method checks the cache for an
     * existing DeferredRegister for DataComponentType in the given namespace, creates one if it doesn't exist, and then registers the entry using that
     * DeferredRegister. This is necessary because DataComponentType registration is handled differently in the Minecraft codebase, and thus it requires a
     * dedicated method to manage its DeferredRegister instances properly.
     * @param id The ResourceLocation ID to register the DataComponentType under. This should be unique within the mod's namespace to avoid conflicts
     *           with other mods. The ID is used as the path for registration, while the namespace is derived from the ID's namespace.
     * @param entrySupplier A Supplier that provides the DataComponentType instance to be registered. This supplier will be called during the
     *                      registration process to create the actual DataComponentType object.
     * @return A Supplier that can be used to retrieve the registered DataComponentType after registration. This allows for lazy retrieval of the
     * registered object, which is useful in cases where the registration may not have occurred yet at the time of calling this method.
     * @param <T> The type of DataComponentType being registered. This is a generic type parameter that allows this method to be used for any specific
     *           DataComponentType type, ensuring type safety when retrieving the registered DataComponentType later on.
     */
    private static <T> Supplier<T> registerDataComponentDeferred(ResourceLocation id, Supplier<T> entrySupplier) {
        DeferredRegister<T> deferredRegister = getOrCreateDataComponentDeferredRegister(id.getNamespace());
        return deferredRegister.register(id.getPath(), entrySupplier);
    }

    /**
     * Internal helper method to retrieve an existing DeferredRegister for the given registry key and namespace, or create a new one if it
     * doesn't exist. This method checks the cache for a DeferredRegister instance associated with the combination of registry key and namespace,
     * and if it doesn't find one, it creates a new DeferredRegister using the NeoForge API, registers it with the mod event bus, and stores it in
     * the cache before returning it. This ensures that all registrations for a given registry and namespace share the same DeferredRegister instance,
     * which is important for proper registration timing and event handling in NeoForge.
     * @param registryKey The registry key to retrieve or create the DeferredRegister for. This is typically a ResourceKey from the Registries
     *                    class, such as Registries.BLOCK or Registries.ITEM, which identifies the type of content being registered.
     * @param namespace The namespace to retrieve or create the DeferredRegister for. This is typically derived from the ResourceLocation ID of
     *                  the entry being registered, and it is used to ensure that registrations from different mods (with different namespaces)
     *                  do not conflict with each other.
     * @return The DeferredRegister instance associated with the given registry key and namespace. If a DeferredRegister already exists in the
     * cache for that combination, it will be returned; otherwise, a new DeferredRegister will be created, registered with the mod event bus,
     * stored in the cache, and then returned.
     * @param <T> The type of objects that the DeferredRegister will handle. This is a generic type parameter that allows this method to be used
     *           for any specific type of content, ensuring type safety when registering entries with the returned DeferredRegister.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <T> DeferredRegister<T> getOrCreateDeferredRegister(Object registryKey, String namespace) {
        IEventBus eventBus = modBus;
        if (eventBus == null) {
            throw new IllegalStateException("NeoForgeRegistryHelper has not been initialized yet");
        }

        String cacheKey = registryKey.toString() + "|" + namespace;
        return (DeferredRegister<T>) DEFERRED_REGISTERS.computeIfAbsent(cacheKey, key -> {
            DeferredRegister<T> deferredRegister = DeferredRegister.create((net.minecraft.resources.ResourceKey) registryKey, namespace);
            deferredRegister.register(eventBus);
            return deferredRegister;
        });
    }

    /**
     * Internal helper method to retrieve an existing DeferredRegister for DataComponentType in the given namespace, or create a new one if it
     * doesn't exist. This method checks the cache for a DeferredRegister instance associated with the combination of DataComponentType registry
     * and namespace, and if it doesn't find one, it creates a new DeferredRegister using the NeoForge API, registers it with the mod event bus,
     * and stores it in the cache before returning it. This is necessary because DataComponentType registration is handled differently in the Minecraft
     * codebase, and thus it requires a dedicated method to manage its DeferredRegister instances properly.
     * @param namespace The namespace to retrieve or create the DeferredRegister for. This is typically derived from the ResourceLocation
     *                  ID of the DataComponentType being registered, and it is used to ensure that registrations from different mods (with different namespaces)
     *                  do not conflict with each other.
     * @return The DeferredRegister instance associated with the DataComponentType registry and the given namespace. If a DeferredRegister
     * already exists in the cache for that combination, it will be returned; otherwise, a new DeferredRegister will be created, registered
     * with the mod event bus, stored in the cache, and then returned.
     * @param <T> The type of DataComponentType being registered. This is a generic type parameter that allows this method to be
     *           used for any specific DataComponentType type, ensuring type safety when registering entries with the returned DeferredRegister.
     */
    @SuppressWarnings({"unchecked"})
    private static <T> DeferredRegister<T> getOrCreateDataComponentDeferredRegister(String namespace) {
        IEventBus eventBus = modBus;
        if (eventBus == null) {
            throw new IllegalStateException("NeoForgeRegistryHelper has not been initialized yet");
        }

        String cacheKey = Registries.DATA_COMPONENT_TYPE + "|" + namespace;
        return (DeferredRegister<T>) DEFERRED_REGISTERS.computeIfAbsent(cacheKey, key -> {
            DeferredRegister<T> deferredRegister = (DeferredRegister<T>) DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, namespace);
            deferredRegister.register(eventBus);
            return deferredRegister;
        });
    }
}