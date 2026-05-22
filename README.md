# Matthiesen Lib

A lightweight Architectury-style library for shared mod code across **Fabric** and **NeoForge** (Minecraft **1.21.1**).

> Note: For regular users, this library does nothing on its own. It's a tool for mod developers to write shared code that works on both Fabric and NeoForge without needing separate implementations.
> All you need to do is drop the mod JAR in your `mods` folder, and it will work as a dependency for any mods that use it.

## Docs

Documentation for the library can be found at [mods.matthiesen.dev](https://mods.matthiesen.dev/matthiesen-lib/)

## Artifacts

- `dev.matthiesen:matthiesen-lib-api:<version>` - Standalone API module, suitable for JiJ/shadow in server-side projects.
- `dev.matthiesen:matthiesen-lib-common:<version>` - Shared code used by the Fabric and NeoForge platform mods, depends on `-api`.
- `dev.matthiesen:matthiesen-lib-fabric:<version>` - Fabric-specific implementation, depends on `-common`.
- `dev.matthiesen:matthiesen-lib-neoforge:<version>` - NeoForge-specific implementation, depends on `-common`.

## Optional Dependencies
- [Ember's Text API](https://modrinth.com/mod/embers-text-api) - Use fancy text effects anywhere, including custom fonts!

## Version Compatibility

| Minecraft Version | Library Version |
|-------------------|-----------------|
| 1.21.1            | 1.x.x           |

## License

MIT - see `LICENSE`.
