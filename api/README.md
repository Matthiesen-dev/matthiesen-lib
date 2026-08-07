# Matthiesen Lib API

> ⚠️ This mod has been deprecated and is no longer going to receive updates.
> This library has been replaced by [Matthiesen Core](https://modrinth.com/mod/matthiesen-core) which provides a more robust and feature-rich foundation for mod developers to build upon.

Matthiesen Lib API is the server-side API for [Matthiesen Lib](https://modrinth.com/mod/matthiesen-lib), a lightweight Architectury-style library that enables 
mod developers to write shared code that works seamlessly across both **Fabric** and **NeoForge** mod loaders for Minecraft **1.21.1**. 
Instead of maintaining separate implementations for each platform, you can write your mod logic once in common code and let 
Matthiesen Lib handle the platform-specific details.

> Note: For regular users, This library does nothing on its own - it's a developer tool. If a mod requires Matthiesen Lib API, 
> simply drop it in your `mods` folder alongside the mod that needs it.

## Docs

Documentation for the library can be found at [mods.matthiesen.dev](https://mods.matthiesen.dev/matthiesen-lib-api/)

## Version Compatibility

| Minecraft Version | Library Version |
|-------------------|-----------------|
| 1.21.1            | 1.x.x           |

## FastStats Metrics

This library uses [FastStats](https://faststats.dev) to collect anonymous usage statistics. This helps the developer understand 
how the library and the mods built using the library are being used and improve it over time. You can learn more about the data 
collected and how it is used by visiting [FastStats: Information](https://faststats.dev/info).

You can also view the data collected by this library on the [FastStats: Matthiesen Lib API](https://faststats.dev/project/matthiesen-lib-api) page.

To opt out of this data collection, set the `enabled` property to `false` in the `<game_directory>/config/matthiesen_lib_api/metrics.properties` file.