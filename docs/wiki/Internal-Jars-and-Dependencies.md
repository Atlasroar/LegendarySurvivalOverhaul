# Internal Jars and Dependencies

This page describes **v3.2.0**: what ships inside the LSO JAR, what must be installed alongside it, and what is optional. Check [`build.gradle`](https://github.com/Atlasroar/LegendarySurvivalOverhaul/blob/lso-fabric-1-20-1/build.gradle) for build/packaging declarations and [`fabric.mod.json`](https://github.com/Atlasroar/LegendarySurvivalOverhaul/blob/lso-fabric-1-20-1/src/main/resources/fabric.mod.json) for runtime requirements. v2.8.1 and earlier required Trinkets, Fzzy Config, and Kotlin; v3.0.0+ does not.

v3.2.0 includes selected Scorchful/Frostiful content as standalone LSO code
and assets, with LGPL-3.0 attribution and license texts. Scorchful, Frostiful
and Thermoo are neither bundled nor required. This is not a new integration
with their runtime temperature systems.

## How to read `build.gradle`'s dependency block

```groovy
dependencies {
    minecraft "com.mojang:minecraft:${minecraft_version}"
    mappings loom.officialMojangMappings()

    modImplementation "net.fabricmc:fabric-loader:${loader_version}"
    modImplementation "net.fabricmc.fabric-api:fabric-api:${fabric_version}"
    modCompileOnly "dev.emi:trinkets:${trinkets_version}"
    modCompileOnly "me.fzzyhmstrs:fzzy_config:${fzzy_config_version}"
    modCompileOnly "net.fabricmc:fabric-language-kotlin:${fabric_kotlin_version}"
    if (!project.hasProperty('withoutOptionalIntegrations')) {
        modLocalRuntime "dev.emi:trinkets:${trinkets_version}"
        modLocalRuntime "me.fzzyhmstrs:fzzy_config:${fzzy_config_version}"
        modLocalRuntime "net.fabricmc:fabric-language-kotlin:${fabric_kotlin_version}"
    }
    modLocalRuntime "com.terraformersmc:modmenu:${modmenu_version}"
    modImplementation "dev.onyxstudios.cardinal-components-api:cardinal-components-base:${cardinal_components_version}"
    modImplementation "dev.onyxstudios.cardinal-components-api:cardinal-components-entity:${cardinal_components_version}"
    include "dev.onyxstudios.cardinal-components-api:cardinal-components-base:${cardinal_components_version}"
    include "dev.onyxstudios.cardinal-components-api:cardinal-components-entity:${cardinal_components_version}"
    modCompileOnly("curse.maven:serene-seasons-291874:6398228") { transitive = false }
    modCompileOnly("curse.maven:glitchcore-955399:5787838") { transitive = false }
    implementation "com.electronwill.night-config:core:3.6.7"
    implementation "com.electronwill.night-config:toml:3.6.7"
    include "com.electronwill.night-config:core:3.6.7"
    include "com.electronwill.night-config:toml:3.6.7"
}
```

Loom's `include(...)` embeds the dependency for Fabric Loader to discover, so no separate download is needed. `modImplementation` puts a mod on development compile/runtime classpaths but does not embed it by itself. `modCompileOnly` supplies API types at compile time; `modLocalRuntime` supplies them only for development runs. These Gradle configurations do not alone define player requirements: `fabric.mod.json` declares hard requirements under `depends` and optional recommendations under `suggests`.

Use `-PwithoutOptionalIntegrations` with `runClient` or `runServer` to omit Trinkets, Fzzy Config, and Kotlin from the development runtime. API-linked classes must remain behind presence checks and out of core class signatures.

## Bundled (jar-in-jar — no separate download needed)

| Artifact | Version key | Why it's bundled |
| --- | --- | --- |
| `cardinal-components-base` | `cardinal_components_version` | Backing store for `PlayerSurvivalComponents.PLAYER_SURVIVAL` (temperature/wetness/thirst/health/body-damage persistence). Small, compatible license, safe to embed. |
| `cardinal-components-entity` | `cardinal_components_version` | Entity-attachment module of the same API; required alongside `-base` for the player component to attach to `Player` entities. |
| NightConfig `core` / `toml` | 3.6.7 | Standalone TOML persistence and validation-backed configuration loading without Fzzy Config or Kotlin. Unmodified LGPL-3.0 libraries; notices/license texts ship in `META-INF/licenses`. |

The Cardinal Components modules were introduced with the original Fabric port foundation — [#1](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/1) — as the Fabric replacement for Forge's player-capability attachment, and expanded in [#7](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/7). NightConfig became bundled for standalone configuration in [#51](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/51).

## Required platform and separate downloads

Use Minecraft 1.20.1, Java 17, Fabric Loader 0.15.0+, and a compatible Fabric API build.

| Artifact | Why it is **not** jar-in-jar'd |
| --- | --- |
| Fabric API | Always assumed already present by essentially every Fabric mod; embedding it would duplicate a dependency the player already has and can cause classloading conflicts with other mods' copies. |

## Strongly recommended, not required

| Library | Purpose and behavior without it |
| --- | --- |
| Trinkets 3.7.2+ | Accessory slots, wearable effects, and Respirator face-slot equipping. Without it, LSO has no accessory slots/auto-equip or new vanilla mask-equip path; supported held-item effects and vanilla equipment checks remain. |
| Fzzy Config 0.7.7+1.20.1 | In-game configuration editor. Without it, edit the existing TOML files while the instance is stopped, then restart. Fzzy is not bundled; its distribution terms forbid jar-in-jar packaging. |
| Fabric Language Kotlin | Not required by LSO, but **required if Fzzy Config is installed**. Follow Fzzy's own dependency metadata. Neither Kotlin nor Fzzy is bundled in LSO. |

Trinkets support arrived in [#7](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/7), and Fzzy replaced Forge Config API Port in [#31](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/31). [#51](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/51) made all three libraries optional through absence-safe item/config classes, isolated adapters, shared schemas, and LSO-owned synchronization of eight gameplay configs. The client config remains local.

## Optional, local-runtime only

| Artifact | Gradle configuration | Behavior without it |
| --- | --- | --- |
| Mod Menu | `modLocalRuntime` | Mod Menu is only used in the *development* run configuration to test the config-button integration. It is never required by players; if absent, Fzzy Config simply has no Mod Menu entry point, and LSO otherwise functions identically. |

## Optional, compile-only integrations (never shipped, never required)

| Artifact | Gradle configuration | Used for |
| --- | --- | --- |
| Serene Seasons (`curse.maven:serene-seasons-291874`) | `modCompileOnly`, `transitive = false` | Compiling `common/integration/sereneseasons` and `client/integration/sereneseasons` against Serene Seasons' real classes, guarded at runtime by `LegendarySurvivalOverhaul.sereneSeasonsLoaded` (a Fabric Loader mod-presence check). If the player doesn't have Serene Seasons installed, none of this code path executes and the dependency is never loaded. |
| GlitchCore (`curse.maven:glitchcore-955399`) | `modCompileOnly`, `transitive = false` | Serene Seasons' own config classes extend GlitchCore's `night-config`-based types; LSO needs those types visible at compile time for the same integration package above. |

`transitive = false` on the Curse Maven entries prevents Loom from also pulling in those projects' own (potentially large or license-incompatible) transitive dependency trees just to satisfy compile-time type-checking.

This pattern — `modCompileOnly` plus a `LegendarySurvivalOverhaul.<mod>Loaded` boolean gate at the call site — is the established way to add an optional integration in this codebase. See [Architecture and APIs](Architecture-and-APIs) and [Mod Compatibility](Mod-Compatibility) for the equivalent **non-compile-time** integrations (LevelZ, HardcoreLite-inspired mechanics, Item Descriptions, Field Guide), which don't even need a compile-time dependency because they only communicate through vanilla attributes, lang-file conventions, or pure data files — see [Compatibility Hook Internals](Compatibility-Hook-Internals) for those.

The v3.0.0 compatibility-data removal does not remove Farmer's Delight Refabricated or Fabric BetterEnd support. Check the [explicit removal list](Mod-Compatibility#explicitly-excluded-or-not-required) rather than assuming every Fabric mod has a bundled adapter.

## Optional storm adapter (v3.1.0)

v3.1.0 also compiles against Atlasroar's Immersive Weathering 2.5.0 and Moonlight (`curse.maven:selene-499980:5619072`), both `modCompileOnly` with transitive dependencies disabled. Neither mod is bundled or required. Moonlight supplies IW's inherited block types so Loom can remap the upstream mod correctly; it is not an LSO runtime dependency. Players choosing IW must install IW's own required dependencies.

An absence/version-safe availability check guards the API adapter, and the optional mixin plugin skips the freezing-wind mixin without compatible IW. The isolated fixture in `test-runtime/weathering-storms` adds the real mods to its server runtime only with `-PwithWeathering`; normal development runs do not automatically install them. See [Mod Compatibility](Mod-Compatibility#immersive-weathering-storms-v310) for behavior and validation limits.

## Platform/version table

For the exact pinned versions of every dependency above (e.g. `fabric_version`, `trinkets_version`), see [`gradle.properties`](https://github.com/Atlasroar/LegendarySurvivalOverhaul/blob/lso-fabric-1-20-1/gradle.properties) and the version table already maintained in [Architecture and APIs](Architecture-and-APIs#platformdependency-apis).

[Developer Hub](Developer-Hub) | [Architecture and APIs](Architecture-and-APIs) | [Credits and Licenses](Credits-and-Licenses)
