# Development and Work Plan

## Build

The project uses JDK 17, Gradle 8.12, and Fabric Loom 1.10.5.

Windows PowerShell:

```powershell
$env:JAVA_HOME = "C:\path\to\jdk-17"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
.\gradlew.bat build
```

macOS/Linux:

```sh
./gradlew build
```

Artifacts are written to `build/libs`. Development commands are `runClient` and `runServer`. Reaching a title screen is not proof of working gameplay or UI.

## Architecture

- Fabric APIs and focused mixins replace Forge runtime event hooks.
- Cardinal Components stores persistent player survival data.
- Fabric networking handles requests and survival dataset synchronization.
- Runtime datapack reload listeners preserve data-driven behavior.
- Dependency-free TOML schemas and LSO-owned synchronization refresh `Config.Baked`; optional Fzzy editor adapters use the same schemas.
- Client-only rendering and input stay on the client side.
- Checked-in generated resources are packaged; Forge datagen execution is excluded.

Before restoring an excluded integration, inspect build exclusions and registration paths. Removing an exclusion alone is not a complete Fabric port.

## Closed work

| Area | Status |
| --- | --- |
| Build, metadata, registration | Completed |
| Persistent player state and networking | Completed |
| Gameplay hooks and Forge parity audit | Completed; intentional platform differences documented |
| HUD, tooltips, Overflowing Bars | Completed; tested visual fixes |
| Survival blocks and equipment | Completed tracked slices; tested in-game |
| Thin Air mechanics and dimensional fixes | Released and tested across all three vanilla dimensions |
| Turtle Helmet / Free Breathing | Completed |
| Fzzy Config / Mod Menu / legacy migration | Released in v2.0.0 through PR #31 |
| Dedicated Air config / Common migration | Released in v2.1.0; 25 controls, default-preserving profiles/rates and tested migration |
| Configurable Trinkets accessory placement | Released in v2.2.0; eight item lists, tag fallback, Respirator exclusion and preservation of equipped stacks |
| Player/developer wiki and simplified README | Updated for v2.2.0; detailed codecs/APIs, attributed media and exact sync limits |
| Datapack-only biome overrides / native climate compatibility | Implemented in v2.3.0; user reported beta.1 biome behavior works in-game |
| Optional sulfur-cave ambient air | Implemented in v2.3.0 for Backport 1.2 development branch; user reported beta.1 air behavior works |
| Respirator Nausea prevention | Revised for v2.3.0 to block new Nausea from any source, with tooltip and toggle; isolated server assertions passed |
| SemVer and obsolete branch/PR cleanup | Completed |
| LevelZ health compatibility | Implemented; death-respawn heal deferred by one tick so LevelZ's max-health attribute bonus is never overwritten or read early; confirmed in-game: health bonus remains stable after death ([#34](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/34), [#35](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/35)) |
| HardcoreLite-inspired Enchanted Golden Apple / death heart-loss | Implemented in v2.5.0; item-specific Shield Health bonus + Heart Container repair for Enchanted Golden Apple, death heart-loss with a non-spectator floor; LevelZ's level-based health floor intentionally takes precedence; confirmed in-game with and without LevelZ installed ([#36](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/36), [#37](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/37)) |
| Item Descriptions / Mod Descriptions / Field Guide | Released in v2.6.0 with a pure-data flora category; icon and crafting-purpose text improved in v2.6.1/v2.6.2 ([#38](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/38), [#39](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/39), [#40](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/40)) |
| Left Shift keybind hijack | Unbound the default in v2.6.3 and migrated saved bindings in v2.6.4; user-confirmed fixed in-game ([#42](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/42), [#43](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/43)) |
| WandererZ compatibility / project notices | Released in v2.7.0; generic level-health timing plus drink spoilage opt-out, corrected project license/credits and issue templates |
| Hydration datapacks | Released in v2.8.0 for Refabricated and seven Let's Do mods |
| Freezing/heat overlay blending | Released in v2.8.1; user-confirmed in-game |
| Optional libraries / compatibility cleanup / canteen enchants | Released in v3.0.0; standalone TOML/config sync, presence-gated Trinkets/Fzzy, requested data removals, and canteen-only loot enchantments |
| Immersive Weathering storms | Released in v3.1.0; optional 2x storm temperature penalties, drink protection, 3x3 shelter, and 10-second freezing-wind Cold Hunger |
| Wet cooling and survival gear | Released in v3.2.0; configurable cooling-only wetness rate, standalone Nether lilies, non-armored fur replacement and Ice Skates with unchanged winter protection |
| Lily Field Guide entries | Released in v3.2.1; additive optional plant tag |
| Skating/wetness compatibility | Released in v3.2.2; composable friction, player blade attribution, immunity-aware wetting, modded/thin-ice surfaces and independent thermal coexistence warning |

## Developer wiki

The wiki includes [Internal Jars and Dependencies](Internal-Jars-and-Dependencies), [Client Input and Keybindings](Client-Input-and-Keybindings), [Compatibility Hook Internals](Compatibility-Hook-Internals), and [Pull Request Changelog](Pull-Request-Changelog). These formerly wiki-only pages are now also versioned in `docs/wiki`, so publishing cannot silently restore outdated dependency guidance.

## Configuration and optional-library checks

```powershell
.\gradlew.bat -PwithoutOptionalIntegrations -I src\test\resources\config-tests.gradle configRegression configAdapterRegression
```

The regressions cover TOML persistence, validation, migration, snapshot application/restoration, missing-library class isolation, and all 264 editor bindings. See `test-runtime/optional-integrations/README.md` for dedicated-server assertions with the optional libraries installed/absent and the distributable resource checker. See `test-runtime/weathering-storms/README.md` for the real IW 2.5.0/Moonlight fixture, including the combined 3x wet snowstorm rate. See `test-runtime/wet-content/README.md` for isolated server/client checks covering cooling, lily interactions/worldgen, preserved protection, real skating movement hooks, baked models and packaged resources. Equipped appearance, skating feel and multiplayer movement remain in-game follow-ups. These fixtures do not ship in release JARs.

## Open validation

Broader multiplayer/dedicated-server gameplay, real operator permissions/config synchronization, and exhaustive in-game edit coverage remain open. Dedicated-server assertions tested Air migration, rates, boundaries and equipment, plus Trinkets list validation, slot relocation, fallback, auto-equip and stack preservation. These are not a substitute for multiplayer gameplay testing. Opt-in runtime assertion fixtures are now checked in under `test-runtime`. Their READMEs explain isolated server setup and validation scope; they are not packaged in release jars.

v3.0.0 assertions additionally cover saved nondefault configs with the optional libraries present/absent, canteen-only anvil behavior, loot/table/trade restrictions, retained recipes, and Refabricated resource loading through a synthetic alias. Actual multiplayer config packet flow, Fzzy editor interaction, and a real Refabricated installation still need in-game checks.

**Dataset sync follow-up:** 14 server listeners exist, but `syncAll()` sends 12 datasets. Temperature consumable blocks has a client receiver but no send in that method; air profiles have no dedicated dataset sync. Track these as integration/remote-client validation limits rather than claiming complete parity.

## Current validation follow-up

The user's beta.1 report confirmed biome/sulfur air behavior but found the mask ineffective. The replacement vanilla effect hook passed both overloads, equipped/held checks, unrelated-effect preservation, config toggles and tooltip tests, with and without a synthetic Backport fixture. In-game revised-mask retest remains open, along with actual remote-client/full upstream integration scenarios. The full v2.3.0 release was user-directed.

## Next phase

Add additional mod integrations after the user selects targets and desired behavior. Keep dependencies optional where appropriate, preserve current behavior, validate with and without the target mod, and document exact support boundaries.

Origins and Meds and Herbs remain intentionally unsupported; Forge datagen is a deliberate developer-workflow omission, not an unfinished gameplay task.

## Documentation maintenance

The wiki page sources live in `docs/wiki` in the main repository. Keep them aligned with the README and [detailed port history](https://github.com/Atlasroar/LegendarySurvivalOverhaul/blob/lso-fabric-1-20-1/docs/Fabric-Port-Wiki.md).

Update player-facing pages and release notes when behavior or dependencies change. Record what was actually built/tested; distinguish known limitations from historical issues fixed in later releases.

Start with the [Developer Hub](Developer-Hub), [Architecture and APIs](Architecture-and-APIs), and [Datapack Schema Reference](Datapack-Schema-Reference). Keep source-page names and sidebar links consistent when publishing to the wiki's separate Git repository.
