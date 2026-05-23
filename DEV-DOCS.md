# Dev Docs

Developer-focused notes and migration details for `matthiesen-lib`.

## 2026-05-22

### API Module Setup

- The project is split into two mod families:
  - API mod: `api-common`, `api-fabric`, `api-neoforge`
  - Lib mod: `common` (lib common), `fabric`, `neoforge`
- `settings.gradle.kts` maps these to the `api/*` and `lib/*` directories.
- `lib/common` depends on `:api-common` using project-local Loom artifacts (`namedElements`) so migration wrappers can forward to API types.

### Config Manager Extraction

- Core implementation moved to `dev.matthiesen.common.matthiesen_lib_api.config.ConfigManager`.
- `common` keeps a compatibility wrapper at `dev.matthiesen.common.matthiesen_lib.config.ConfigManager` (deprecated) to avoid breaking existing imports.
- API `ConfigManager` adds an overload allowing custom config namespace:
  - `ConfigManager(Class<T> configClass, String configName, String modId)`

### Dynamic Logger Binding

- API logger hub: `dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiConstants`.
- Lib logger hub: `dev.matthiesen.common.matthiesen_lib.core.MatthiesenLibConstants`.
- Because API and lib are now separate mods, each keeps its own constants/logger identity (`MOD_ID`, `MOD_NAME`, logger instance).
- Both still support runtime logger rebinding:
  - `setLogger(Logger)`
  - `setLoggerName(String)`

### Permission Module Extraction

- Shared permission types now live in `dev.matthiesen.common.matthiesen_lib_api.permission`.
- Shared permission registry/validator logic now lives in `dev.matthiesen.common.matthiesen_lib_api.core` and `dev.matthiesen.common.matthiesen_lib_api.core.interfaces`.
- `common` keeps compatibility aliases/wrappers for the old permission packages so existing imports continue to work during migration.
- Platform validators (`fabric` and `neoforge`) now depend on the API permission interfaces directly.

### Command Registration Extraction

- Shared command registration types now live in:
  - `dev.matthiesen.common.matthiesen_lib_api.command.AbstractCommand`
  - `dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibCommandsManager`
  - `dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibCommandRegistrar`
  - `dev.matthiesen.common.matthiesen_lib_api.core.platform.MatthiesenLibCommandPlatform`
  - `dev.matthiesen.common.matthiesen_lib_api.registry.AbstractCommandRegistry`
- `common` keeps compatibility wrappers/aliases for the old command package paths.
- Fabric and NeoForge command platform services now implement the API command-platform interface.
- Added new service loader files for API command platform discovery in Fabric and NeoForge resources.

### Platform Interface Extraction

- `MatthiesenLibPlatform` now lives in `dev.matthiesen.common.matthiesen_lib_api.core.platform.MatthiesenLibPlatform`.
- `common` keeps `dev.matthiesen.common.matthiesen_lib.core.platform.MatthiesenLibPlatform` as a deprecated compatibility alias.
- `MatthiesenLib` now loads the API platform interface via `ServiceLoader`.
- Fabric and NeoForge platform service classes now implement the API platform interface directly.
- Added API `META-INF/services` files for platform interface discovery in Fabric and NeoForge resources.

### TextParser Extraction

- Core TextParser system moved into API:
  - `dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibTextParserManager`
  - `dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibTextParser`
  - `dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibBuiltInTextParsers`
  - `dev.matthiesen.common.matthiesen_lib_api.core.text_parser.MatthiesenLibVanillaTextParser`
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

- `ItemBuilder` now lives in `dev.matthiesen.common.matthiesen_lib_api.utility.ItemBuilder`.
- `ItemDecoder` now lives in `dev.matthiesen.common.matthiesen_lib_api.utility.ItemDecoder`.
- `common` keeps deprecated compatibility wrappers for the old utility package paths.

### Sound Utility Extraction

- `SoundsPlayer` now lives in `dev.matthiesen.common.matthiesen_lib_api.utility.SoundsPlayer`.
- `common` keeps a deprecated compatibility wrapper for the old utility package path.

### RunSlashCommand Split

- `RunSlashCommand` now lives in `dev.matthiesen.common.matthiesen_lib_api.utility.RunSlashCommand` for server-explicit command execution.
- The API version only exposes overloads that require a `MinecraftServer` parameter.
- `common` keeps `dev.matthiesen.common.matthiesen_lib.utility.RunSlashCommand` as a convenience wrapper that still supports resolving the server through `MatthiesenLib.getMinecraftServer()`.

### Loader Dependency Wiring (API as required mod)

- Lib platform metadata now declares API as a required runtime dependency:
  - `lib/fabric/src/main/resources/fabric.mod.json` depends on `${api_mod_id}`
  - `lib/neoforge/src/main/resources/META-INF/neoforge.mods.toml` has a mandatory dependency on `${api_mod_id}`
- Runtime classpath wiring in lib platform Gradle modules uses API platform mods as runtime dependencies:
  - `modRuntimeOnly(project(":api-fabric"))`
  - `modRuntimeOnly(project(":api-neoforge"))`
- These runtime API platform dependencies are configured as non-transitive in lib platform modules so Loom does not attempt to resolve `api-common-dev.jar` as a standalone runtime artifact during dev launch.
