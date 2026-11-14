# Legendary Survival Overhaul

![Legendary Survival Overhaul](https://cdn.modrinth.com/data/cached_images/68fe59ec2cf6de438b8c470e7afa579815c9d6c2.png)

**Make survival matter.** This Fabric 1.20.1 port of Legendary Survival Overhaul adds configurable environmental survival, hydration, injuries, health, and air quality.

[**Download v3.2.4**](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v3.2.4) | [**Player and developer wiki**](https://github.com/Atlasroar/LegendarySurvivalOverhaul/wiki) | [Report an issue](https://github.com/Atlasroar/LegendarySurvivalOverhaul/issues)

## Features

- **Temperature and wetness:** environmental heat/cold, insulated equipment, armor coats, heaters/chillers, and warning effects.
- **Hydration:** thirst and saturation, drinking, canteens, purification, consumable effects, and hydration tooltips.
- **Localized injuries:** body-part damage, secondary effects, healing supplies, and a limb-health screen (**H** by default).
- **Expanded health:** broken, shield, and additional hearts, with optional Overflowing Bars HUD spacing.
- **Air quality:** integrated Thin Air mechanics, dimension/height hazards, lava fumes, soul sources, Safety Lanterns, Air Bladders, Respirators, and Turtle Helmet protection. Water Breathing is displayed as **Free Breathing** in English.
- **Configuration:** **264 settings across nine configs**, including **LSO - Air** and **LSO - Trinkets**. File-based configuration works standalone; optional Fzzy Config adds in-game editing.
- **Biome compatibility:** native climates for modded biomes, datapack-only temperature overrides, and optional VanillaBackport sulfur-cave air. Equipped Respirators prevent new Nausea effects.
- **Accessory slot customization:** choose allowed slots for eight LSO accessories; the Respirator remains excluded.
- **Datapack customization:** tune consumables, equipment resistance, environment profiles, and body-damage rules.
- **Hydration mod support:** pure-datapack Thirst hydration for Farmer's Delight Refabricated, HerbalBrews, Vinery, Meadow, Farm & Charm, Bakery, Candlelight, and Brewery drinks (and related foods) — no dependency required, inactive unless the target mod is installed.

## Install

Use **Minecraft 1.20.1**, **Fabric Loader 0.15.0+**, **Java 17**, and **Fabric API**. Cardinal Components base/entity are bundled.

**Strongly recommended, not required (v3.0.0+):**

- **[Fzzy Config](https://modrinth.com/mod/fzzy-config) 0.7.7+1.20.1:** in-game configuration editing. Installing it also requires **Fabric Language Kotlin** to satisfy Fzzy Config's own dependencies.
- **[Trinkets](https://modrinth.com/mod/trinkets) 3.7.2+:** accessory slots, wearable accessory effects, and the Respirator's face-slot equip path.

LSO runs without all three libraries: edit its TOML files while the game/server is stopped. Without Trinkets, accessory slots and auto-equip are unavailable; supported held-item effects still work. v2.8.1 and earlier require all three.

**Mod Menu** and **Overflowing Bars** are optional. Forge Config API Port and a separate Thin Air installation are not required by current LSO releases.

Back up worlds and configs before updating, and replace the old LSO jar. See [installation/upgrading](https://github.com/Atlasroar/LegendarySurvivalOverhaul/wiki/Installation-and-Upgrading) and [configuration](https://github.com/Atlasroar/LegendarySurvivalOverhaul/wiki/Configuration) for migration and multiplayer settings.

## Learn more

The [wiki](https://github.com/Atlasroar/LegendarySurvivalOverhaul/wiki) contains player guides, compatibility limits, troubleshooting, detailed datapack recipes, architecture/API references, and credits. Broad multiplayer validation remains open; see the documented testing status rather than assuming every mod combination is supported.

Build from source with **JDK 17**: `.\gradlew.bat build` on Windows or `./gradlew build` on macOS/Linux. See the [developer guide](https://github.com/Atlasroar/LegendarySurvivalOverhaul/wiki/Development-and-Work-Plan).

## Optional storm integration

v3.1.0 adds optional [Atlasroar/Immersive-Weathering](https://github.com/Atlasroar/Immersive-Weathering) **2.5.0+** storm integration. Exposed snow/sand storms drive dangerous cold/heat at 2x speed with Cold Hunger/Heat Thirst, countered by Hot Drink/Cold Drink respectively. A complete centered 3x3 sky-blocking roof within 10 blocks above eye level provides shelter. Freezing-wind hits separately apply 10 seconds of Cold Hunger. See the [versioned compatibility guide](docs/wiki/Mod-Compatibility.md#immersive-weathering-storms-v310) for exact scope and the independent toggle.

## Performance optimizations

**v3.2.4 completes six performance stages:** non-wearer skating fast paths,
temperature scan housekeeping, scan-local air chunk reuse, built-in
equipment temperature caching, change-detected config serialization and
per-update wetness fluid reuse. Survival balance, mob air sensitivity,
provider coverage, cache timing and optional-mod fallbacks remain unchanged.
No new settings or dependencies are required. See the
[staged work and measured operation counts](docs/wiki/Development-and-Work-Plan.md#staged-performance-work-after-v323);
whole-server gains require a new Spark profile after updating.

**v3.2.3 air-scan optimization:** rejects out-of-range positions before
world lookups and classifies each distinct block state once per scan.
Provider radii, hazard precedence, breathing behavior and cache timing
are unchanged. See [air internals](docs/wiki/Air-System-Internals.md#cache-and-cost).

## Wet cooling and survival gear

**v3.2.2 compatibility fixes:** skating supports ice subclasses (including
Immersive Weathering's Thin Ice) and common/custom ice tags, composes with
other friction adjustments, and uses player-attributed blade damage.
Crimson Lily soaking respects wetness immunity. Scorchful/Frostiful detection
warns about independent overlapping thermal systems without disabling them.
See [compatibility guidance](docs/wiki/Mod-Compatibility.md#thermal-and-skating-coexistence-v322).

**New in v3.2.0:** configurable 1.5x wet cooling, standalone
Scorchful crimson/warped lilies, Frostiful ice skates, and non-armored fur
textures replacing winter clothing without changing its IDs or protection.
No Scorchful, Frostiful or Thermoo dependency is added. See the
[survival guide](docs/wiki/Survival-Systems.md#wetness-nether-lilies-fur-and-skates-v320)
and [wetness comparison](docs/wiki/Mod-Compatibility.md#wetness-comparison-and-content-ports-v320).

## Credits

Original LSO by **Sfiomn**; Fabric port maintained by **Atlasroar**. Integrated air mechanics derive from **Thin Air / Miner's Lung**; accessories use **Trinkets**. See [credits and licenses](https://github.com/Atlasroar/LegendarySurvivalOverhaul/wiki/Credits-and-Licenses) for upstream authors, dependencies, and separate asset permissions.

[Original Modrinth project](https://modrinth.com/mod/legendary-survival-overhaul) | [Community Discord](https://discord.gg/XPHtcP89P3)
