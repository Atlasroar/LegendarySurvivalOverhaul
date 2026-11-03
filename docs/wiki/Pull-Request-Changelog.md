# Pull Request Changelog and Methodology

A browsable index of every merged pull request on [`lso-fabric-1-20-1`](https://github.com/Atlasroar/LegendarySurvivalOverhaul/tree/lso-fabric-1-20-1), grouped by subsystem, for anyone who wants the exact reasoning and methodology behind a change rather than just the end result. For the mechanics themselves, see the linked internals pages; this page is the "why" and "in what order" index.

PR numbering is not strictly chronological-by-feature — several early PRs (e.g. #9, #10) landed supporting infrastructure slightly out of the order their *release* notes describe, and #11 does not exist (opened then superseded without merging).

## Foundation and Fabric port

| PR | Title |
| --- | --- |
| [#1](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/1) | Start Fabric 1.20.1 port foundation |
| [#2](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/2) | Fix Fabric test build startup |
| [#3](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/3) | Restore Fabric survival HUD indicators |
| [#4](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/4) | Document Fabric port and restore health HUD overlay |
| [#5](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/5) | Update Fabric port wiki for health HUD release |
| [#6](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/6) | Port temperature immunity after death to Fabric |
| [#7](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/7) | Port effect interception, health handling, and Trinkets support |
| [#8](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/8) | Port Fabric survival, temperature, and seasonal effects |
| [#9](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/9) | Introduce Fabric HUD layer foundation |
| [#10](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/10) | Restore omitted Fabric gameplay hooks |
| [#12](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/12) | Remove Origins support and fix Serene Seasons warning |
| [#13](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/13) | Fix sewing table menu + confirm heater/cooler/coat behavior |
| [#14](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/14) | Record release .13 in docs |
| [#15](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/15) | Fix Overflowing Bars HUD compatibility |
| [#16](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/16) | Close Forge event-subscriber audit; first full release (.15) |

## Air quality system

| PR | Title |
| --- | --- |
| [#17](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/17) | Integrate Thin Air air quality for Fabric 1.20.1 |
| [#18](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/18) | Fix air-quality cache and HUD placement |
| [#19](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/19) | Move air-bar spacing to thirst HUD |
| [#20](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/20) | Detect flowing lava as a RED air provider |
| [#21](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/21) | Stabilize air quality ticks and Nether profiles |
| [#22](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/22) | Fix air sync flicker and lava provider tag |
| [#23](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/23) | Add green oak logs and Nether air drain |
| [#24](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/24) | Detect lava fluids within the configured air radius |
| [#25](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/25) | Add suffocation damage for air-quality air loss outside water |
| [#26](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/26) | Make Nether ambient air quality independent of datapack JSON loading |
| [#27](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/27) | Fix nearby air-provider scan masking hazards with a simultaneously-in-range safe source |
| [#28](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/28) | Add diagnostic logging to pin down Nether air bar freeze report |
| [#29](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/29) | Turtle helmet as breathing equipment; quiet air diagnostics |

See [Air System Internals](Air-System-Internals) for the resulting implementation.

## Versioning, config, and release process

| PR | Title |
| --- | --- |
| [#30](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/30) | Adopt Semantic Versioning, release 1.0.0 |
| [#31](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/31) | Migrate config to Fzzy Config + Mod Menu support (2.0.0) |
| [#32](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/32) | Merge v2.3.0 biome compatibility and Respirator Nausea fix into lso-fabric-1-20-1 |
| [#33](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/33) | Fold Fabric SemVer release notes into changelog.txt |

See [Releases and Versioning](Releases-and-Versioning) for the resulting policy and [Internal Jars and Dependencies](Internal-Jars-and-Dependencies) for why Fzzy Config cannot be jar-in-jar'd.

## Health, progression compatibility, and death mechanics

| PR | Title |
| --- | --- |
| [#34](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/34) | Add LevelZ health compatibility: defer death-respawn heal by one tick |
| [#35](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/35) | Release v2.4.0: LevelZ health compatibility |
| [#36](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/36) | Add HardcoreLite-inspired Enchanted Golden Apple and death heart-loss mechanics |
| [#37](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/37) | Release v2.5.0: Enchanted Golden Apple and death heart-loss mechanics |

See [Compatibility Hook Internals](Compatibility-Hook-Internals) for the resulting deferred-tick and dedup implementation.

## Field Guide, Item Descriptions, and flora content

| PR | Title |
| --- | --- |
| [#38](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/38) | Release v2.6.0: Item Descriptions, Mod Descriptions, and Field Guide compatibility |
| [#39](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/39) | v2.6.1: Sepia-tone Field Guide flora icon from real Sun Fern Leaf sprite |
| [#40](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/40) | v2.6.2: Flora item Uses: descriptions |
| [#41](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/41) | Docs: HardcoreLite credit + bring wiki current through v2.6.2 |

See [Compatibility Hook Internals](Compatibility-Hook-Internals) for the resulting lang-file/datapack conventions.

## Input and keybindings

| PR | Title |
| --- | --- |
| [#42](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/42) | Release v2.6.3: fix Left Shift keybind hijack |
| [#43](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/43) | Release v2.6.4: migrate existing Left-Shift added_desc keybinds |

See [Client Input and Keybindings](Client-Input-and-Keybindings) for the full root-cause analysis and migration pattern — this is the most detailed worked methodology example on the wiki and a good template for diagnosing future platform-level bugs.

## Recent releases and compatibility cleanup

| PR | Title |
| --- | --- |
| [#44](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/44) | Replace stock Forge MDK LICENSE.txt/CREDITS.txt with LGPL-2.1 and project credits |
| [#45](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/45) | Drop unneeded express-authorization clause for Fuzs's Thin Air credit |
| [#46](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/46) | Modernize issue templates for Fabric 1.20.1 support |
| [#47](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/47) | Add WandererZ compatibility (LevelZ max-health timing + SpoiledZ opt-out for drinks) |
| [#48](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/48) | Release v2.7.0 |
| [#49](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/49) | Add hydration datapack support for 8 Let's Do / Farmer's Delight mods (v2.8.0) |
| [#50](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/50) | Fix temperature overlay washout and release v2.8.1 |
| [#51](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/51) | Release v3.0.0: optional integrations and compatibility cleanup |
| [#52](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/52) | Release v3.1.0: optional Immersive Weathering storm integration |
| [#53](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/53) | Release v3.2.0: wet cooling, Nether lilies, fur clothing and Ice Skates |

See [Compatibility Hook Internals](Compatibility-Hook-Internals), [Internal Jars and Dependencies](Internal-Jars-and-Dependencies), and [Releases and Versioning](Releases-and-Versioning) for the current behavior, dependency boundaries, and migration notes.

## Using this index

Each PR title is exactly as merged; open the PR itself for the full description, diff, and any review discussion. When writing a new internals page or updating an existing one, link the specific PR(s) that introduced the behavior you're documenting rather than only describing the current state — this preserves the reasoning for future maintainers, per this wiki's methodology-first convention.

[Developer Hub](Developer-Hub) | [Development and Work Plan](Development-and-Work-Plan) | [Releases and Versioning](Releases-and-Versioning)
