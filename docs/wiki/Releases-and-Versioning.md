# Releases and Versioning

## Latest full release

[**v2.0.0**](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v2.0.0) targets Minecraft 1.20.1, Fabric, and Java 17.

```text
legendarysurvivaloverhaul-fabric-1.20.1-2.0.0.jar
SHA-256: BC65555303CA316995465449DB6FFE3F95D626572A8D818B06B130622BE1991F
```

**Breaking dependency/config change:** Fzzy Config and Fabric Language Kotlin replace Forge Config API Port. Legacy configs migrate automatically with backups. All 230 options have an in-game editor, Mod Menu support is available, and config titles use the short `LSO - ...` form.

## Release milestones

| Version | Milestone |
| --- | --- |
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
