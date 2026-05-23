---
modrinth:
    server_side: 'required'
    client_side: 'required'
---
# Matthiesen Lib

A lightweight Architectury-style library for shared mod code across **Fabric** and **NeoForge** (Minecraft **1.21.1**).

> Note: For regular users, this library does nothing on its own. It's a tool for mod developers to write shared code that works on both Fabric and NeoForge without needing separate implementations.
> All you need to do is drop the mod JAR in your `mods` folder, and it will work as a dependency for any mods that use it.

## Docs

Documentation for the library can be found at [mods.matthiesen.dev](https://mods.matthiesen.dev/matthiesen-lib/)

## Dependencies

- [Matthiesen Lib API](https://modrinth.com/mod/matthiesen-lib-api) - The core API mod that provides the shared code and functionality for mod developers to use in their mods.

## Optional Dependencies
- [Ember's Text API](https://modrinth.com/mod/embers-text-api) - Use fancy text effects anywhere, including custom fonts!

## Version Compatibility

| Minecraft Version | Library Version |
|-------------------|-----------------|
| 1.21.1            | 1.x.x           |
