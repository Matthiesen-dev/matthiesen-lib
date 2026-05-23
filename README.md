# Matthiesen Lib

Matthiesen Lib has two main components published as separate mods on Modrinth:

- [`matthiesen-lib-api`](./api) - The Server-side API mod that provides the shared code and functionality for mod developers to use in their mods.
- [`matthiesen-lib`](./lib) - The full Server/Client mod that runs alongside the API mod and provides extended functionality, including client-side features and optional dependencies.

## Docs

Documentation for the library can be found at [mods.matthiesen.dev](https://mods.matthiesen.dev/matthiesen-lib/)

## Fresh Clone Setup

Loom requires API platform JARs to exist before it can configure the Fabric/NeoForge subprojects. On a fresh clone, run this first:

```bash
./gradlew :api-common:build :api-fabric:remapJar :api-neoforge:remapJar --configure-on-demand
```

Then trigger an IDE Gradle sync, or run a full build:

```bash
./gradlew build
```

## Version Compatibility

| Minecraft Version | Library Version |
|-------------------|-----------------|
| 1.21.1            | 1.x.x           |

## License

MIT - see `LICENSE`.
