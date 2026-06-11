# CommandsAPI
I was reusing command code, so this is some centralized code that automates tabcompletion, command reflection, and unordered parameters.

## Platforms

- `common/` - platform-neutral command tree, parameters, and dispatch (`CommandsAPICommand`, `TreeCommand`, `CommandParameter`, and the typed `common/parameters/` set: boolean, integer, float, coordinate, enum).
- `bukkit/` - Bukkit/Spigot/Paper adapter (`CommandsAPIBukkit`, `BukkitCommand`, `BukkitParameter`).
- `brigadier/` - platform-neutral Brigadier bridge (`BrigadierCommandAdapter`, `BrigadierBridgeContext`). Walks a `CommandsAPICommand` tree and emits a Brigadier `LiteralArgumentBuilder<S>` that any Brigadier-based dispatcher can register. This is what lets Fabric and NeoForge adapters reuse the same command tree as Bukkit without leaking platform types into the common core. The bridge uses only vanilla Brigadier argument types (a single greedy `args` slot per literal level) so vanilla clients can join Fabric servers.

## Building

Gradle, Java 17. Brigadier (`com.mojang:brigadier`) is resolved from the Minecraft Libraries repo and is `provided` (compileOnly) scope - Bukkit-family runtimes do not load it, while Fabric/NeoForge supply it at runtime.

### JitPack

To use this project with JitPack, add the following to your `build.gradle`:

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.DailyStruggle:CommandsAPI:1.0.0-RELEASE'
}
```
