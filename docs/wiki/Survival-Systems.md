# Survival Systems

LSO's survival mechanics are configurable. The following describes the Fabric release's systems, not a fixed difficulty preset.

![Original LSO survival feature overview](https://cdn.modrinth.com/data/cached_images/b052d40ca61b2747c9003f4b508527ddd1db1df8.png)

*Feature illustration from the [original LSO project](https://modrinth.com/mod/legendary-survival-overhaul), credited to the upstream project; not a v2.2.0 Fabric screenshot.*

## Getting started

Carry drinking supplies and suitable clothing before a long trip. Watch temperature and hydration instead of waiting for damage. Check limb injuries with **H**, and prepare healing supplies. Before entering the Nether or End, read the [breathing guide](Air-Quality-and-Breathing): dry land does not necessarily mean breathable air.

## Temperature and wetness

Temperature responds to the environment and equipment. Insulated armor, coats, temperature consumables, and heater/chiller blocks help manage dangerous cold and heat.

The HUD includes temperature and wetness indicators. Dangerous temperatures can trigger warning overlays, sounds, and secondary effects. Heat-driven thirst and Heat Stroke are implemented, as are frostbite warnings and Cold Hunger.

v2.8.1 fixed the opaque white/yellow freezing and heat overlays by enabling alpha blending. The user confirmed the fix in-game; v3.0.0 includes it.

Continuous Cold Hunger is managed by dangerous cold or storm exposure. Finite effects, including freezing-wind hits, retain their duration instead of being removed by normal-temperature cleanup.

Death respawn grants configured temperature immunity (90 seconds by default), including across dimension transitions.

The Thermometer helps inspect world temperature. Environmental/target temperature is different from your current body temperature: protection and consumables affect how the survival system responds over time. Configure the units, HUD and warnings in Client; balance the mechanic in Temperature and Seasons.

## Wetness, Nether lilies, fur and skates (v3.2.0)

These features are included in **v3.2.0** and do not require upstream mods.

Wetness still builds from fluids/rain/cauldrons and lowers the temperature
target in proportion to saturation (default `-10` fully soaked). Cooling
steps are now `1.5x` faster at any nonzero wetness in all biomes; configure
`wetCoolingRateMultiplier` separately from `wetMultiplier`. Warming is not
accelerated. A hot environment can still warm a wet player if the adjusted
target is above their current body temperature. Snowstorm cooling combines
multiplicatively with wetness (`3x` by default).

Crimson/warped lilies generate on nylium in newly generated crimson/warped
forests. Harvest and replant them on supported ground. They store three
water levels and refill gradually; in the Nether/other ultra-warm dimensions
they require nylium or rooted dirt below them (datapack tag
`legendarysurvivaloverhaul:nether_lily_can_absorb_water`). Elsewhere they
refill on any supported ground. Full flowers drip.

A full Crimson Lily bursts on contact, fully wetting players and damaging
water-sensitive tagged mobs, then empties. Refill it with a water bottle or
one canteen serving. A full Warped Lily gives one normal-water bottle or
canteen serving when harvested, then empties. Non-water potions are rejected;
full canteens are not wasted. Purity follows the canteen's normal fill rule.
These are LSO plants, not a requirement to install Scorchful or Thermoo.
Scorchful's additional rooted blocks, water skins and farm structures are
not ported; LSO uses natural forest patches instead.

The existing winter suit now uses Frostiful's **non-armored fur** appearance
and English names. Its `snow_*` IDs, recipes, repair material, enchantability,
durability and armor/cold values remain unchanged, so old equipped items and
datapacks continue to work. This is not the fur-padded chainmail variant.

Craft Ice Skates from Fur Boots and two iron ingots. They retain the boots'
armor/cold protection and add visible blades. This is ordinary crafting:
input boot damage, coatings and enchantments are not transferred.
Ice/packed ice/blue ice allow
gliding; sprint to accelerate and sneak to brake. Walking on non-ice ground
is 50% slower; removing skates or leaving the ground clears the penalty.
Landing on another living entity with the blades deals one point of damage.
No Frostiful or Thermoo dependency is added.

**Added in v3.2.2:** modded ice support covers `IceBlock` subclasses such as
Immersive Weathering's Thin Ice, plus common ice tags and LSO's extensible
`legendarysurvivaloverhaul:skateable_ice` block tag. Both gliding and the
off-ice penalty use the same recognition. Thin ice can still crack, break
and melt normally.

## Hydration

Hydration and saturation are tracked alongside food. Use drinking, purification, canteens, and configured consumables. Jumping, successful block breaking, and attacking can consume hydration according to settings; Creative/Spectator are excluded from these tested exhaustion paths.

Consumable tooltips show hydration/saturation and effects. Low hydration can apply configurable vision blur. The Water Purifier blocks the LSO Thirst effect when detected in either hand or a Trinkets slot.

Blocking the Thirst effect is not an unlimited hydration refill. Use the drinking/purification tools and verify the tooltip for the actual item, especially in modpacks with changed consumable data.

**Canteen enchantments (v3.0.0):** Refreshing, Purity, and Reservoir apply only to Canteens and Large Canteens. Find enchanted books in loot and apply them with an anvil; these enchantments are not offered by enchanting tables or villager trades. Their existing levels and effects are unchanged.

## Localized body damage

Damage can affect individual body parts and trigger configured secondary effects. Open the limb-health screen with **H** by default, or rebind it in Controls. Healing items can target injuries; First Aid Supplies held or equipped are recognized.

Body-part health and effects are configurable. Data-driven damage-source rules and equipment resistance complement the settings. Projectile headshots and the Vulnerability/Hard Falling adjustments were tested in-game.

Check which part is injured before applying treatment. First Aid Supplies provides held/equipped support through the existing healing system; it does not make all injuries or secondary effects disappear instantly.

## Health

The health overhaul supports additional, broken, resilient, permanent, and shield hearts. When enabled/configured, Absorption converts to shield health instead of applying vanilla Absorption. Shields absorb damage before remaining player/body-part damage.

Broken hearts occupy health containers; shield hearts use separate yellow/orange rows and reserve space above them for armor. Optional Overflowing Bars spacing was tested. Sleeping can restore configured player and body-part health.

**Enchanted Golden Apple and death (inspired by [HardcoreLite](https://github.com/MC-Mods-Pete/HardcoreLite/tree/1.20.1-Fabric)):** eating specifically an Enchanted Golden Apple grants a Shield Health bonus (2 Shield Hearts by default) and repairs a Heart Container, on top of its vanilla effects. Dying removes one Heart Container, down to a configurable floor (1 Heart Container / 2 health by default) — LSO never forces Spectator mode at that floor, unlike the mod that inspired it. If LevelZ is installed, its level-based max-health bonus takes precedence: a LevelZ character's level-based health floor is never lowered by LSO's death heart-loss. Tune or disable both mechanics in **LSO - Health** (`enchantedGoldenAppleOverrideEnabled`, `heartsLostOnDeath`, `permanentHearts`); see [Mod Compatibility](Mod-Compatibility) for full field-by-field behavior.

## Equipment and survival blocks

Wearable accessory slots use the strongly recommended, optional Trinkets integration. Without Trinkets there are no accessory slots or auto-equip, while supported held-item effects remain available. Vanilla equipment and equipped LSO accessories can receive datapack-driven temperature and body-part resistance.

The sewing table supports warm/cold string recipes and coat application to armor. Heater/chiller fuel operation and fuel persistence across save/reload were tested; heater multiblock drops were also checked.

For breathing equipment and air hazards, see [Air Quality and Breathing](Air-Quality-and-Breathing).

For the eight configurable accessories and their defaults, see [Trinkets and Accessories](Trinkets-and-Accessories). Armor resistance, hydration values and damage distribution can be changed by [datapacks](Datapack-Customization) without changing base recipes.

## Customize difficulty and presentation

[Configuration](Configuration) explains all nine TOML files and their optional Fzzy editor screens. Use Client for local presentation and the mechanic-specific settings for balance. On a server, server-owned settings determine gameplay; a local Client HUD preference does not change server difficulty.

Test one change at a time. An effect that is temperature-managed may disappear when its triggering condition ends; command duration alone is not a reliable mechanic test.
