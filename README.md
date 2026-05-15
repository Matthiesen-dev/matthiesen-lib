# Matthiesen Lib

A lightweight Architectury-style library for shared mod code across **Fabric** and **NeoForge** (Minecraft **1.21.1**).

## What This Library Provides

- Unified content registration from common code:
  - blocks
  - items
  - block entities
  - sounds
  - creative tabs
  - criteria triggers
  - stats
  - menu types
  - data components
  - enchantment entity effects
- Unified command registration (`AbstractCommand` + common API)
- Unified permission system (`Permission`, `PermissionLevel`, `PermissionValidator`)
- Unified client screen registration with a static queue/map-style approach that works with loader timing differences

## Versions

From `gradle.properties`:

- Minecraft: `1.21.1`
- Fabric Loader: `0.17.2+`
- Fabric API: `0.116.6+1.21.1`
- NeoForge: `21.1.182+`
- Java target: `21`

## Consuming as a Dependency

- Group: `dev.matthiesen`
- Artifact: `matthiesen-lib-<platform>`
- Version: `${matthiesen_lib_version}` (defined in your `gradle.properties`)

You can depend on the common API artifact in your shared code, and optionally the platform-specific artifact in your platform-specific code if you need loader-specific features.

```kotlin
repositories {
    maven("https://maven.matthiesen.dev/releases")
}

dependencies {
    modImplementation("dev.matthiesen:matthiesen-lib-common:${property("matthiesen_lib_version")}") // for common API
    modImplementation("dev.matthiesen:matthiesen-lib-fabric:${property("matthiesen_lib_version")}") // for Fabric-specific API
    modImplementation("dev.matthiesen:matthiesen-lib-neoforge:${property("matthiesen_lib_version")}") // for NeoForge-specific API
}
```

> You can find the latest version for your platform on [Matthiesen Dev Maven](https://maven.matthiesen.dev/).


If you publish platform-specific artifacts, depend on the matching one for your loader and keep `common` on compile/runtime classpath as needed by your build setup.

## Common API Usage

### Recommended: Using RegistryBuilder

Register content from common code using `RegistryBuilder` for automatic mod ID prefixing:

```java
public static final RegistryBuilder REGISTRY = new RegistryBuilder("examplemod");

public static final Supplier<Item> MY_ITEM = REGISTRY.registerItem(
        "my_item",
        () -> new Item(new Item.Properties())
);

public static final Supplier<Block> MY_BLOCK = REGISTRY.registerBlock(
        "my_block",
        () -> new Block(BlockBehaviour.Properties.of())
);

public static final Supplier<SoundEvent> MY_SOUND = REGISTRY.registerSound(
        "my_sound",
        () -> SoundEvent.createVariableRangeEvent(
            ResourceLocation.fromNamespaceAndPath("examplemod", "my_sound")
        )
);
```

The `RegistryBuilder` automatically prefixes your mod ID to all registered content IDs and supports registering:
- items
- blocks
- block entities
- creative tabs
- sounds
- criteria triggers
- statistics
- menu types
- data components
- enchantment entity effects

All registration methods return `Supplier<T>` so you can safely reference values before actual loader registration runs.

### Legacy: Direct Registration Methods (Deprecated)

> ⚠️ **Deprecated**: The direct `MatthiesenLib.registerX(...)` methods are deprecated and planned for removal. Please use `RegistryBuilder` for new code.

```java
// ❌ Not recommended - will be removed
public static final Supplier<Item> MY_ITEM = MatthiesenLib.registerItem(
        ResourceLocation.fromNamespaceAndPath("examplemod", "my_item"),
        () -> new Item(new Item.Properties())
);
```

If you have existing code using direct methods, migrate to `RegistryBuilder` for better organization and consistency.

## Unified Commands

### Create a command

Extend `AbstractCommand`:

```java
public class PingCommand extends AbstractCommand {
    @Override
    public void register(CommandDispatcher<CommandSourceStack> dispatcher,
                         CommandBuildContext registry,
                         Commands.CommandSelection context) {
        dispatcher.register(
                Commands.literal("ping")
                        .executes(this::action)
        );
    }

    @Override
    public int action(CommandContext<CommandSourceStack> context) {
        context.getSource().sendSuccess(() -> Component.literal("pong"), false);
        return 1;
    }
}
```

### Register the command

```java
MatthiesenLib.registerCommand(new PingCommand());
```

The library queues commands if needed and binds them when Fabric/NeoForge command registration events fire.

## Permission System

Matthiesen Lib includes a cross-loader permission API that lets you define permissions in common code and validate them with a platform-aware validator.

### Register a permission

Create a `Permission` and register it through the common API:

```java
public static final Permission ADMIN_PERMISSION = new Permission() {
    @Override
    public ResourceLocation getIdentifier() {
        return ResourceLocation.fromNamespaceAndPath("examplemod", "admin");
    }

    @Override
    public String getLiteral() {
        return "examplemod.admin";
    }

    @Override
    public PermissionLevel getLevel() {
        return PermissionLevel.MULTIPLAYER_MANAGEMENT;
    }
};

MatthiesenLib.registerPermission(ADMIN_PERMISSION);
```

### Check a permission

Use the active validator from common code:

```java
if (MatthiesenLib.getPermissionValidator().hasPermission(context.getSource(), ADMIN_PERMISSION)) {
    // allowed
}
```

You can also check by literal and level:

```java
boolean allowed = MatthiesenLib.getPermissionValidator()
        .hasPermission(context.getSource(), "examplemod.admin", PermissionLevel.MULTIPLAYER_MANAGEMENT.getNumericalValue());
```

### Platform behavior

- Default/fallback: vanilla permission levels (`source.hasPermission(level)`)
- Fabric: uses `fabric-permissions-api-v0` when present, otherwise falls back to vanilla levels
- NeoForge: uses NeoForge `PermissionAPI` nodes for registered permissions

## Unified Screen Registration

Register menu screens from common/client code:

```java
MatthiesenLibClient.registerMenuScreen(MY_MENU_TYPE_SUPPLIER, MyMenuScreen::new);
```

or

```java
MatthiesenLibClient.registerMenuScreen(MY_MENU_TYPE, MyMenuScreen::new);
```

To register multiple screens at once:

```java
MatthiesenLibClient.registerMenuScreens(register -> {
    register.register(MY_MENU_TYPE_SUPPLIER, MyMenuScreen::new);
    register.register(OTHER_MENU_TYPE_SUPPLIER, OtherMenuScreen::new);
});
```

This bulk registration API is the preferred way to queue multiple menu screens.

### Why this works on both loaders

Screens are stored in a shared static list first, then applied when each loader reaches the correct lifecycle stage:

- Fabric: applied during `onInitializeClient()`
- NeoForge: applied during `RegisterMenuScreensEvent`

This avoids event-order issues where NeoForge screen events can fire before later setup callbacks.

## Notes for Library Consumers

- **Use `RegistryBuilder`** for all content registration - create an instance with your mod ID and call its methods to register items, blocks, and other content. This provides automatic mod ID prefixing and better code organization.
- Register content in static init/bootstrap of your own registries, then use the returned `Supplier<T>`.
- For client-only screen registration, call `MatthiesenLibClient.registerMenuScreen(...)` from your client init path.
- For commands, register `AbstractCommand` implementations via `MatthiesenLib.registerCommand(...)`.
- Avoid using the deprecated direct `MatthiesenLib.registerX(...)` methods - these are planned for removal. Use `RegistryBuilder` instead.

## License

MIT - see `LICENSE`.
