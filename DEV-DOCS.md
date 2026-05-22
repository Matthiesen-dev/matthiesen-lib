# Dev Docs

Developer-focused notes and migration details for `matthiesen-lib`.

## 2026-05-22

### API Module Setup

- Added a dedicated `api` module and included it in `settings.gradle.kts`.
- `common` now depends on `api` via `api(project(":api"))`.
- `api` applies Loom and uses Mojang mappings so API classes can reference Minecraft code.

### Config Manager Extraction

- Core implementation moved to `dev.matthiesen.api.matthiesen_lib.config.ConfigManager`.
- `common` keeps a compatibility wrapper at `dev.matthiesen.common.matthiesen_lib.config.ConfigManager` (deprecated) to avoid breaking existing imports.
- API `ConfigManager` adds an overload allowing custom config namespace:
  - `ConfigManager(Class<T> configClass, String configName, String modId)`

### Dynamic Logger Binding

- New logger hub: `dev.matthiesen.api.matthiesen_lib.core.MatthiesenLibConstants`.
- Logger can be rebound at runtime:
  - `setLogger(Logger)`
  - `setLoggerName(String)`
- `common` constants delegate to API constants to preserve existing behavior and callsites.

### Permission Module Extraction

- Shared permission types now live in `dev.matthiesen.api.matthiesen_lib.permission`.
- Shared permission registry/validator logic now lives in `dev.matthiesen.api.matthiesen_lib.core` and `dev.matthiesen.api.matthiesen_lib.core.interfaces`.
- `common` keeps compatibility aliases/wrappers for the old permission packages so existing imports continue to work during migration.
- Platform validators (`fabric` and `neoforge`) now depend on the API permission interfaces directly.

### Command Registration Extraction

- Shared command registration types now live in:
  - `dev.matthiesen.api.matthiesen_lib.command.AbstractCommand`
  - `dev.matthiesen.api.matthiesen_lib.core.MatthiesenLibCommandsManager`
  - `dev.matthiesen.api.matthiesen_lib.core.interfaces.MatthiesenLibCommandRegistrar`
  - `dev.matthiesen.api.matthiesen_lib.core.platform.MatthiesenLibCommandPlatform`
  - `dev.matthiesen.api.matthiesen_lib.registry.AbstractCommandRegistry`
- `common` keeps compatibility wrappers/aliases for the old command package paths.
- Fabric and NeoForge command platform services now implement the API command-platform interface.
- Added new service loader files for API command platform discovery in Fabric and NeoForge resources.

### Platform Interface Extraction

- `MatthiesenLibPlatform` now lives in `dev.matthiesen.api.matthiesen_lib.core.platform.MatthiesenLibPlatform`.
- `common` keeps `dev.matthiesen.common.matthiesen_lib.core.platform.MatthiesenLibPlatform` as a deprecated compatibility alias.
- `MatthiesenLib` now loads the API platform interface via `ServiceLoader`.
- Fabric and NeoForge platform service classes now implement the API platform interface directly.
- Added API `META-INF/services` files for platform interface discovery in Fabric and NeoForge resources.

### TextParser Extraction

- Core TextParser system moved into API:
  - `dev.matthiesen.api.matthiesen_lib.core.MatthiesenLibTextParserManager`
  - `dev.matthiesen.api.matthiesen_lib.core.interfaces.MatthiesenLibTextParser`
  - `dev.matthiesen.api.matthiesen_lib.core.interfaces.MatthiesenLibBuiltInTextParsers`
  - `dev.matthiesen.api.matthiesen_lib.core.text_parser.MatthiesenLibVanillaTextParser`
- `common` keeps compatibility wrappers for legacy imports.
- Ember's Text API integration remains in `common` (`MatthiesenLibEmbersTextParserCompat`, `MatthiesenLibEmbersMessagingPlatform`, and related compat classes) so server-only API consumers do not depend on Ember's client-side logic.
- Common now exposes `MatthiesenLibExtendedTextParser` as the Ember-aware parser contract.
- Legacy `MatthiesenLibTextParser` remains as a deprecated alias to ease migration.
- `MatthiesenLib` now uses the API TextParser types directly.
- Added Embers compat accessors on `MatthiesenLib` for convenience:
  - `getEmbersTextParserCompat()`
  - `getEmbersTextParserCompat(MatthiesenLibBuiltInTextParsers type)`
  - `getEmbersTextParserCompat(String type)`

### Item Utility Extraction

- `ItemBuilder` now lives in `dev.matthiesen.api.matthiesen_lib.utility.ItemBuilder`.
- `ItemDecoder` now lives in `dev.matthiesen.api.matthiesen_lib.utility.ItemDecoder`.
- `common` keeps deprecated compatibility wrappers for the old utility package paths.

### Sound Utility Extraction

- `SoundsPlayer` now lives in `dev.matthiesen.api.matthiesen_lib.utility.SoundsPlayer`.
- `common` keeps a deprecated compatibility wrapper for the old utility package path.

### RunSlashCommand Split

- `RunSlashCommand` now lives in `dev.matthiesen.api.matthiesen_lib.utility.RunSlashCommand` for server-explicit command execution.
- The API version only exposes overloads that require a `MinecraftServer` parameter.
- `common` keeps `dev.matthiesen.common.matthiesen_lib.utility.RunSlashCommand` as a convenience wrapper that still supports resolving the server through `MatthiesenLib.getMinecraftServer()`.

