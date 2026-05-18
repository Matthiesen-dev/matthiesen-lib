# Embers Text API Integration

This guide covers how to use **Matthiesen Lib** to send customized immersive messages to players using the **Ember's Text API** mod.

## Overview

Matthiesen Lib provides a platform-agnostic compatibility layer for Ember's Text API. It works identically on both **Fabric** and **NeoForge**, abstracting away platform differences and version variations in the Ember API.

Access the compatibility layer through the parser system:

```java
MatthiesenLibTextParser parser = MatthiesenLib.getTextParser(MatthiesenLibBuiltInTextParsers.EMBER);
MatthiesenLibEmbersTextParserCompat compat = parser.getEmbersCompat();
```

Always check that `compat` is not `null` before using it, in case Ember's Text API is not installed.

## Quick Start

### Basic Message

```java
// Get the Ember's Text API compatibility layer from the parser
MatthiesenLibTextParser parser = MatthiesenLib.getTextParser(MatthiesenLibBuiltInTextParsers.EMBER);
MatthiesenLibEmbersTextParserCompat compat = parser.getEmbersCompat();

if (compat != null) {
    compat.sendMessage(player, "Hello World", 100f);
}
```

This sends a simple text message to the player for 100 ticks (5 seconds).

### Customized Message with Inline Builder (Convenience)

```java
// Get the Ember's Text API compatibility layer
MatthiesenLibTextParser parser = MatthiesenLib.getTextParser(MatthiesenLibBuiltInTextParsers.EMBER);
MatthiesenLibEmbersTextParserCompat compat = parser.getEmbersCompat();

if (compat != null) {
    compat.sendMessage(player, "Customized Message", 100f, builder -> builder
        .anchor(MatthiesenLibImmersiveMessageBuilder.TextAnchor.TOP_CENTER)
        .align(MatthiesenLibImmersiveMessageBuilder.TextAlign.CENTER)
        .scale(1.5f)
        .shadow(true)
        .fadeInTicks(10)
        .fadeOutTicks(20));
}
```

The `builder -> ...` overload creates a new builder internally so you do not need to pre-create one.

## API Reference

### Obtaining the Compatibility Layer

The Ember's Text API compatibility layer is accessed through the parser system:

```java
MatthiesenLibTextParser parser = MatthiesenLib.getTextParser(MatthiesenLibBuiltInTextParsers.EMBER);
MatthiesenLibEmbersTextParserCompat compat = parser.getEmbersCompat();
```

**Important:** Always check that the result is not null before using it:

```java
if (compat != null) {
    // Use compat layer...
}
```

### MatthiesenLibEmbersTextParserCompat

Main interface for sending immersive messages.

#### Methods

| Method | Description |
|--------|-------------|
| `sendMessage(ServerPlayer, Component, float)` | Send a message using a Minecraft `Component` |
| `sendMessage(ServerPlayer, Component, float, Builder)` | Send a Component message with custom formatting via builder |
| `sendMessage(ServerPlayer, Component, float, Consumer<Builder>)` | Send a Component message with inline builder configuration (`builder -> ...`) |
| `sendMessage(ServerPlayer, String, float)` | Send a message using plain text (string) |
| `sendMessage(ServerPlayer, String, float, Builder)` | Send a message with custom formatting via builder |
| `sendMessage(ServerPlayer, String, float, Consumer<Builder>)` | Send a message with inline builder configuration (`builder -> ...`) |
| `sendUpdateMessage(ServerPlayer, String, Component, float)` | Update an existing message by ID using a Component |
| `sendUpdateMessage(ServerPlayer, String, Component, float, Builder)` | Update a Component message with custom formatting |
| `sendUpdateMessage(ServerPlayer, String, Component, float, Consumer<Builder>)` | Update a Component message with inline builder configuration (`builder -> ...`) |
| `sendUpdateMessage(ServerPlayer, String, String, float)` | Update an existing message by ID using plain text |
| `sendUpdateMessage(ServerPlayer, String, String, float, Builder)` | Update an existing message with custom formatting |
| `sendUpdateMessage(ServerPlayer, String, String, float, Consumer<Builder>)` | Update a message with inline builder configuration (`builder -> ...`) |
| `sendCloseMessage(ServerPlayer, String)` | Close a specific message by ID |
| `sendCloseAllMessages(ServerPlayer)` | Close all active messages for a player |

### MatthiesenLibImmersiveMessageBuilder

Fluent builder for configuring message appearance and behavior.

#### Factory Method

```java
MatthiesenLibImmersiveMessageBuilder builder = MatthiesenLibImmersiveMessageBuilder.create();
```

#### Configuration Methods

| Method | Parameter(s) | Description | Default |
|--------|-------------|-------------|---------|
| `anchor(TextAnchor)` | Screen position | Where the message appears | `TOP_CENTER` |
| `align(TextAlign)` | Horizontal alignment | Text alignment relative to anchor | `CENTER` |
| `offset(float, float)` | X, Y pixels | Pixel offset from anchor point | `(0, 55)` |
| `scale(float)` | Scale multiplier | Text size (1.0 = normal) | `1.0` |
| `shadow(boolean)` | Enable/disable | Render text shadows | `true` |
| `background(boolean)` | Enable/disable | Show background panel | `false` |
| `fadeInTicks(int)` | Ticks (20 = 1s) | Duration to fade in | `0` |
| `fadeOutTicks(int)` | Ticks (20 = 1s) | Duration to fade out | `0` |
| `typewriter(float)` | Chars/tick | Typewriter effect speed | Disabled |
| `typewriter(float, boolean)` | Speed, center | Typewriter with centering | Disabled |
| `wrap(int)` | Width in pixels | Text wrap width | Disabled |

#### Enum Types

##### TextAnchor

Screen positions (3×3 grid):
- `TOP_LEFT`, `TOP_CENTER`, `TOP_RIGHT`
- `MIDDLE_LEFT`, `MIDDLE`, `MIDDLE_RIGHT`
- `BOTTOM_LEFT`, `BOTTOM_CENTER`, `BOTTOM_RIGHT`

##### TextAlign

Horizontal alignment relative to anchor:
- `LEFT` – align text left
- `CENTER` – center text (default)
- `RIGHT` – align text right

## Examples

### Example 1: Top-Center Status Message

```java
MatthiesenLibTextParser parser = MatthiesenLib.getTextParser(MatthiesenLibBuiltInTextParsers.EMBER);
MatthiesenLibEmbersTextParserCompat compat = parser.getEmbersCompat();

if (compat != null) {
    compat.sendMessage(player, "Your quest has been updated!", 80f);
}
```

Appears at the top-center by default.

### Example 2: Large, Centered, Glowing Message

```java
MatthiesenLibTextParser parser = MatthiesenLib.getTextParser(MatthiesenLibBuiltInTextParsers.EMBER);
MatthiesenLibEmbersTextParserCompat compat = parser.getEmbersCompat();

if (compat != null) {
    MatthiesenLibImmersiveMessageBuilder builder = MatthiesenLibImmersiveMessageBuilder.create()
        .anchor(MatthiesenLibImmersiveMessageBuilder.TextAnchor.MIDDLE)
        .scale(2.0f)
        .shadow(true)
        .fadeInTicks(5)
        .fadeOutTicks(10);

    compat.sendMessage(player, "Victory!", 120f, builder);
}
```

Appears at the center with double size, fades in/out gracefully.

### Example 2b: Styled Component Message with Inline Builder

```java
MatthiesenLibTextParser parser = MatthiesenLib.getTextParser(MatthiesenLibBuiltInTextParsers.EMBER);
MatthiesenLibEmbersTextParserCompat compat = parser.getEmbersCompat();

if (compat != null) {
    Component msg = Component.literal("Dungeon Cleared!");

    compat.sendMessage(player, msg, 120f, builder -> builder
        .anchor(MatthiesenLibImmersiveMessageBuilder.TextAnchor.MIDDLE)
        .scale(1.8f)
        .fadeInTicks(6)
        .fadeOutTicks(10));
}
```

### Example 3: Typewriter Effect

```java
MatthiesenLibTextParser parser = MatthiesenLib.getTextParser(MatthiesenLibBuiltInTextParsers.EMBER);
MatthiesenLibEmbersTextParserCompat compat = parser.getEmbersCompat();

if (compat != null) {
    MatthiesenLibImmersiveMessageBuilder builder = MatthiesenLibImmersiveMessageBuilder.create()
        .typewriter(0.5f, true)  // 0.5 chars per tick, centered while typing
        .offset(0f, 150f)
        .anchor(MatthiesenLibImmersiveMessageBuilder.TextAnchor.BOTTOM_CENTER);

    compat.sendMessage(player, "A mysterious message unfolds...", 200f, builder);
}
```

Text reveals character-by-character from bottom-center.

### Example 4: Alert with Background

```java
MatthiesenLibTextParser parser = MatthiesenLib.getTextParser(MatthiesenLibBuiltInTextParsers.EMBER);
MatthiesenLibEmbersTextParserCompat compat = parser.getEmbersCompat();

if (compat != null) {
    MatthiesenLibImmersiveMessageBuilder builder = MatthiesenLibImmersiveMessageBuilder.create()
        .anchor(MatthiesenLibImmersiveMessageBuilder.TextAnchor.TOP_CENTER)
        .background(true)
        .scale(1.2f)
        .shadow(true);

    compat.sendMessage(player, "⚠ Warning!", 150f, builder);
}
```

Displays with a background panel for emphasis.

### Example 5: Updatable Message (Progress Bar or Status)

```java
MatthiesenLibTextParser parser = MatthiesenLib.getTextParser(MatthiesenLibBuiltInTextParsers.EMBER);
MatthiesenLibEmbersTextParserCompat compat = parser.getEmbersCompat();

if (compat != null) {
    String messageId = "task_progress_" + player.getUUID();

    // Send initial message
    compat.sendMessage(player, "Task starting...", 300f, builder -> builder
        .anchor(MatthiesenLibImmersiveMessageBuilder.TextAnchor.MIDDLE_LEFT)
        .offset(100f, 0f));

    // Later, update the message with progress
    compat.sendUpdateMessage(player, messageId, "Task: 50% complete", 300f, builder -> builder
        .anchor(MatthiesenLibImmersiveMessageBuilder.TextAnchor.MIDDLE_LEFT)
        .offset(100f, 0f));

    // When done
    compat.sendCloseMessage(player, messageId);
}
```

### Reusing a Builder Instance (Optional)

If you send many messages with exactly the same style, you can still create and reuse a builder instance:

```java
MatthiesenLibImmersiveMessageBuilder statusStyle = MatthiesenLibImmersiveMessageBuilder.create()
    .anchor(MatthiesenLibImmersiveMessageBuilder.TextAnchor.TOP_CENTER)
    .background(true)
    .scale(1.1f);

compat.sendMessage(player, "Wave 1 incoming", 120f, statusStyle);
compat.sendUpdateMessage(player, "wave_status", "Wave 2 incoming", 120f, statusStyle);
```

## Common Patterns

### Greeting on Join

```java
public static void onPlayerJoin(ServerPlayer player) {
    MatthiesenLibTextParser parser = MatthiesenLib.getTextParser(MatthiesenLibBuiltInTextParsers.EMBER);
    MatthiesenLibEmbersTextParserCompat compat = parser.getEmbersCompat();
    if (compat == null) {
        return;  // Ember's Text API not available
    }

    MatthiesenLibImmersiveMessageBuilder greeting = MatthiesenLibImmersiveMessageBuilder.create()
        .scale(1.3f)
        .fadeInTicks(10);

    compat.sendMessage(player, "Welcome back, " + player.getName().getString() + "!", 100f, greeting);
}
```

### Error Notification

```java
public static void notifyError(ServerPlayer player, String errorMessage) {
    MatthiesenLibTextParser parser = MatthiesenLib.getTextParser(MatthiesenLibBuiltInTextParsers.EMBER);
    MatthiesenLibEmbersTextParserCompat compat = parser.getEmbersCompat();
    if (compat == null) {
        return;  // Ember's Text API not available
    }

    MatthiesenLibImmersiveMessageBuilder error = MatthiesenLibImmersiveMessageBuilder.create()
        .anchor(MatthiesenLibImmersiveMessageBuilder.TextAnchor.TOP_CENTER)
        .background(true)
        .scale(1.1f);

    compat.sendMessage(player, "✗ Error: " + errorMessage, 150f, error);
}
```

### Periodic Status Updates

```java
public static void sendBossHealthUpdate(ServerPlayer player, String bossName, int health, int maxHealth) {
    MatthiesenLibTextParser parser = MatthiesenLib.getTextParser(MatthiesenLibBuiltInTextParsers.EMBER);
    MatthiesenLibEmbersTextParserCompat compat = parser.getEmbersCompat();
    if (compat == null) {
        return;  // Ember's Text API not available
    }

    String progress = "[" + "█".repeat(health / 10) + "░".repeat((maxHealth - health) / 10) + "]";
    String messageId = "boss_health_" + bossName.toLowerCase();

    MatthiesenLibImmersiveMessageBuilder statusBuilder = MatthiesenLibImmersiveMessageBuilder.create()
        .anchor(MatthiesenLibImmersiveMessageBuilder.TextAnchor.MIDDLE)
        .scale(1.2f);

    compat.sendUpdateMessage(player, messageId, bossName + " " + progress, 80f, statusBuilder);
}
```

## FAQ

**Q: How do I get the compatibility layer?**  
A: Use `MatthiesenLib.getTextParser(MatthiesenLibBuiltInTextParsers.EMBER).getEmbersCompat()`. This returns `null` if Ember's Text API is not available.

**Q: Does my mod need to depend on Ember's Text API?**  
A: No. Your mod only needs to depend on Matthiesen Lib. Ember's Text API dependency is optional—if it's not installed, the compat layer will be `null`.

**Q: What happens if Ember isn't installed?**  
A: The parser is still available (it will parse text with the Vanilla markup syntax), but `getEmbersCompat()` will return `null`. Always check for null before calling send methods.

**Q: Can I use this on Fabric and NeoForge with the same code?**  
A: Yes! Matthiesen Lib handles all platform differences. Write once, deploy to both.

**Q: How do I format text in messages?**  
A: Use Minecraft's `Component` API for formatted text, or use Ember's markup syntax in string messages. See Ember's Text API documentation for markup details.

**Q: Are there more settings I can customize?**  
A: The builder covers the core essentials. For advanced Ember features (custom colors, borders, textures, etc.), consider enhancing the builder or using Ember's API directly if needed.

