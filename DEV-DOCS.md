# Dev Docs

Developer-focused notes and migration details for `matthiesen-lib` and `matthiesen-lib-api`.

## 2026-07-04

### Player Event Handler Additions (new)

The shared player-event system in API common now includes two interaction callbacks in addition to join/leave.

#### Common interface update

- `dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibPlayerEventHandler` adds:
  - `onPlayerUseItemResult(ServerPlayer player, Level level, InteractionHand hand)`
  - `onPlayerUseBlockResult(ServerPlayer player, Level level, InteractionHand hand, BlockPos pos)`
- These are the only interaction callbacks to implement for right-click behavior.
- Return `InteractionResult.PASS` to continue default behavior, or return a non-`PASS` result to consume/cancel.

#### Common manager dispatch update

- `dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiPlayerEventsManager` now dispatches:
  - `onPlayerUseItem(...)`
  - `onPlayerUseBlock(...)`
- These methods now return an aggregated `InteractionResult` from registered handlers.
- Dispatch behavior matches existing join/leave handling:
  - iterates all registered handlers,
  - isolates per-handler failures with `try/catch`,
  - reports exceptions via `MatthiesenLibApiMetricsManager.ERROR_TRACKER`,
  - logs the failing handler class,
  - prioritizes `FAIL`, otherwise returns the first non-`PASS` result.

#### Platform bridge wiring

- Fabric (`api/fabric/.../MatthiesenLibApiFabric`):
  - `UseItemCallback.EVENT` now forwards to `MatthiesenLibApiPlayerEventsManager.onPlayerUseItem(...)`.
  - `UseBlockCallback.EVENT` now forwards to `MatthiesenLibApiPlayerEventsManager.onPlayerUseBlock(...)`.
  - Forwarding is server-side only (`ServerPlayer` guard), and now returns the manager result so handlers can allow/consume/fail.

- NeoForge (`api/neoforge/.../MatthiesenLibApiNeoForgeServerBusEvents`):
  - `PlayerInteractEvent.RightClickItem` now forwards to `MatthiesenLibApiPlayerEventsManager.onPlayerUseItem(...)`.
  - `PlayerInteractEvent.RightClickBlock` now forwards to `MatthiesenLibApiPlayerEventsManager.onPlayerUseBlock(...)`.
  - Forwarding is server-side only and requires a `ServerPlayer` instance.
  - Non-`PASS` manager results set `event.setCancellationResult(...)` and cancel the NeoForge event.

## 2026-06-01

### Metrics System (new)

The API now ships a full metrics integration built on the [FastStats](https://github.com/faststats-dev/faststats-java) library (shadowed and relocated to `dev.matthiesen.libs.faststats`).

#### Core manager — `MatthiesenLibApiMetricsManager`

- New class at `dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibApiMetricsManager`.
- Non-instantiable static utility (private constructor).
- Owns the shared `UniversalMetricContext` instance, initialized at class-load time with the API mod ID and `METRICS_TOKEN`.
- Tracks a `REGISTERED_MODS` map (`modId → "name version"`) submitted as a `string_map` metric named `registered_mods` on each flush; the map is cleared after every flush.
- Public API:
  - `registerMod(String modId)` — looks up the mod container via `MatthiesenLibApi`, stores `"modName modVersion"` in the map. Logs a warning and no-ops on unknown mod IDs or duplicate registrations.
  - `getMetricContext()` — returns the shared `UniversalMetricContext`.
  - `ERROR_TRACKER` — public `static final ErrorTracker` instance (see below).

#### Error tracker — `MatthiesenLibApi.ERROR_TRACKER`

- `MatthiesenLibApi.ERROR_TRACKER` is a public constant forwarded from `MatthiesenLibApiMetricsManager.ERROR_TRACKER`.
- Configured via `ErrorTracker.contextUnaware()` with the following rules:
  - **Ignored exceptions**: `InvocationTargetException` matching `"Expected .* but got .*"`, `AccessDeniedException`.
  - **Anonymized patterns**: email addresses `→ [email hidden]`, `Bearer <token>` → `Bearer [token hidden]`, AWS access key IDs `→ [aws-key hidden]`, UUIDs `→ [uuid hidden]`, `api_key`/`token`/`secret` query-param values `→ [redacted]`.
- `ERROR_TRACKER.trackError(e)` is now called in every major catch block across the codebase:
  - `ConfigManager` — `createDefaultInstance()`, `save()`
  - `MatthiesenLibApiPlayerEventsManager` — player join / leave handlers
  - `MatthiesenLibApiServerEventsManager` — server start / tick / stop handlers
  - `RunSlashCommand.asServer()` / `asPlayer()`
  - `SoundsPlayer.play()`
  - Fabric and NeoForge reload runnable executors
  - NeoForge player join / leave bus event handlers

#### Metrics token

- `MatthiesenLibApiConstants.METRICS_TOKEN` — new `@Token`-annotated `static final String` holding the FastStats project token. Annotated with FastStats' `@Token` type annotation for compile-time token validation.

#### `MatthiesenLibModContainer` interface (new)

- New interface at `dev.matthiesen.common.matthiesen_lib_api.core.interfaces.MatthiesenLibModContainer`.
- Required methods: `getModName()`, `getModVersion()`, `getPlatform()`.
- Default method: `getModMetricId()` returns `"platform:normalized_mod_name"` (lowercase, spaces → underscores).
- Nested `Platform` enum with constants `FABRIC("fabric")` and `NEOFORGE("neoforge")`.

#### `MatthiesenLibPlatform` additions

Three new methods added to the `MatthiesenLibPlatform` interface:

| Method | Description |
|---|---|
| `MatthiesenLibModContainer getModContainer(String modId)` | Returns the mod container for the given mod ID, or `null` if not loaded. |
| `Path getModConfig(String dir, String file)` | Resolves `config/<dir>/<file>` relative to the game config directory. |
| `ENVIRONMENT getEnvironmentType()` | Returns `ENVIRONMENT.CLIENT` or `ENVIRONMENT.SERVER`. |

- New `ENVIRONMENT` enum on `MatthiesenLibPlatform`: `CLIENT`, `SERVER`.
- Fabric implementation delegates to `FabricLoader` (`getModContainer`, `getConfigDir`, `getEnvironmentType`).
- NeoForge implementation delegates to `ModList`, `FMLPaths.CONFIGDIR`, and `FMLEnvironment.dist`.

#### `MatthiesenLibApi` public surface additions

| Method / Field | Description |
|---|---|
| `public static final ErrorTracker ERROR_TRACKER` | Shared error tracker; forwarded from `MatthiesenLibApiMetricsManager`. |
| `getModContainer(String modId)` | Delegates to `PLATFORM.getModContainer(modId)`. |
| `getModConfig(String dir, String file)` | Delegates to `PLATFORM.getModConfig(dir, file)`. |
| `getEnvironmentType()` | Delegates to `PLATFORM.getEnvironmentType()`. |
| `registerModToMetrics(String modId)` | Delegates to `MatthiesenLibApiMetricsManager.registerMod(modId)`. |

- `modInitializer()` now calls `MatthiesenLibApiMetricsManager.getMetricContext().ready()` after all other managers are initialized.

#### `UniversalMetricContext` / `UniversalMetricsImpl` hierarchy (new)

All located under `dev.matthiesen.common.matthiesen_lib_api.core.metric`.

- **`UniversalMetricContext`** — extends FastStats `SimpleContext`. Created via a fluent `Factory(modId, token)` builder:
  ```java
  new UniversalMetricContext.Factory(modId, token)
      .metrics(factory -> factory.addMetric(...).onFlush(...).create())
      .errorTrackerService(errorTracker)
      .create();
  ```
  - Reads config from `config/faststats/config.properties` (via `MatthiesenLibApi.getModConfig`).
  - `getProjectName()` returns `mod.getModMetricId()`.
  - `metricsFactory()` branches on `MatthiesenLibApi.getEnvironmentType()`: returns `UniversalMetricsClientImpl` for `CLIENT`, `UniversalMetricsServerImpl` for `SERVER`.

- **`UniversalMetricsImpl`** (abstract) — extends FastStats `SimpleMetrics`. Base class holding the `MatthiesenLibModContainer modContainer` field and a shared `appendUniversalData(JsonObject)` helper. `preSubmissionStart()` delegates to `SimpleConfig`.

- **`UniversalMetricsClientImpl`** — client environment. `appendDefaultData` calls `appendUniversalData` only (client-side no-op submission).

- **`UniversalMetricsServerImpl`** — server environment. Registers a server event handler under key `"<MOD_ID>_metrics"` at construction time. Calls `startSubmitting()` on server start and `shutdown()` on server stop. `appendDefaultData` adds `minecraft_version`, `online_mode` (boolean), and `player_count` fields plus universal data.

#### Lib auto-registration

- `MatthiesenLib.modInitializer()` now calls `MatthiesenLibApi.registerModToMetrics(MatthiesenLibConstants.MOD_ID)`, so the lib module itself is automatically registered in the metrics `registered_mods` map.
- Downstream mods should call `MatthiesenLibApi.registerModToMetrics(myModId)` during their own `modInitializer` if they want to appear in collected metrics.

#### Gradle / build changes

- `libs.versions.toml` now declares FastStats dependencies as a `bundles.faststats` bundle.
- `api/common` and all platform modules depend on `libs.bundles.faststats` and include the bundle in `shadowBundle` for relocation.
- Shadow relocation: `dev.faststats` → `dev.matthiesen.libs.faststats` (all modules).
- The `matthiesen.api-shadow-platform-conventions` plugin now wraps the `remapJar` configuration in `afterEvaluate` to ensure Loom's default `inputFile` is overridden by the shadow jar. The `remapSourcesJar` override was removed — sources jars now use their own default source inputs.

## 2026-05-30

### Reload Runnable API (new)

- Added `dev.matthiesen.common.matthiesen_lib_api.core.MatthiesenLibReloadManager` to track per-mod reload callbacks.
- `MatthiesenLibApi` now initializes the reload manager in `modInitializer()`.
- New API methods on `MatthiesenLibApi`:
  - `registerReloadRunnable(String modId, Runnable runnable)`
  - `getReloadRunnables()`
- The lib facade (`dev.matthiesen.common.matthiesen_lib.MatthiesenLib`) now forwards these same methods for compatibility/convenience.

### Fabric Reload Hook

- `api/fabric/.../MatthiesenLibApiFabric` now listens to `ServerLifecycleEvents.END_DATA_PACK_RELOAD`.
- On successful reload, Fabric iterates `MatthiesenLibApi.getReloadRunnables()` and executes each callback.
- Each callback execution is wrapped in `try/catch` and logged via `MatthiesenLibApiConstants` (success path info logs + per-mod error logs).

### NeoForge Reload Hook

- `api/neoforge/.../MatthiesenLibApiNeoForgeServerBusEvents` now subscribes to `AddReloadListenerEvent`.
- Added helper listener `api/neoforge/.../helper/MatthiesenLibReloadListener` extending `SimplePreparableReloadListener<Void>`.
- During listener `apply(...)`, NeoForge iterates and executes the registered mod reload callbacks with the same per-mod logging/error handling approach.

### Behavior Notes

- Reload callbacks are keyed by `modId` and duplicate registrations are rejected with an error log.
- Current storage is an in-memory `Map<String, Runnable>` in API common, shared across platform integrations.

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
