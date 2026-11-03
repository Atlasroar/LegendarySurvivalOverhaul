# Mod Compatibility

This page distinguishes implemented paths from general compatibility assumptions. Unlisted mods are not automatically supported or incompatible.

## Libraries

Fabric API is required. Cardinal Components base/entity and NightConfig core/TOML are bundled. Starting with v3.0.0, **Fzzy Config and Trinkets are strongly recommended, not required**; Fabric Language Kotlin is also optional for LSO. v2.8.1 and earlier still require all three. Fzzy Config's own Kotlin dependency must still be installed if you choose to install Fzzy Config. See [Installation and Upgrading](Installation-and-Upgrading) for versions.

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
| Farmer's Delight Refabricated, HerbalBrews, Vinery, Meadow, Farm & Charm, Bakery, Candlelight, Brewery | Drink/beverage items (and a few relevant foods) from each installed mod grant LSO Thirst hydration via a pure datapack — no code dependency on any of these mods. See "Hydration datapack support for Let's Do + Farmer's Delight" below. |

For tropical Serene Seasons cards, enable **Tropical Seasons Enabled** as well as season cards. The bonemeal warning was checked with seasonal crops enabled and the out-of-season behavior set to disallow growth.

## Explicitly excluded or not required

v3.0.0 removes bundled datapack compatibility for Alex's Mobs, Atmospheric, Better End Forge, Born in Chaos, BYG, Call of Yucatan (`call_of_yucutan`), Cataclysm, Crabber's Delight, Create, Create Confectionery, Crockpot, Curios, Decorative Blocks, Endergetic, Farmer's Respite, Hearth and Harvest, Seasonals, Survival Instinct, TerraFirmaCraft, Upgrade Aquatic, Valhelsia Structures, Wardrobe, and Quark, including Quark's asset overrides. Unused Forge loot modifiers and biome modifiers are removed; retained recipes use Fabric-compatible tags and NBT ingredients. Farmer's Delight **Refabricated** (`farmersdelight`) and Fabric BetterEnd (`betterend`) data remain supported.

The `data/forge` compatibility namespace is also removed. This cleanup removes LSO's bundled survival-value/asset support; it does not mean every listed mod is categorically incompatible or unavailable on Fabric.

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

When installed, Trinkets is the accessory API, not Curios. **LSO - Trinkets** customizes allowed slots for eight LSO accessories. It does not create slot types or change other mods' items. Without Trinkets there are no accessory slots or auto-equipping; held-item effects still work. Existing head-slot breathing-equipment checks are retained, but this does not add a vanilla equip path for the Respirator. The Respirator/face mask is excluded from configurable accessory slots; its existing face-slot behavior remains when Trinkets is installed.

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

## Hydration datapack support for Let's Do + Farmer's Delight (v2.8.0)

LSO's Thirst system loads cross-mod hydration/saturation values from reloadable JSON resources at `data/<mod id>/legendarysurvivaloverhaul/thirst/consumables/<item id>.json` (and `data/<mod id>/legendarysurvivaloverhaul/thirst/blocks/<block id>.json` for drinkable blocks like kegs or sinks). Each file is a plain data array — `[{"hydration": <int>, "saturation": <float>, "effects": [...], "properties": {...}}]` — keyed by the target mod's own namespace and item/block path, so it only activates when that mod is actually installed (`FabricLoader.isModLoaded(namespace)`), and is otherwise inert. No Java dependency, mixin, or compile-time reference to the target mod is required; this is why the feature is described as a "datapack" — it ships as resources, not code.

v2.8.0 adds hydration coverage, ported from LSO's existing (pre-Fabric-port) value set for these same mods, for:

| Mod | Namespace | Coverage |
| --- | --- | --- |
| [Farmer's Delight Refabricated](https://github.com/MehVahdJukaar/FarmersDelightRefabricated/tree/fabric/lts/1.20.1) | `farmersdelight` | Tomato, milk bottle, apple cider, hot cocoa, melon juice, tomato sauce, melon popsicle, fruit salad, and the chicken/vegetable/pumpkin soups. |
| [HerbalBrews](https://github.com/Let-s-Do-Collection/HerbalBrews/tree/1.20.1) | `herbalbrews` | All brewed teas (green, black, hibiscus, lavender, rooibos, oolong, yerba mate), coffee, milk coffee, and the flask. |
| [Vinery](https://github.com/Let-s-Do-Collection/Vinery/tree/1.20.1) | `vinery` | Grapes/grape juices, cider, and the full wine lineup; higher-effort wines (Mead, Jellie Wine, Nether/End wines, etc.) grant progressively larger Thirst-effect buffers. Rotten cherry is a small hydration penalty, matching spoiled food behavior. |
| [Meadow](https://github.com/Let-s-Do-Collection/Meadow/tree/1.20.1) | `meadow` | Wooden milk buckets (sheep/buffalo/goat/warped/grain/amethyst variants) and the wooden water cauldron block. |
| [Farm & Charm](https://github.com/Let-s-Do-Collection/FarmAndCharm/tree/1.20.1) | `farm_and_charm` | Tomato, strawberry, the strawberry/nettle/ribwort teas (cup and non-cup forms), and its soups. |
| [Bakery](https://github.com/Let-s-Do-Collection/Bakery/tree/1.20.1) | `bakery` | No dedicated beverages exist in Bakery; its moistest item (Pudding Slice) grants a small hydration amount. |
| [Candlelight](https://github.com/Let-s-Do-Collection/Candlelight/tree/1.20.1) | `candlelight` | Mozzarella, tomato/mushroom soups, and its full set of kitchen sink blocks (filled state) as a drinkable water source. |
| [Brewery](https://github.com/Let-s-Do-Collection/Brewery/tree/1.20.1) | `brewery` | All whiskey and beer variants, both as consumed items and as their keg/tap blocks, so drinking directly from a placed keg hydrates the same as drinking a bottled serving. |

None of these mods' items declare vanilla `FoodProperties`-driven Thirst by themselves — LSO's values are additive data, not an override of anything the target mod ships. Uninstalling any of these mods makes its datapack entries simply unused; nothing in LSO depends on them being present.

## Immersive Weathering storms (v3.1.0)

v3.1.0 optionally supports **Atlasroar's [Immersive Weathering 2.5.0+ Fabric 1.20.1 fork](https://github.com/Atlasroar/Immersive-Weathering)**. IW is neither bundled nor required; install its own Moonlight/Fabric dependencies if choosing it. Older IW versions leave the adapter disabled with a warning.

Enable `temperatureEnabled` and the independent, default-enabled `immersiveWeatheringStormsEnabled` in `temperature.toml`. The optional Fzzy editor exposes the same toggle. v3.1.0 has 263 settings across nine configs; v3.0.0 had 262.

| Exposure | LSO behavior | Drink protection |
| --- | --- | --- |
| Snowstorm | Drive the target to frostbite (5) or colder, with cooling steps at 2x the normal rate; apply Cold Hunger during exposure | Hot Drink (`legendarysurvivaloverhaul:hot_drink`) blocks both storm penalties |
| Sandstorm, regular or red | Drive the target to heatstroke (35) or hotter, with heating steps at 2x the normal rate; apply Heat Thirst during exposure | Cold Drink (`legendarysurvivaloverhaul:cold_drink`) blocks both storm penalties |
| Actual freezing-wind hit | Apply/refresh Cold Hunger for 200 game ticks (10 seconds) from the last hit; keep IW's push/Slowness behavior | Hot Drink does not block this separate hit penalty |

Storm detection uses IW's real weather/biome/config predicates, not particles. Snow requires IW-enabled snowstorms during snowy-biome thunder; ordinary snowfall does not qualify. Sand uses IW's tagged dry-biome rain conditions and yields when IW yields to Scorchful. IW's presentation yielding to Immersive Storms does not disable snowstorm gameplay.

**Shelter:** every column in a centered 3x3 horizontal area needs a block that obstructs skylight within the first 10 block positions above the player's eye block. All nine columns must qualify; heights can differ. A single uncovered corner, a roof above that limit, or skylight-transparent glass does not count. This deliberately replaces IW's single-column sky-exposure check for LSO's storm penalties. It does not prevent an actual freezing-wind collision from causing its separate penalty.

Storm penalties stop when sheltered, out of the storm, or protected by the matching drink. General temperature and matching cold/heat immunity are respected; Creative/Spectator are excluded. Existing body temperature does not reset: ordinary frostbite/heatstroke effects can still apply after leaving a storm. Ambient secondary-effect toggles do not disable this independent integration. Finite wind hunger survives temperature updates, storm overlap, and capability saves instead of being removed early.

Dedicated-server assertions pass with IW absent and with the real 2.5.0/Moonlight mods installed, covering the rate, targets, shelter boundaries, drinks, toggles, and a real wind hit. See `test-runtime/weathering-storms/README.md`. Client visuals and separate-client multiplayer remain unverified.

## Wetness comparison and content ports (v3.2.0)

Released in v3.2.0. This is a standalone port of
selected mechanics/assets, **not** a requirement to install Scorchful,
Frostiful or Thermoo, nor a claim that running all their temperature systems
together has been tested.

| System | Water/wetness behavior in the examined 1.20.1 branch |
| --- | --- |
| LSO | Player wetness is capped at 400 and builds gradually from fluid contact, rain and water cauldrons; it dries over time or faster while burning/in lava. A saturation-scaled target offset defaults to -10 fully soaked. Canteen self-watering and Sponge accessory behavior remain. The new `wetCoolingRateMultiplier` defaults to 1.5 at any nonzero wetness, multiplying only cooling steps, without biome/humidity restrictions. |
| Scorchful | Thermoo wetness; submersion immediately soaks, contact/rain adds wetness, dry/fire rates are configurable. Evaporative cooling applies when temperature is positive, with reduced effectiveness in tagged humid biomes. Lily/water-skin mechanics connect wetness to heat survival. |
| Frostiful | Thermoo wetness; rain/contact/cauldrons add wetness and submersion/bubble columns immediately soak. Block light and fire increase drying. Saturation scales passive cold exposure; frigid-water immunity can bypass that multiplier. |
| Thermoo | Shared living-entity temperature/wetness framework, rather than LSO's standalone player model. The examined controller defaults to 600 maximum wet ticks, with 0-1 saturation and delegated wetting/drying changes. |

LSO deliberately keeps its existing acquisition/drying rates and target
model, rather than importing immediate-soak, sweating/dehydration,
humidity penalties or the complete upstream thermal systems. A warmer
adjusted target can still warm a wet player. Wet cooling combines with
IW snowstorm cooling multiplicatively, at 3x baseline by default.

The content port adds Scorchful's crimson/warped lily textures/shared model,
adapted to LSO wetness and canteens, plus Frostiful's non-armored fur textures
and ice-skate movement/blade model. Existing winter IDs/protection are
preserved. Lilies use natural crimson/warped forest patches and
nylium/rooted-dirt substrates instead of porting rooted blocks/farm
structures. See [Survival Systems](Survival-Systems#wetness-nether-lilies-fur-and-skates-v320)
for exact interactions and obtainability.

Source snapshots examined:
[Scorchful](https://github.com/TheDeathlyCow/scorchful/tree/d74712e9b5fc4348aa8ad9c417ce938daf8bc0a1),
[Frostiful](https://github.com/TheDeathlyCow/frostiful/tree/f017b15a2b23a6474d03d8e4b4e9e244247ffa24),
[Thermoo](https://github.com/TheDeathlyCow/thermoo/tree/915def30a0374121e90485c0240001702b507aa5).
Imported/adapted portions remain LGPL-3.0 with source mapping and license
texts under `META-INF/licenses`; Thermoo code/assets are not copied.

## Future integrations

Additional mod support is the next development phase. Targets and desired interactions must be selected before implementation. New adapters should remain optional where possible and preserve behavior without the target mod installed.

Compatibility reports should include exact mod versions, dependency versions, logs, and a small reproduction case.
