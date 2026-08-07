# Matthiesen Lib

> ⚠️ This mod has been deprecated and is no longer going to receive updates.
> This library has been replaced by [Matthiesen Core](https://modrinth.com/mod/matthiesen-core) which provides a more robust and feature-rich foundation for mod developers to build upon.

Matthiesen Lib is a lightweight Architectury-style library that enables mod developers to write shared code that works seamlessly across both **Fabric** and **NeoForge** mod loaders for Minecraft **1.21.1**. Instead of maintaining separate implementations for each platform, you can write your mod logic once in common code and let Matthiesen Lib handle the platform-specific details.

> Note: For regular users, This library does nothing on its own - it's a developer tool. If a mod requires Matthiesen Lib, simply drop it in your `mods` folder alongside the mod that needs it.

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
