# Releases and Versioning

## Latest full release

[**v2.6.2**](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v2.6.2) targets Minecraft 1.20.1, Fabric, and Java 17.

```text
legendarysurvivaloverhaul-fabric-1.20.1-2.6.2.jar
SHA-256: 11240C318BB64210FBB750531FD87C1FFC2AC28538539B64735123DC66FAF17E
```

v2.6.2 adds "Uses:" crafting-purpose text to the flora item descriptions (Sun Fern, Ice Fern, Water Plant seeds/leaves/crops), surfaced via Item Descriptions and Field Guide. v2.6.1 sepia-toned the Field Guide "LSO Flora" tab icon from the real Sun Fern Leaf sprite. v2.6.0 added Field Guide "LSO Flora" datapack category plus Item Descriptions/Mod Descriptions compatibility lore. v2.5.0 added HardcoreLite-inspired Enchanted Golden Apple shield-health/heart-container-repair and death heart-loss mechanics, with a non-spectator floor and LevelZ precedence. v2.4.0 added LevelZ/LibZ health compatibility. v2.3.0 enabled datapack-only biome overrides while preserving native climates, added optional VanillaBackport sulfur-cave air, and made equipped Respirators prevent new Nausea effects from any source. **LSO - Air** has 27 controls; there are **259+ settings across nine configs**. Backport/Platform remain optional.

The user confirmed in-game: the LevelZ health bonus remains stable after death, and the Enchanted Golden Apple/death heart-loss mechanics work as intended with and without LevelZ installed. Revised Respirator Nausea protection (from v2.3.0) still needs an in-game retest; broader multiplayer/dedicated-server validation remains open.

## Release milestones

| Version | Milestone |
| --- | --- |
| v2.6.2 | "Uses:" crafting-purpose text added to flora item descriptions (Item Descriptions/Field Guide). |
| v2.6.1 | Field Guide "LSO Flora" tab icon sepia-toned from the real Sun Fern Leaf sprite, replacing a placeholder design. |
| v2.6.0 | Field Guide "LSO Flora" datapack category; Item Descriptions and Mod Descriptions compatibility lore. |
| v2.5.0 | HardcoreLite-inspired Enchanted Golden Apple bonus and death heart-loss (config-driven, non-spectator floor). |
| v2.4.0 | LevelZ/LibZ health compatibility; death-respawn heal deferred so LevelZ's max-health bonus is preserved. |
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
