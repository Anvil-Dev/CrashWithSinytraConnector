<div align="center">

# CrashWithSinytraConnector

**A NeoForge mod that declares incompatibility with Sinytra Connector.**

[简体中文](README.md) | English

</div>

## Purpose

This mod contains no gameplay content. Its sole purpose is to declare incompatibility with Sinytra Connector at NeoForge startup:

- At build time it reads [sinytra_reason.json](sinytra_reason.json) in the repository root and appends every Fabric API component listed there as an `incompatible` dependency to the generated `neoforge.mods.toml`
- When a player installs this mod alongside any mod that uses Sinytra Connector components, NeoForge reports an error during startup and refuses to load, preventing hard-to-diagnose runtime compatibility issues

## Usage

If your mod does not intend to support Sinytra Connector, you can embed this mod into yours via `jarJar` so it is distributed together with your mod.

Add the maven repository and dependency in your `build.gradle`:

```groovy
repositories {
    maven { url = 'https://server.cjsah.net:1002/maven/' }
}

dependencies {
    jarJar(implementation("dev.anvilcraft.crash:crash_sinytra-neoforge-26.1.1:1.0.0+snapshot.+"))
}
```

- Maven: <https://server.cjsah.net:1002/maven/dev/anvilcraft/crash/crash_sinytra-neoforge-26.1.1/>
- Recommended version: `1.0.0+snapshot.+`

Once embedded, this mod is bundled into your mod's build artifact. Players who install your mod together with Sinytra Connector will receive a clear incompatibility error during startup.

## License

This project is open source under the [MIT](LICENSE) license.
