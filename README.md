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
- Unified client screen registration with a static queue/map-style approach that works with loader timing differences

## Versions

From `gradle.properties`:

- Minecraft: `1.21.1`
- Fabric Loader: `0.17.2+`
- Fabric API: `0.116.6+1.21.1`
- NeoForge: `21.1.182+`
- Java target: `21`

## Consuming as a Dependency

Use the published artifact coordinates from Maven Central:

- Group: `dev.matthiesen`
- Artifact: `matthiesen-lib-<platform>`
- Version: `${matthiesen_lib_version}` (defined in your `gradle.properties`)

> You can find the latest version for your platform on [Maven Central](https://central.sonatype.com/search?q=matthiesen-lib).

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    modImplementation("dev.matthiesen:matthiesen-lib-common:${property("matthiesen_lib_version")}") // for common API
    modImplementation("dev.matthiesen:matthiesen-lib-fabric:${property("matthiesen_lib_version")}") // for Fabric-specific API
    modImplementation("dev.matthiesen:matthiesen-lib-neoforge:${property("matthiesen_lib_version")}") // for NeoForge-specific API
}
```


If you publish platform-specific artifacts, depend on the matching one for your loader and keep `common` on compile/runtime classpath as needed by your build setup.

## Common API Usage

Register content from common code:

Example:

```java
public static final Supplier<Item> MY_ITEM = MatthiesenLib.registerItem(
        ResourceLocation.fromNamespaceAndPath("examplemod", "my_item"),
        () -> new Item(new Item.Properties())
);
```

All `MatthiesenLib.registerX(...)` methods return `Supplier<T>` so you can safely reference values before actual loader registration runs.

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

## Unified Screen Registration

Register menu screens from common/client code:

```java
MatthiesenLibClient.registerMenuScreen(MY_MENU_TYPE_SUPPLIER, MyMenuScreen::new);
```

or

```java
MatthiesenLibClient.registerMenuScreen(MY_MENU_TYPE, MyMenuScreen::new);
```

### Why this works on both loaders

Screens are stored in a shared static list first, then applied when each loader reaches the correct lifecycle stage:

- Fabric: applied during `onInitializeClient()`
- NeoForge: applied during `RegisterMenuScreensEvent`

This avoids event-order issues where NeoForge screen events can fire before later setup callbacks.

## Notes for Library Consumers

- Prefer registering things in static init/bootstrap of your own registries, then use returned `Supplier<T>`.
- For client-only screen registration, call `MatthiesenLibClient.registerMenuScreen(...)` from your client init path.
- For commands, register `AbstractCommand` implementations via `MatthiesenLib.registerCommand(...)`.

## License

MIT - see `LICENSE`.
