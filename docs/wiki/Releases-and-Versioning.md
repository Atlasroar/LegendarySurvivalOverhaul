# Releases and Versioning

## Latest full release

[**v2.2.0**](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v2.2.0) targets Minecraft 1.20.1, Fabric, and Java 17.

```text
legendarysurvivaloverhaul-fabric-1.20.1-2.2.0.jar
SHA-256: 4DD625A3F28334532678E5B91544D6D7F9ED38F3AB8E87D4F6ECEB616B429AA8
```

v2.2.0 adds **LSO - Trinkets** with nine controls and per-item slot lists for eight accessories; Respirator is excluded. It retains **LSO - Air** with 25 controls. There are **257 options across nine configs**. Dependencies remain unchanged from v2.0.0: Fzzy Config and Fabric Language Kotlin replace Forge Config API Port; Mod Menu is optional.

## Release milestones

| Version | Milestone |
| --- | --- |
| v2.2.0 | Configurable accessory slot lists, override/tag-fallback switch, missing-slot warnings, preservation of equipped items. |
| v2.2.0-beta.1 | Trinkets configuration testing prerelease; superseded by v2.2.0. |
| v2.1.0 | Dedicated Air config, opt-in dimension/height overrides, breathing rates, equipment wear, bladder controls, and automatic Common migration. |
| v2.1.0-beta.1 | Air configuration testing prerelease; superseded by v2.1.0. |
| v2.0.0 | Full Fzzy Config migration, Mod Menu support, legacy backups, short config titles. |
| v2.0.0-beta.1 | Testing prerelease for config migration; superseded by v2.0.0. |
| v1.0.0 | First SemVer release; validated Thin Air mechanics, Turtle Helmet protection, and Free Breathing names. |
| Legacy Fabric .30 | Turtle Helmet breathing protection and quieter diagnostics. |
| Legacy Fabric .16-.29 | Thin Air integration and successive provider, dimension, oxygen, and HUD fixes. |
| Legacy Fabric .15 | First full Fabric port release after the Forge event audit. |
| Earlier legacy builds | Incremental port and in-game validation slices. |

The legacy naming scheme was `v1.20.1-2.4.7-fabric.N`. These releases remain historical; use the current release for a new installation.

## Semantic Versioning

The Fabric port follows [SemVer](https://semver.org/) independently from the original Forge release.

- **MAJOR:** incompatible changes, including breaking config/datapack/save formats or required-dependency changes.
- **MINOR:** backward-compatible new functionality.
- **PATCH:** backward-compatible bug fixes.
- Testing builds append a prerelease identifier, such as `2.0.0-beta.1`; full releases remove it.

Git tags use `v<mod_version>`. In-game metadata uses `<mod_version>+<minecraft_version>`; Minecraft is build metadata and does not change SemVer precedence. Download filenames use `legendarysurvivaloverhaul-fabric-<minecraft_version>-<mod_version>.jar`.

[All releases](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases) | [Detailed versioned port history](https://github.com/Atlasroar/LegendarySurvivalOverhaul/blob/lso-fabric-1-20-1/docs/Fabric-Port-Wiki.md)
