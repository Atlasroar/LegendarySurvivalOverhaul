# Legendary Survival Overhaul Wiki

**Make survival matter.** This wiki documents the **Fabric 1.20.1 port**, not the original Forge release.

![LSO environmental survival](https://cdn.modrinth.com/data/cached_images/b052d40ca61b2747c9003f4b508527ddd1db1df8.png)

*Original LSO feature image from the [LSO Modrinth project](https://modrinth.com/mod/legendary-survival-overhaul); artwork and screenshots credited to the upstream project.*

The latest full release is [**v2.3.0**](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v2.3.0): Minecraft 1.20.1, Fabric, Java 17. It includes **259 settings across nine configs**, native biome climates/datapack overrides, optional sulfur-cave air, and equipped Respirator protection against new Nausea effects. The Respirator remains excluded from accessory slot customization.

## For players

| Guide | What you will learn |
| --- | --- |
| [Installation and Upgrading](Installation-and-Upgrading) | Dependencies, client/server setup, and safe migration |
| [Survival Systems](Survival-Systems) | Temperature, hydration, localized injuries, health, and equipment |
| [Air Quality and Breathing](Air-Quality-and-Breathing) | Dimension hazards, providers, lanterns, and breathing protection |
| [Trinkets and Accessories](Trinkets-and-Accessories) | Accessory effects, slots, relocation, and Respirator exception |
| [Configuration](Configuration) | Nine config files, server ownership, edits, and legacy migration |
| [Air Configuration](Air-Configuration) | Every air control, units, defaults, and profile precedence |
| [Trinkets Configuration](Trinkets-Configuration) | Per-item slot lists and datapack fallback |
| [Mod Compatibility](Mod-Compatibility) | Implemented integrations versus unsupported adapters |
| [Troubleshooting](Troubleshooting) | Logs, HUD, breathing, config, and slot diagnostics |
| [Releases and Versioning](Releases-and-Versioning) | Release history, checksums, and SemVer |

## For coders and pack authors

| Reference | Contents |
| --- | --- |
| [Developer Hub](Developer-Hub) | Reading order, source layout, and contribution boundaries |
| [Architecture and APIs](Architecture-and-APIs) | Fabric, Cardinal Components, Fzzy, Trinkets, networking, and LSO facades |
| [Air System Internals](Air-System-Internals) | Lookup/cache precedence, air ticks, equipment wear, and suffocation |
| [Trinkets Integration Internals](Trinkets-Integration-Internals) | Predicate wrapping, insertion rules, immutable config lookup, and modifiers |
| [Datapack Customization](Datapack-Customization) | Create/install packs; exact paths, namespaces, and reload behavior |
| [Datapack Schema Reference](Datapack-Schema-Reference) | JSON fields, object/array shapes, examples, and pitfalls |
| [Datapack Air and Slot Recipes](Datapack-Air-and-Slot-Recipes) | Dimension air profiles, source tags, breathing tags, and custom slots |
| [Development and Work Plan](Development-and-Work-Plan) | Build commands, validation coverage, and completed/open work |
| [Credits and Licenses](Credits-and-Licenses) | Authors, third-party code, asset permissions, and image provenance |

## Validation and scope

The tracked Fabric gameplay port and air fixes are complete. Survival systems and air equipment were tested in single-player across the Overworld, Nether, and End. Air/Trinkets config prereleases were followed by user-directed full releases. Real-server assertions exercised migration, profiles, slot restrictions, and equipment boundaries.

Broader multiplayer scenarios, operator permissions/config synchronization, and interactions with untested mods remain open. A full release is not a claim that every config combination or datapack has been tested.

The user tested v2.3.0-beta.1 biome/sulfur air behavior successfully but reported mask failure. v2.3.0 replaces the upstream gas-method interception with vanilla Nausea interception. Revised mask behavior passed isolated server assertions, including without Backport; its in-game retest remains pending.

[Downloads](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases) | [Issues](https://github.com/Atlasroar/LegendarySurvivalOverhaul/issues) | [Source branch](https://github.com/Atlasroar/LegendarySurvivalOverhaul/tree/lso-fabric-1-20-1)
