# Releases and Versioning

## Latest full release

[**v3.2.4**](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v3.2.4) targets Minecraft 1.20.1, Fabric, and Java 17.

```text
legendarysurvivaloverhaul-fabric-1.20.1-3.2.4.jar
SHA-256: ECA6EE774AEBEF1E65427C6140367C26A9E2197694C983293A2FE882326D0BDF

legendarysurvivaloverhaul-fabric-1.20.1-3.2.4-sources.jar
SHA-256: 4D8D662F9A11E09ADA2E9CA56FD83D7C5D8703F6F309318438A58B2CD0029225
```

v3.2.4 completes six performance stages: non-wearer skating fast paths,
temperature scan housekeeping, scan-local air chunk reuse, built-in
equipment temperature caching, change-detected config serialization and
per-update wetness fluid reuse.

The default empty-air fixture reduces chunk checks/fetches from 6,143 to 11,
while retaining all 3,071 spherical block candidates. Twenty unchanged
config polls rebuild zero JSON snapshots rather than 20. Partially
submerged wet updates read two fluid states instead of four, with the
existing block read unchanged.

Survival balance, mob air sensitivity, provider-specific radii and hazard
precedence, entity-cache timing, wetness immunity and mount behavior remain
unchanged. Equipment attribute reconciliation remains live, with uncached
fallbacks for custom modifiers/providers. There are no new settings,
required dependencies or save/config migrations.

These are focused operation counts, not a whole-server MSPT guarantee.
A new Spark capture after installation is needed to measure overall gains.
All v3.2.3 features remain included. See the
[staged performance work](Development-and-Work-Plan#staged-performance-work-after-v323).

## Previous releases

[**v3.2.3**](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v3.2.3) targets Minecraft 1.20.1, Fabric, and Java 17.

```text
legendarysurvivaloverhaul-fabric-1.20.1-3.2.3.jar
SHA-256: 0A04F6620EAB06BD741DD10AC3837E9BDFBEFC03E581F8417C0F006FB7B3456B
```

v3.2.3 optimizes nearby air-provider scans without changing provider radii,
hazard precedence, eye-fluid handling, breathing rules or entity-cache timing.
Out-of-range positions are rejected before world lookups; each distinct
block state is classified only once per scan. Local empty-air uncached
benchmarks were about 6-7.5x faster, not a whole-server MSPT guarantee.

The supplied Spark tree contains LSO scans in player and villager ticks,
but inconsistent parent/child timings prevent trustworthy server-time
percentages. A fresh profile using a Spark build containing
[lucko/spark#588](https://github.com/lucko/spark/pull/588) is needed to
quantify server-wide gains. See [Air System Internals](Air-System-Internals#cache-and-cost).

All v3.2.2 features remain included. No new settings, dependencies or
save/config migrations are required.

[**v3.2.2**](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v3.2.2) targets Minecraft 1.20.1, Fabric, and Java 17.

```text
legendarysurvivaloverhaul-fabric-1.20.1-3.2.2.jar
SHA-256: 2447B92FD0BDABE18152EBD349AF96E2E07CA1DEFD6BCED4D7C1F2CF863E54E7
```

v3.2.2 hardens skating and lily compatibility. Friction uses a composable
expression hook, player blade hits use player-attack attribution with
normal PvP/team/damage-cancellation handling, and wetting respects Sponge,
per-player deactivation, configured mount immunity and Creative/Spectator.
Skating and off-ice penalties share recognition of vanilla ice, `IceBlock`
subclasses (including IW Thin Ice), common ice tags and the extensible
`legendarysurvivaloverhaul:skateable_ice` block tag. Thin ice still cracks,
breaks and melts normally.

Scorchful/Frostiful detection logs overlap/configuration guidance without
automatically disabling or bridging their independent thermal systems.
No new required dependencies or settings are added; there remain **264
settings across nine configs**. Fzzy Config and Trinkets remain strongly
recommended, not required. All v3.2.0/v3.2.1 features remain included.

Assertions cover synthetic friction-redirect composition, vanilla PvP/team
rules, Fabric damage cancellation, held/equipped Sponge, actual IW Thin Ice
travel across all block states, and common-tag/custom-surface recognition.
Actual Scorchful/Frostiful coexistence and arbitrary third-party movement/
protection mods are not claimed as tested. See [Mod Compatibility](Mod-Compatibility#thermal-and-skating-coexistence-v322).

[**v3.2.1**](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v3.2.1) targets Minecraft 1.20.1, Fabric, and Java 17.

```text
legendarysurvivaloverhaul-fabric-1.20.1-3.2.1.jar
SHA-256: 1805CE46E8ED291B10FC6DB08F2E451A048A0A44C3ABD0FD65A398503E4167D9
```

v3.2.1 fixes Crimson/Warped Lily inclusion in the existing LSO Flora Field
Guide category using an additive plant tag. Existing lily lore supplies
notes; Field Guide remains optional. No gameplay balance or dependency
changes are introduced. All v3.2.0 features below remain included.

[**v3.2.0**](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v3.2.0) targets Minecraft 1.20.1, Fabric, and Java 17.

```text
legendarysurvivaloverhaul-fabric-1.20.1-3.2.0.jar
SHA-256: CE43D557E535F37FEBCA7FDEB2E6550D87E57E2B9B917C5E99ADA858373CB2A6
```

v3.2.0 adds configurable 1.5x cooling-only acceleration at any nonzero
wetness, in every biome. Existing saturation-scaled target cooling remains;
warming is unchanged. Set `wetCoolingRateMultiplier = 1` for the previous
speed. IW snowstorm cooling multiplies with wet cooling (3x by default).

Standalone Crimson/Warped Lilies generate in their Nether forests and
interact with LSO wetness, bottles and canteens. Ice Skates add gliding,
sprint acceleration, sneak braking and an off-ice walking penalty.
Frostiful's non-armored fur visuals replace winter clothing while retaining
`snow_*` IDs, durability, recipes and armor/cold protection. No Scorchful,
Frostiful or Thermoo dependency is added; imported/adapted portions retain
LGPL-3.0 notices and licenses.

There are **264 settings across nine configs**. Existing TOML files remain
usable and receive the new default. **Fzzy Config and Trinkets remain
strongly recommended, not required.** Config regressions, packaged-resource
checks, isolated server assertions and client model/resource loading pass.
Equipped visuals, skating feel and separate-client multiplayer remain
in-game follow-ups. See [Survival Systems](Survival-Systems#wetness-nether-lilies-fur-and-skates-v320)
and [Installation and Upgrading](Installation-and-Upgrading).

[**v3.1.0**](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v3.1.0) targets Minecraft 1.20.1, Fabric, and Java 17.

```text
legendarysurvivaloverhaul-fabric-1.20.1-3.1.0.jar
SHA-256: 4C80029DF40A0C28B8299005A8019648AC609F7C033F588A010E0D8FE5F1BD1E
```

v3.1.0 adds optional Atlasroar Immersive Weathering 2.5.0+ support: snow/sand storms force dangerous cold/heat targets at 2x rate and apply Cold Hunger/Heat Thirst, blocked by Hot Drink/Cold Drink respectively. Complete centered 3x3 sky-blocking cover within 10 blocks above the eye block provides shelter. Actual freezing-wind hits separately apply 10 seconds of Cold Hunger. The independent, default-enabled `immersiveWeatheringStormsEnabled` setting requires the temperature system and is inert without IW. Neither IW nor Moonlight is bundled or required.

There are **263 settings across nine configs**. Existing TOML files remain usable and acquire the new default. Fzzy Config and Trinkets remain strongly recommended, not required. Real dedicated-server assertions pass with IW 2.5.0/Moonlight installed and absent, covering targets/rates, shelter boundaries, drinks, wind impacts/expiry, overlapping effects, saves, and toggle/Creative cleanup. Storm client visuals and separate-client multiplayer remain unverified. See [Mod Compatibility](Mod-Compatibility#immersive-weathering-storms-v310) for exact boundaries.

[**v3.0.0**](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v3.0.0) targets Minecraft 1.20.1, Fabric, and Java 17.

```text
legendarysurvivaloverhaul-fabric-1.20.1-3.0.0.jar
SHA-256: 808F5BF57B48E06DFC1D9F2DAE514A6023F9744695B2F8B04B04E988F492C55B
```

v3.0.0 removes the explicitly dropped compatibility datapacks and Quark overrides while retaining Farmer's Delight Refabricated and Fabric BetterEnd support. Refreshing, Purity, and Reservoir are canteen-only loot enchantments, unavailable from enchanting tables and villager trades. Trinkets, Fzzy Config, and Fabric Language Kotlin are optional; standalone TOML configuration uses bundled NightConfig. Installing Fzzy still requires Kotlin for Fzzy itself. Without Trinkets, there are no accessory slots or new vanilla Respirator equip path. The major version follows the policy for changed dependency requirements and removed integrations. See [Installation and Upgrading](Installation-and-Upgrading) before updating.

Configuration regressions, all 262 optional editor bindings, packaged-resource checks, and dedicated-server assertions with the optional libraries absent and installed pass. Actual multiplayer packet flow, in-game editor interaction, and a real Farmer's Delight Refabricated installation still need in-game checks; the resource fixture uses a synthetic loader alias.

v2.8.1 fixes freezing and heat overlays washing out the screen white or yellow by explicitly enabling alpha blending in their shared renderer. Texture transparency and the existing fade levels are preserved. The user confirmed both fixes work in-game; v3.0.0 includes them.

v2.8.0 adds hydration datapack support for drinks (and a few related foods) from eight optional mods: Farmer's Delight Refabricated, HerbalBrews, Vinery, Meadow, Farm & Charm, Bakery, Candlelight, and Brewery. Runtime resource paths are `data/<mod id>/legendarysurvivaloverhaul/thirst/consumables/` and `data/<mod id>/legendarysurvivaloverhaul/thirst/blocks/`; there is no compile-time dependency on any of the eight mods, and entries are inert unless LSO detects the matching mod is loaded. Bakery has no dedicated beverages, so it only receives a small hydration amount on its Pudding Slice.

v2.7.0 adds explicit WandererZ compatibility: LSO's existing LevelZ death-respawn max-health timing fix already covered WandererZ's level-based max-health bonus generically (no mod-ID gating), and a new defensive datapack tag excludes all of LSO's drink items from WandererZ's bundled SpoiledZ food-spoilage system. v2.7.0 also replaces the repo's stock, unedited Forge MDK LICENSE.txt/CREDITS.txt template with a project-specific LGPL-2.1 license and accurate credits (Sfiomn, Atlasroar, Miner's Lung/MIT, Fuzs, Thinner Air/MPL-2.0 inspiration-only), and modernizes the GitHub issue templates to state 1.20.1-Fabric-only support and add a screenshots/video section.

v2.6.4 fixes the Left Shift hijack for players upgrading from pre-2.6.3 builds: v2.6.3 unbound the "added_desc" tooltip keybind's default, but Minecraft persists bound keys by name in options.txt, so already-saved installs kept the old Left Shift binding regardless of the new default. v2.6.4 adds a one-time client-side migration that detects a saved added_desc binding still on Left Shift, clears it, and resaves options.txt, so vanilla sneak and other keybinds sharing Shift work again without the player needing to manually rebind in Controls. v2.6.3 fixed the "added_desc" tooltip keybind (Thermometer/Coat item descriptions) hijacking Left Shift: Minecraft's KeyMapping system routes key events to only one binding per physical key, so defaulting this keybind to Left Shift silently blocked vanilla sneak and any other mod/custom keybind sharing that key.

v2.6.2 adds "Uses:" crafting-purpose text to the flora item descriptions (Sun Fern, Ice Fern, Water Plant seeds/leaves/crops), surfaced via Item Descriptions and Field Guide. v2.6.1 sepia-toned the Field Guide "LSO Flora" tab icon from the real Sun Fern Leaf sprite. v2.6.0 added Field Guide "LSO Flora" datapack category plus Item Descriptions/Mod Descriptions compatibility lore. v2.5.0 added HardcoreLite-inspired Enchanted Golden Apple shield-health/heart-container-repair and death heart-loss mechanics, with a non-spectator floor and LevelZ precedence. v2.4.0 added LevelZ/LibZ health compatibility. v2.3.0 enabled datapack-only biome overrides while preserving native climates, added optional VanillaBackport sulfur-cave air, and made equipped Respirators prevent new Nausea effects from any source. **LSO - Air** has 27 controls; v3.0.0 has **262 settings across nine configs**. Backport/Platform remain optional.

The user confirmed in-game: the LevelZ health bonus remains stable after death, and the Enchanted Golden Apple/death heart-loss mechanics work as intended with and without LevelZ installed. Revised Respirator Nausea protection (from v2.3.0) still needs an in-game retest; broader multiplayer/dedicated-server validation remains open.

## Release milestones

| Version | Milestone |
| --- | --- |
| v3.2.4 | Six behavior-preserving optimization stages covering skating, temperature, air chunks, equipment, config serialization and wetness lookups. |
| v3.2.3 | Reduce air-scan world lookups and repeated block-state classification without changing breathing rules, provider precedence or cache timing. |
| v3.2.2 | Composable skating friction, player-attributed blade damage, lily wetness immunities, modded/thin-ice surfaces and independent thermal-system coexistence guidance. |
| v3.2.1 | Add Crimson/Warped Lilies to the existing LSO Flora Field Guide category using the optional integration's plant tag. |
| v3.2.0 | Configurable 1.5x wet cooling; standalone Crimson/Warped Lilies; Ice Skates; non-armored fur visuals preserving winter IDs/protection; no new mod dependencies. |
| v3.1.0 | Optional Atlasroar IW storm/wind integration, 2x dangerous temperature steps, matching drink protection, 3x3 shelter, and 10-second freezing-wind Cold Hunger. |
| v3.0.0 | Removed explicitly dropped compatibility data; retained Refabricated/BetterEnd; canteen-only loot enchantments; optional Trinkets, Fzzy Config, and Kotlin with standalone TOML configuration. |
| v2.8.1 | Fixed opaque white/yellow freezing and heat overlays by enabling alpha blending. |
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
