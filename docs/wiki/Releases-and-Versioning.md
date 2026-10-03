# Releases and Versioning

## Latest full release

[**v2.8.0**](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v2.8.0) targets Minecraft 1.20.1, Fabric, and Java 17.

```text
legendarysurvivaloverhaul-fabric-1.20.1-2.8.0.jar
SHA-256: BF009EF73F41F572A162BF9DDA0EC2785DD8E1904713407E5ED30E7978ABF46A
```

v2.8.0 adds hydration datapack support for drinks (and a few related foods) from eight optional mods: Farmer's Delight Refabricated, HerbalBrews, Vinery, Meadow, Farm & Charm, Bakery, Candlelight, and Brewery. Values are shipped as plain reloadable JSON under `data/<mod id>/thirst/consumables/` and `data/<mod id>/thirst/blocks/`, matching LSO's existing cross-mod Thirst data format; there is no compile-time dependency on any of the eight mods, and entries are inert unless LSO detects the matching mod is loaded. Bakery has no dedicated beverages, so it only receives a small hydration amount on its Pudding Slice. v2.7.0 adds explicit WandererZ compatibility: LSO's existing LevelZ death-respawn max-health timing fix already covered WandererZ's level-based max-health bonus generically (no mod-ID gating), and a new defensive datapack tag excludes all of LSO's drink items from WandererZ's bundled SpoiledZ food-spoilage system. v2.7.0 also replaces the repo's stock, unedited Forge MDK LICENSE.txt/CREDITS.txt template with a project-specific LGPL-2.1 license and accurate credits (Sfiomn, Atlasroar, Miner's Lung/MIT, Fuzs, Thinner Air/MPL-2.0 inspiration-only), and modernizes the GitHub issue templates to state 1.20.1-Fabric-only support and add a screenshots/video section. v2.6.4 fixes the Left Shift hijack for players upgrading from pre-2.6.3 builds: v2.6.3 unbound the "added_desc" tooltip keybind's default, but Minecraft persists bound keys by name in options.txt, so already-saved installs kept the old Left Shift binding regardless of the new default. v2.6.4 adds a one-time client-side migration that detects a saved added_desc binding still on Left Shift, clears it, and resaves options.txt, so vanilla sneak and other keybinds sharing Shift work again without the player needing to manually rebind in Controls. v2.6.3 fixed the "added_desc" tooltip keybind (Thermometer/Coat item descriptions) hijacking Left Shift: Minecraft's KeyMapping system routes key events to only one binding per physical key, so defaulting this keybind to Left Shift silently blocked vanilla sneak and any other mod/custom keybind sharing that key. v2.6.2 adds "Uses:" crafting-purpose text to the flora item descriptions (Sun Fern, Ice Fern, Water Plant seeds/leaves/crops), surfaced via Item Descriptions and Field Guide. v2.6.1 sepia-toned the Field Guide "LSO Flora" tab icon from the real Sun Fern Leaf sprite. v2.6.0 added Field Guide "LSO Flora" datapack category plus Item Descriptions/Mod Descriptions compatibility lore. v2.5.0 added HardcoreLite-inspired Enchanted Golden Apple shield-health/heart-container-repair and death heart-loss mechanics, with a non-spectator floor and LevelZ precedence. v2.4.0 added LevelZ/LibZ health compatibility. v2.3.0 enabled datapack-only biome overrides while preserving native climates, added optional VanillaBackport sulfur-cave air, and made equipped Respirators prevent new Nausea effects from any source. **LSO - Air** has 27 controls; there are **259+ settings across nine configs**. Backport/Platform remain optional.

The user confirmed in-game: the LevelZ health bonus remains stable after death, and the Enchanted Golden Apple/death heart-loss mechanics work as intended with and without LevelZ installed. Revised Respirator Nausea protection (from v2.3.0) still needs an in-game retest; broader multiplayer/dedicated-server validation remains open.

## Release milestones

| Version | Milestone |
| --- | --- |
| v2.8.0 | Hydration datapack support for Farmer's Delight Refabricated, HerbalBrews, Vinery, Meadow, Farm & Charm, Bakery, Candlelight, and Brewery drinks; no compile-time dependency, pure data. |
| v2.7.0 | WandererZ compatibility (LevelZ max-health timing already generic; SpoiledZ spoilage opt-out for LSO drinks); LGPL-2.1 LICENSE.txt/CREDITS.txt replacing the stock Forge MDK template; modernized issue templates. |
| v2.6.4 | One-time migration clears a saved added_desc keybind still on Left Shift, so the Shift-hijack fix actually applies to upgrading players, not just fresh installs. |
| v2.6.3 | Fixed "added_desc" tooltip keybind hijacking Left Shift (vanilla KeyMapping.MAP only routes events to one binding per key); now unbound by default. |
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
