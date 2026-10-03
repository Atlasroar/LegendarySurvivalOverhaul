# Mod Compatibility

This page distinguishes implemented paths from general compatibility assumptions. Unlisted mods are not automatically supported or incompatible.

## Libraries

Fabric API, Fzzy Config, Fabric Language Kotlin, and Trinkets are required. Cardinal Components base/entity modules are bundled. See [Installation and Upgrading](Installation-and-Upgrading) for versions.

Forge Config API Port is only needed by older LSO versions, or by other installed mods that still depend on it.

## Optional integrations

| Mod | Implemented support and limits |
| --- | --- |
| Mod Menu | Config button supplied through Fzzy Config; LSO supplies icon, project/contact links, and wiki metadata. |
| LevelZ / WandererZ | No conflict with level-based max-health bonuses; death-respawn heal deferred so it never overwrites the mod's attribute bonus. Confirmed in-game: the bonus remains stable across death/respawn. See "LevelZ/WandererZ health compatibility" below. WandererZ's bundled food-spoilage system (SpoiledZ) is explicitly excluded from LSO's own drink items. |
| Overflowing Bars | Shared HUD heights, shield/armor separation, broken-heart rendering, and vehicle-row gap correction; tested in-game. |
| Serene Seasons | Season cards (including tropical Wet/Dry), temperature integration, and out-of-season bonemeal warning. Cards and warning were tested in-game. |
| Supplementaries | Ordinary consumable finish-use flow uses LSO's existing hook; this is not a claim of a complete custom integration. |
| Item Descriptions / Mod Descriptions | LSO ships its own `lore.legendarysurvivaloverhaul.*` entries for every item and block; no dependency required. See "Item Descriptions, Mod Descriptions and Field Guide" below. |
| Field Guide | Purely client-side spyglass scanning of entities/blocks; no conflicts. LSO ships a built-in Field Guide "Flora" category for Sun Fern/Ice Fern/Water Plant crops, and any LSO item referenced from a Field Guide entry picks up LSO's Item Descriptions lore automatically. |
| Immersive Overlays | Not currently reachable on Fabric 1.20.1 — see "Item Descriptions, Mod Descriptions and Field Guide" below for details and the upstream limitation. |

For tropical Serene Seasons cards, enable **Tropical Seasons Enabled** as well as season cards. The bonemeal warning was checked with seasonal crops enabled and the out-of-season behavior set to disallow growth.

## Explicitly excluded or not required

| Mod/workflow | Status |
| --- | --- |
| Origins | Original adapter intentionally unsupported in this Fabric port. |
| Meds and Herbs | Original integration targets a Forge-only mod; unsupported. |
| Curios | Replaced by Trinkets for this port, not required. |
| Thermoo | Not required; LSO retains its own temperature model. |
| Balm | Not required; not adopted as a platform dependency. |
| Create, Aether, Dimensional Doors air adapters | Outside the implemented Thin Air integration scope. |
| Separate Thin Air installation | Not required; coexistence is not claimed as tested. |

## Trinkets and integrated Thin Air

Trinkets is the accessory API, not Curios. **LSO - Trinkets** customizes allowed slots for eight LSO accessories. It does not create slot types or change other mods' items. The Respirator/face mask is excluded; its existing face-slot behavior remains.

Custom Trinkets slot datapacks work only when the type exists and is assigned to players. While Use Configured Slots is enabled, the configured lists own those eight items' acceptance rather than their normal item tags. Disable it for tag-based pack control. See [Trinkets Configuration](Trinkets-Configuration) and [slot recipes](Datapack-Air-and-Slot-Recipes).

Thin Air mechanics are built into LSO, with the dependencies/defaults listed in this wiki. The original Thin Air download page's Forge/Puzzles Lib requirements are not LSO requirements. Do not install a second Thin Air implementation expecting automatically compatible duplicate mechanics.

## Modded biomes and terrain (v2.3.0)

| Project | Support boundary |
| --- | --- |
| [Terralith](https://github.com/Stardust-Labs-MC/Terralith/tree/1.20) | Registered biomes use native climate; biome temperature overrides work with both mod and datapack installations, without requiring a matching mod ID |
| [Tectonic](https://github.com/Apollounknowndev/tectonic/tree/v2/1.20) | Terrain uses normal LSO biome/altitude evaluation; no custom biome table or terrain patch |
| [Lithostitched](https://github.com/Apollounknowndev/lithostitched/tree/1.20.1) | Worldgen infrastructure, not a set of biomes; no mandatory LSO dependency or direct API adapter |

No hand-tuned Terralith temperature presets are shipped. Biome JSON temperature is a Minecraft climate value, not Celsius. Existing altitude, season, weather, nearby-block and underground modifiers continue to contribute.

v2.3.0 does not change the default Overworld air-height limits: Tectonic's taller terrain can still encounter high-altitude YELLOW air. Adjust Air profiles deliberately if your pack should differ.

## LevelZ/WandererZ health compatibility

[LevelZ](https://github.com/Globox1997/LevelZ/tree/1.20) (requires its [LibZ](https://github.com/Globox1997/LibZ/tree/1.20) library) can grant a level-based bonus to the `generic.max_health` attribute via its own `EntityAttributeModifier`. LSO's health overhaul already adds its own max-health delta as a single fixed-UUID modifier without touching the attribute's base value or other mods' modifiers, so the two mods' bonuses stack correctly regardless of load order.

The one real conflict: on death, `ModCapabilities.copyPlayerState` healed the player to `getMaxHealth()` immediately during `ServerPlayerEvents.COPY_FROM`. LevelZ reapplies its own max-health attribute bonus later, from its own `AFTER_RESPAWN` listener — Fabric doesn't guarantee ordering between independent mods' listeners. That could snapshot an incomplete (pre-LevelZ-bonus) max health on a death respawn. LSO's death-respawn heal is now deferred to the player's next server tick (instead of healing immediately during `COPY_FROM`), so it always reads the final, fully-combined max health — LevelZ's bonus is never clobbered or read too early. The world/dimension-change heal path is unaffected, since LevelZ does not recompute attributes synchronously there.

Confirmed in-game (v2.4.0): the LevelZ health bonus remains stable across death and respawn, operating as intended.

LibZ itself only supplies shared GUI/network/config/registry utilities for LevelZ; it has no attribute or health logic of its own, so no additional adapter is needed for it.

**[WandererZ](https://github.com/Atlasroar/WandererZ)** is the continuation of LevelZ, merging LevelZ's own level/skill system together with Jobs, RPG Difficulty, Tiered, Party and a food-spoilage system (internally named SpoiledZ) into a single mod (`wandererz` mod id). The max-health timing fix above is implemented generically — it reacts to any mod's `AFTER_RESPAWN`-timed attribute recomputation rather than gating on a specific mod ID — so it applies to WandererZ's level-based max-health bonus exactly the same way it applies to standalone LevelZ, with no separate adapter required.

**Food spoilage exclusion (by design):** WandererZ's bundled SpoiledZ system tracks spoilage NBT on any item that is vanilla "food" (`Item#isFood()`) or explicitly added to the `spoiledz:spoiling_items` tag, unless it is listed in `spoiledz:non_spoiling_items`. None of LSO's thirst/drink items (Canteen, Large Canteen, the Juice line, Purified Water Bottle, Water Plant Bag) declare vanilla `FoodProperties` — they are driven entirely by LSO's own Thirst capability and custom use/finish-use logic — so they are not swept up by SpoiledZ automatically. LSO still ships `data/spoiledz/tags/items/non_spoiling_items.json`, explicitly adding all of its drink items to that tag, as a defensive guarantee that WandererZ's food-spoilage mechanic never applies to LSO's drinks regardless of future changes on either side.

## Enchanted Golden Apple and death heart-loss (v2.5.0, HardcoreLite-inspired)

Inspired by [HardcoreLite](https://github.com/MC-Mods-Pete/HardcoreLite/tree/1.20.1-Fabric), LSO adds two independently config-driven, opt-out mechanics layered on its existing heart-container/shield-health systems rather than copying HardcoreLite's own max-health/gamemode logic:

- **Enchanted Golden Apple bonus** (`enchantedGoldenAppleOverrideEnabled`, default on): eating specifically an Enchanted Golden Apple grants a configurable Shield Health bonus (default `enchantedGoldenAppleShieldHealth = 4.0`, i.e. 2 Shield Hearts) and repairs/restores Heart Containers (default `enchantedGoldenAppleHeartContainersRepaired = 1`). This is distinct from, and takes priority over, the generic vanilla-Absorption override (`absorptionEffectOverride`) used for other Absorption sources such as a regular Golden Apple — the two do not stack.
- **Death heart loss** (`heartsLostOnDeath`, default `1`) removes one Heart Container per death, down to a configurable floor (`permanentHearts`, default `1`, i.e. 1 Heart Container / 2 health). Unlike HardcoreLite, LSO never forces Spectator mode at the floor — the existing floor-clamping `loseHearth` logic simply stops reducing hearts once the floor is reached.

**LevelZ precedence (by design):** when LevelZ is installed, its level-based `generic.max_health` bonus is treated as the baseline on top of which LSO's own heart-loss delta is applied. In practice this means a player's level-based LevelZ health floor takes precedence over LSO's `permanentHearts` floor — e.g. a level-0 LevelZ character starting at 3 Heart Containers (6 health) will not be brought lower than that by LSO's death heart-loss, even if `permanentHearts` is configured as low as `1`. This is intentional for compatibility between the two mods; raising a player's LevelZ level still raises how far they can be brought down by subsequent deaths. Like the full-heal path above, the heart-loss floor check is deferred to the player's next server tick on death so it always reads the final, LevelZ-combined max health rather than a pre-LevelZ snapshot.

Both features are fully config-driven (Fzzy Config) and can be disabled or tuned per world, including reverting to the pre-v2.5.0 defaults (`heartsLostOnDeath = 0`, `permanentHearts = 10`).

## Item Descriptions, Mod Descriptions, Field Guide, and Immersive Overlays (v2.6.0)

[Item Descriptions](https://github.com/cassiancc/Item-Descriptions) shows a per-item/block description when holding Ctrl, driven entirely by lang-file entries in the form `lore.<namespace>.<id>`. LSO ships its own `lore.legendarysurvivaloverhaul.*` entries for every item and block it registers, so descriptions appear automatically with just Item Descriptions installed — no LSO-specific dependency, patch, or [Mod Descriptions](https://github.com/cassiancc/Mod-Descriptions) resource pack entry is required (Item Descriptions prefers a mod's own translations when present).

[Field Guide](https://github.com/evanbones/Field-Guide) is a purely client-side spyglass-scanning mod for entities, blocks and multiblock structures; it does not hook into LSO at all, so there is no conflict. LSO does not register any custom `EntityType`, so none of its content is scanned by default. LSO ships its own Field Guide datapack entry, `data/legendarysurvivaloverhaul/fieldguide/categories/flora.json`, which adds a dedicated "LSO Flora" category (named via `category.legendarysurvivaloverhaul.fieldguide.flora` in the lang file) auto-populated (`mod_plants:legendarysurvivaloverhaul`) with Sun Fern, Sun Fern (Gold), Ice Fern and Water Plant crops. This only takes effect when Field Guide is installed; it is otherwise unused and harmless. The tab icon (`assets/legendarysurvivaloverhaul/textures/gui/fieldguide/flora_icon.png`) is a sepia-toned recolor of the Sun Fern Leaf item sprite — same silhouette, remapped to Field Guide's own sepia tab-icon palette — since Field Guide binds category icons as raw GUI textures rather than rendering them through the item renderer, so a bare item ID or the item's true-color texture would not fit visually. The actual in-game item texture is untouched; this is a separate, standalone asset. Any LSO item appearing in a Field Guide entry automatically picks up LSO's Item Descriptions lore as its prefilled note, per Field Guide's own "Take notes" feature. As of v2.6.2, the flora seed/leaf/crop `lore.legendarysurvivaloverhaul.*` entries also state each plant's crafting use (e.g. Warm String, Cold String, drinkable Water Plant Bags), so this prefilled note tells players what the plant is for, not just what it is.

[Immersive Overlays](https://github.com/cassiancc/Immersive-Overlays) already ships a `LegendarySurvivalOverhaulCompat` class that sources its thermometer overlay from `sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil#getPlayerTargetTemperature`/`#getTemperatureEnum` — LSO's existing public API fully supports this call on Fabric 1.20.1. However, in Immersive Overlays' own multi-loader build, that compat class is currently compiled only for Forge and NeoForge 1.21.1 (gated by a Stonecutter `//? if (forge) || (neoforge && =1.21.1)` block); on Fabric the method unconditionally returns `null`, so the thermometer overlay never queries LSO there today. This is an upstream build-configuration gap in Immersive Overlays, not something fixable from LSO's codebase. No further action is planned on LSO's side for this integration.

## Optional VanillaBackport sulfur support

[VanillaBackport](https://github.com/ItsBlackGear/VanillaBackport/tree/1.20.1) support targets the **1.2 development branch** and its matching [Platform](https://github.com/ItsBlackGear/Platform/tree/1.20.1) dependency. Sulfur caves use `minecraft:sulfur_caves`, not the mod namespace.

With the Air toggle on, that biome has YELLOW ambient air using the Nether drain interval. Eye fluids and nearby providers retain priority; the sulfur rule precedes ambient dimension profiles. The installed-mod check keeps this rule inactive without Backport.

An equipped Respirator now prevents new vanilla Nausea effects from any source, independently of Backport. Both `LivingEntity.addEffect` overloads pass through the existing LSO effect hook. No fragile upstream gas-method mixin or direct Platform dependency is required. This does not cure existing Nausea or protect a merely held mask.

The user tested beta.1 biome/air behavior in-game successfully. Mask protection was revised after a reported failure and passed isolated server assertions; revised in-game protection remains pending. The synthetic upstream-shape fixture is not full real-mod/client validation.

## Future integrations

Additional mod support is the next development phase. Targets and desired interactions must be selected before implementation. New adapters should remain optional where possible and preserve behavior without the target mod installed.

Compatibility reports should include exact mod versions, dependency versions, logs, and a small reproduction case.
