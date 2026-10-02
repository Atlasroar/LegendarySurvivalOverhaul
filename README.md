# Legendary Survival Overhaul

![Legendary Survival Overhaul](https://cdn.modrinth.com/data/cached_images/68fe59ec2cf6de438b8c470e7afa579815c9d6c2.png)

**Make survival matter.** Legendary Survival Overhaul (LSO) adds configurable temperature, hydration, localized body damage, and health systems to Minecraft.

[Modrinth project](https://modrinth.com/mod/legendary-survival-overhaul) · [Fabric releases](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases) · [Port and release notes](docs/Fabric-Port-Wiki.md) · [Community Discord](https://discord.gg/XPHtcP89P3) · [Guide](https://minecraft-legendary-edition.gitbook.io/minecraft-legendary-edition)

> **Fabric port status:** v2.0.0 is a full release for Fabric 1.20.1. The tracked gameplay port and air-quality fixes are complete, with single-player gameplay tested in-game. Broader multiplayer/dedicated-server validation remains open. Back up worlds and configs before updating.

## Screenshots

### Temperature and environmental survival

![Temperature system](https://cdn.modrinth.com/data/cached_images/b052d40ca61b2747c9003f4b508527ddd1db1df8.png)

Temperature responds to environmental factors. Monitor it to avoid dangerous cold or heat, and use insulated armor, plants, and temperature-control blocks to adapt.

![Temperature effects](https://cdn.modrinth.com/data/cached_images/b93e834e677bcb6491bb2762137bd9b5ce20bdea.png)

### Hydration

![Hydration bar](https://cdn.modrinth.com/data/cached_images/f0e9472f7b82073a5a17ff3ef99bcb5ec92ee7d9.png)

Hydration and saturation add a survival resource alongside hunger. Drink from water, purify water, use canteens, and account for heat-related thirst. The Fabric release includes the hydration HUD.

![Canteen](https://cdn.modrinth.com/data/cached_images/1a26798d91db5a24755f6612f1c30b54785f94bc.png)

### Localized body damage

![Localized limb damage](https://cdn.modrinth.com/data/cached_images/a713a1b8637a4236bed84dbae7e9bfb8ba539ebf.png)

Damage can affect specific body parts and cause secondary effects. Treat injuries with appropriate healing items. Open the limb-health screen with **H** by default (rebindable in Controls).

![Limb damage effects](https://cdn.modrinth.com/data/cached_images/7f33279aa5296d405b3f31f84dc3b4418cd8f5a3.png)

### Health overhaul

![Broken hearts](https://cdn.modrinth.com/data/cached_images/1d2c85437c1d9fa0e42882bbfb838e0469c7ad65.png)

The health systems include additional, broken, resilient, permanent, and shield hearts. The Fabric HUD rewrite uses the Overflowing Bars-based health renderer; the user confirmed vanilla health layers and effects at 40 health, and verified that broken hearts replace their containers while shield hearts use separate alternating yellow/orange rows that move armor upward.

## Current Fabric release

The latest release is [**v2.0.0**](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v2.0.0), targeting **Minecraft 1.20.1**, **Fabric**, and **Java 17**. It replaces Forge Config API Port with Fzzy Config and Fabric Language Kotlin, with in-game config editing and Mod Menu support. Starting with 1.0.0, the Fabric port is versioned as its own project using [Semantic Versioning](https://semver.org/); see the [versioning policy](docs/Fabric-Port-Wiki.md#versioning-policy). Older `v1.20.1-2.4.7-fabric.N` builds remain on the releases page for history.

Current highlights:

- Fzzy Config in-game editing for all 230 options across seven configs, with short titles such as **LSO - Client**, server synchronization, and optional Mod Menu integration.
- Automatic migration of 1.x configuration files, retaining `.forge-backup` copies.
- Integrated air-quality mechanics, survival equipment, and **Free Breathing** effects; no separate Thin Air installation is required.
- Temperature, thirst, wetness, and body-damage HUD indicators.
- LSO shield/broken-heart health HUD layers, including broken hearts replacing health containers and shield rows separated from the armor bar.
- Cold-hunger food overlay and thirst-row placement, visually confirmed.
- Core survival items, including wearable armor and heater/chiller behavior, validated in-game. The sewing table, warm/cold string recipes, and coat application to armor are also validated in-game, along with heater multiblock drop behavior and fuel persistence across save/reload.
- Configured thirst exhaustion from jumping, mining, and attacking, validated in-game.
- Consumable hydration, temperature effects, localized body damage, and healing items are validated in-game.
- Configured temperature immunity after death is validated for its 90-second default duration.
- Absorption-to-shield conversion, shield-first damage processing, and Water Purifier effect blocking are restored. Shield/broken-heart HUD presentation and armor spacing were verified in-game.
- Vulnerability and Hard Falling damage adjustments have been verified in-game.
- Configured player-health and body-part recovery after sleeping has been verified in-game.
- The configurable F3 coordinate/debug-information filter is restored on Fabric; the user confirmed F3 debug values are hidden when enabled.
- Low-hydration vision blur is restored using the vanilla post-processing effect and has been verified in-game.
- Equipment item-data modifiers for temperature resistance and localized body-part resistance are restored for vanilla equipment and LSO Trinkets; the user confirmed they work as expected in-game.
- Heat-stroke and frostbite warning overlays/sounds and Heat Stroke/heat-driven thirst effects are restored and verified in-game at forced thresholds.
- Optional Serene Seasons season cards are restored on Fabric; the user verified temperate Spring/Summer/Autumn/Winter cards and tropical Wet/Dry cards in-game.
- Item tooltips are user-verified for hydration and consumable effects, including Rotten Flesh and Refreshing enchantment levels I-III, and for temperature modifiers with appropriate colors and values on Snow and Desert armor.
- Serene Seasons' out-of-season bonemeal warning is restored and verified in-game with seasonal crops enabled and the "can't grow" behavior selected.
- Origins compatibility is intentionally omitted from the Fabric port.
- Meds and Herbs compatibility is intentionally unsupported because the mod is Forge-only.
- Wearable survival items use Trinkets slots; Trinkets 3.7.2 or later for Minecraft 1.20.1 is required.
- Datapack-driven survival data loading and synchronization.
- Optional shared HUD spacing with Overflowing Bars, including shield/armor row separation, broken-heart rendering, and vehicle-row gap correction, verified in-game.
- The final Forge event-subscriber audit found no remaining behavioral gaps; all excluded Forge-only handlers have Fabric equivalents, are intentional feature drops, or are justified platform adaptations.

The tracked Fabric port is complete. Broader multiplayer/dedicated-server gameplay testing remains open; dedicated-server config loading and legacy migration have been exercised. Forge-only data generators and selected optional integrations (Origins, Meds and Herbs) remain intentionally omitted. See the [port wiki](docs/Fabric-Port-Wiki.md) for migration history, validation limits, dependencies, and release notes.

Cold Hunger is a temperature-managed secondary effect: LSO applies it during dangerous cold and clears it when the player is no longer in that condition, so manually granting it with `/effect` outside dangerous cold will not keep it active for the requested duration.

### Air quality (Thin Air integration)

The air-quality system introduced in v1.0.0 remains included in v2.0.0: height- and dimension-based air (the Nether drains slowly, the End drains like water), RED air within 3 blocks of lava, GREEN air near portals, BLUE air near soul fire/torches/campfires/lanterns, Safety Lanterns, Signal Torches, Air Bladders, Soulfire Bottles, a Trinkets Respirator, and Turtle Helmet protection. Nearby hazards take precedence over safer providers; portal proximity no longer masks Nether or lava hazards. The vanilla Water Breathing effect, potions, and tipped arrows are renamed to **Free Breathing** in English, since they also protect against bad air. The air system and equipment were tested across the Overworld, Nether, and End. See the [air-quality integration notes](docs/Fabric-Port-Wiki.md#29-thin-air-air-quality-integration).

## Installation

Install the latest Fabric release from the [GitHub releases page](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases), along with:

- Minecraft **1.20.1**
- Fabric Loader
- Fabric API
- [Fzzy Config](https://modrinth.com/mod/fzzy-config) **0.7.7+1.20.1** or newer (required as of 2.0.0)
- [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) (required by Fzzy Config)
- Trinkets **3.7.2+** for Minecraft 1.20.1
- Cardinal Components API (base and entity; included in the published mod jar)

Replace the old LSO jar rather than keeping multiple versions installed. Forge Config API Port is no longer needed by LSO as of 2.0.0; keep it only if another mod requires it. [Mod Menu](https://modrinth.com/mod/modmenu) is optional but recommended. Overflowing Bars is optional, and the mod remains playable without it. Use dependency builds compatible with Minecraft 1.20.1.

## Configuration

As of 2.0.0, all settings use [Fzzy Config](https://github.com/fzzyhmstrs/fconfig). Every option in the seven config files can be edited in-game: Common, Temperature, Seasons, Thirst, Health, Body Damage, and Client.

- Open the editor from the **Mod Menu** config button, or run `/configure legendarysurvivaloverhaul`.
- Server-side settings are synced to clients, and operators can change them in-game.
- The files are stored in `config/legendarysurvivaloverhaul/<name>.toml`.
- Configs from 1.x (Forge Config API Port format) are migrated automatically on first launch. The original file is kept as `<name>.toml.forge-backup`.
- Client settings stay local; the other six configs use the server's synchronized values.
- In-game edits refresh the settings used by gameplay and HUD code. Settings read only during startup still require a restart.

## Mod compatibility

| Mod | Current support |
| --- | --- |
| Mod Menu | Optional config button, icon, project links, and wiki link. |
| Overflowing Bars | Optional shared HUD spacing; shield, broken-heart, armor, and vehicle-row fixes tested in-game. |
| Serene Seasons | Optional season cards and out-of-season bonemeal warnings tested in-game. |
| Supplementaries | Standard item finish-use path is handled by the existing consumable hook; no dedicated adapter is required for that path. |
| Origins | Intentionally unsupported in this port. |
| Meds and Herbs | Intentionally unsupported; the original integration targets Forge. |

Further mod integrations are the next development phase. This does not imply support for unlisted mods; each integration will need its own implementation and validation.

## Build from source

Requirements: JDK 17. The repository uses Gradle 8.12 and Fabric Loom 1.10.5.

Windows PowerShell:

```powershell
.\gradlew.bat build
```

macOS/Linux:

```sh
./gradlew build
```

The mod jar is written to `build/libs` as `legendarysurvivaloverhaul-fabric-<minecraft version>-<mod version>.jar`. To launch a development client, run `.\gradlew.bat runClient` on Windows or `./gradlew runClient` on macOS/Linux.

## Datapack customization

Temperature, hydration, air-quality dimension profiles, equipment resistance, and body-damage behavior use datapack-driven data alongside the in-game settings. See the included default resources and [port notes](docs/Fabric-Port-Wiki.md) for supported data and reload/synchronization behavior. The Fabric build packages checked-in generated resources; Forge datagen execution is not part of the supported build workflow.

## Testing and feedback

Please report the exact Minecraft version, loader/dependency versions, relevant log or crash report, and steps to reproduce. For visual HUD reports, include a screenshot and note which optional HUD mods are installed. Back up worlds before testing prereleases.

## Credits and links

- [Modrinth project](https://modrinth.com/mod/legendary-survival-overhaul) — feature overview and original screenshots.
- [Community Discord](https://discord.gg/XPHtcP89P3)
- [Minecraft Legendary Edition guide](https://minecraft-legendary-edition.gitbook.io/minecraft-legendary-edition)
- [YouTube](https://www.youtube.com/@MinecraftLegendaryEdition/featured)
- [Ko-fi](https://ko-fi.com/legendaryworkshop)
