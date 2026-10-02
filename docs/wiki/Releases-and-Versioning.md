# Releases and Versioning

## Latest full release

[**v2.3.0**](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v2.3.0) targets Minecraft 1.20.1, Fabric, and Java 17.

```text
legendarysurvivaloverhaul-fabric-1.20.1-2.3.0.jar
SHA-256: 9A4942EB243F73D3805A4D1B1FDFE72C0926A9CA630038CEEEC5263B89726685
```

v2.3.0 enables datapack-only biome overrides while preserving native climates, adds optional VanillaBackport sulfur-cave air, and makes equipped Respirators prevent new Nausea effects from any source. **LSO - Air** now has 27 controls; there are **259 settings across nine configs**. Dependencies remain unchanged. Backport/Platform are optional.

The user verified beta.1 biome/sulfur air behavior and reported mask failure. The full release replaces upstream-specific gas interception with vanilla effect interception and adds a descriptive tooltip. Revised protection passed isolated server assertions; in-game mask retest and broad multiplayer remain open.

## Release milestones

| Version | Milestone |
| --- | --- |
| v2.3.0 | Datapack biome compatibility, optional sulfur-cave YELLOW air, equipped-mask Nausea prevention and tooltip. |
| v2.3.0-beta.1 | Biome/sulfur testing prerelease; source-specific mask protection superseded by the broader full-release behavior. |
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
