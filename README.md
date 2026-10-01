# Legendary Survival Overhaul

![Legendary Survival Overhaul](https://cdn.modrinth.com/data/cached_images/68fe59ec2cf6de438b8c470e7afa579815c9d6c2.png)

**Make survival matter.** Legendary Survival Overhaul (LSO) adds configurable temperature, hydration, localized body damage, and health systems to Minecraft.

[Modrinth project](https://modrinth.com/mod/legendary-survival-overhaul) · [Fabric test releases](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases) · [Port and release notes](docs/Fabric-Port-Wiki.md) · [Community Discord](https://discord.gg/XPHtcP89P3) · [Guide](https://minecraft-legendary-edition.gitbook.io/minecraft-legendary-edition)

> **Fabric port status:** The current Fabric 1.20.1 build is an early prerelease, not feature-complete or feature-equivalent to the Forge version. It has been tested in a development client and visually tested by players; back up worlds before trying prereleases.

## Screenshots

### Temperature and environmental survival

![Temperature system](https://cdn.modrinth.com/data/cached_images/b052d40ca61b2747c9003f4b508527ddd1db1df8.png)

Temperature responds to environmental factors. Monitor it to avoid dangerous cold or heat, and use insulated armor, plants, and temperature-control blocks to adapt.

![Temperature effects](https://cdn.modrinth.com/data/cached_images/b93e834e677bcb6491bb2762137bd9b5ce20bdea.png)

### Hydration

![Hydration bar](https://cdn.modrinth.com/data/cached_images/f0e9472f7b82073a5a17ff3ef99bcb5ec92ee7d9.png)

Hydration and saturation add a survival resource alongside hunger. Drink from water, purify water, use canteens, and account for heat-related thirst. The Fabric test build includes the hydration HUD.

![Canteen](https://cdn.modrinth.com/data/cached_images/1a26798d91db5a24755f6612f1c30b54785f94bc.png)

### Localized body damage

![Localized limb damage](https://cdn.modrinth.com/data/cached_images/a713a1b8637a4236bed84dbae7e9bfb8ba539ebf.png)

Damage can affect specific body parts and cause secondary effects. Treat injuries with appropriate healing items. The limb-health screen is opened with **H** where supported by the current build.

![Limb damage effects](https://cdn.modrinth.com/data/cached_images/7f33279aa5296d405b3f31f84dc3b4418cd8f5a3.png)

### Health overhaul

![Broken hearts](https://cdn.modrinth.com/data/cached_images/1d2c85437c1d9fa0e42882bbfb838e0469c7ad65.png)

The health systems include additional, broken, resilient, permanent, and shield hearts. The current Fabric prerelease includes the shield/broken-heart HUD overlay; the full vanilla health-bar replacement is still being ported.

## Current Fabric release

The latest prerelease is [**v1.20.1-2.4.7-fabric.8**](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v1.20.1-2.4.7-fabric.8), targeting **Minecraft 1.20.1**, **Fabric**, and **Java 17**.

Current highlights:

- Temperature, thirst, wetness, and body-damage HUD indicators.
- LSO shield/broken-heart indicators, visually adjusted to clear the armor row.
- Cold-hunger food overlay and thirst-row placement, visually confirmed.
- Core survival items, including wearable armor and heater/chiller behavior.
- Configured thirst exhaustion from jumping, mining, and attacking, validated in-game.
- Consumable hydration, localized body damage, and healing items are validated in-game; other consumable effects remain under test.
- Configured temperature immunity after death is validated for its 90-second default duration.
- Absorption-to-shield conversion and Water Purifier effect blocking have passed initial in-game verification; shield depletion and the HUD presentation remain under further testing.
- Vulnerability and Hard Falling damage adjustments have been verified in-game.
- Configured player-health and body-part recovery after sleeping has been verified in-game.
- The configurable F3 coordinate/debug-information filter is restored on Fabric; the user confirmed F3 debug values are hidden when enabled.
- Low-hydration vision blur is restored using the vanilla post-processing effect and has been verified in-game.
- Equipment item-data modifiers for temperature resistance and localized body-part resistance are restored for vanilla equipment and LSO Trinkets; the user confirmed they work as expected in-game.
- Heat Stroke and heat-related thirst behavior are under investigation after the user reported they do not trigger as expected; the full-screen heat/cold overlay remains unported.
- Wearable survival items use Trinkets slots; Trinkets 3.7.2 or later for Minecraft 1.20.1 is required.
- Datapack-driven survival data loading and synchronization.
- Optional shared HUD spacing with Overflowing Bars.

The Fabric port is ongoing. The complete health-bar replacement, some Forge event behavior, data generators, and selected optional integrations are not yet included. See the [port wiki](docs/Fabric-Port-Wiki.md) for the migration history, known gaps, dependencies, and release-by-release notes.

Cold Hunger is a temperature-managed secondary effect: LSO applies it during dangerous cold and clears it when the player is no longer in that condition, so manually granting it with `/effect` outside dangerous cold will not keep it active for the requested duration.

## Installation

Install the latest Fabric prerelease from the [GitHub releases page](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases), along with:

- Minecraft **1.20.1**
- Fabric Loader
- Fabric API
- Forge Config API Port **8.0.3** for Minecraft 1.20.1
- Trinkets **3.7.2+** for Minecraft 1.20.1
- Cardinal Components API (base and entity; included in the published mod jar)

Overflowing Bars is optional. The mod remains playable without it.

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

The mod jar is written to `build/libs`. To launch a development client, run `.\gradlew.bat runClient` on Windows or `./gradlew runClient` on macOS/Linux.

## Configuration and customization

Configuration files are under `config/legendarysurvivaloverhaul`. Temperature, hydration, and body-damage behavior is substantially data-driven; see the included default data and the upstream [Modrinth description](https://modrinth.com/mod/legendary-survival-overhaul) for feature concepts. The Fabric port is restoring functionality incrementally, so check the [current port notes](docs/Fabric-Port-Wiki.md) before relying on a Forge-specific feature or datapack workflow.

## Testing and feedback

Please report the exact Minecraft version, loader/dependency versions, relevant log or crash report, and steps to reproduce. For visual HUD reports, include a screenshot and note which optional HUD mods are installed. Back up worlds before testing prereleases.

## Credits and links

- [Modrinth project](https://modrinth.com/mod/legendary-survival-overhaul) — feature overview and original screenshots.
- [Community Discord](https://discord.gg/XPHtcP89P3)
- [Minecraft Legendary Edition guide](https://minecraft-legendary-edition.gitbook.io/minecraft-legendary-edition)
- [YouTube](https://www.youtube.com/@MinecraftLegendaryEdition/featured)
- [Ko-fi](https://ko-fi.com/legendaryworkshop)
