# Datapack Schema Reference

These are the **v2.3.0 Fabric 1.20.1** codecs in `api/data/json`, not examples for the Forge original. Start with [Datapack Customization](Datapack-Customization) for pack structure and target IDs.

## Dataset directory index

Paths below follow `data/<target_namespace>/legendarysurvivaloverhaul/`. A filename such as `items/diamond_helmet.json` identifies the registry path `diamond_helmet` in that target namespace.

| Directory | Target | JSON root | Codec |
| --- | --- | --- | --- |
| `temperature/biomes` | Biome ID | object | `JsonTemperatureBiomeOverride` |
| `temperature/blocks` | Block, fluid or entity ID queried by temperature code | array | `JsonTemperatureBlock.LIST_CODEC` |
| `temperature/consumables` | Item ID | array | `JsonTemperatureConsumable.LIST_CODEC` |
| `temperature/consumable_blocks` | Block ID | array | `JsonTemperatureConsumableBlock.LIST_CODEC` |
| `temperature/dimensions` | Dimension ID | object | `JsonTemperatureDimension` |
| `temperature/fuel_items` | Item ID | object | `JsonTemperatureFuelItem` |
| `temperature/items` | Item ID | object | `JsonTemperatureResistance` |
| `temperature/mounts` | Entity type ID | object | `JsonTemperatureResistance` |
| `thirst/blocks` | Block or fluid ID | array | `JsonThirstBlock.LIST_CODEC` |
| `thirst/consumables` | Item ID | array | `JsonThirstConsumable.LIST_CODEC` |
| `body_damage/consumables` | Healing item ID | object | `JsonHealingConsumable` |
| `body_damage/damage_sources` | Damage-source message ID path | object | `JsonBodyPartsDamageSource` |
| `body_damage/items` | Equipment item ID | object | `JsonBodyPartResistance` |
| `air_quality/dimensions` | Dimension ID | object | `JsonAirQualityDimension` |

Most listeners load a namespace only if its mod is present; `minecraft` is available through Fabric Loader. Fuel explicitly accepts `minecraft` as well. **Biome temperature overrides and air dimension profiles accept datapack-only namespaces** without a matching installed mod. The resource key must still equal the actual target biome/dimension ID.

Files at the same exact resource ID follow resource-pack priority. Arrays are the entire dataset entry for one target; they are not automatically appended across packs. Vanilla tags and Trinkets slot/entity data have their own merge behavior.

## Temperature: equipment and mounts

`JsonTemperatureResistance`: all fields optional floats, default `0.0`.

| Field | Meaning |
| --- | --- |
| `temperature` | Added heating/cooling modifier |
| `heat_resistance` | Heat resistance modifier |
| `cold_resistance` | Cold resistance modifier |
| `thermal_resistance` | General thermal resistance modifier |

```json
{
  "temperature": 0.0,
  "heat_resistance": 0.0,
  "cold_resistance": 2.0,
  "thermal_resistance": 0.0
}
```

Equipment numbers are modifier magnitudes, not a guarantee of a fixed number of degrees of protection. Mount entries use the **same four-field codec**, not a one-field `JsonTemperature` assumption.

## Temperature: environmental sources

`JsonTemperatureBlock`: root array of objects with required float `temperature`, optional `properties` string-to-string map (default `{}`).

```json
[
  {"temperature": 8.0, "properties": {"lit": "true"}},
  {"temperature": 0.0, "properties": {"lit": "false"}}
]
```

An example path is `data/minecraft/legendarysurvivaloverhaul/temperature/blocks/campfire.json`. Specificity is selected by consuming manager/query code; do not depend on unsupported property keys producing rigorous validation errors.

`JsonTemperatureBiomeOverride`: required float `temperature` and boolean `is_dry`.

```json
{"temperature": 30.0, "is_dry": true}
```

`JsonTemperatureDimension`: required float `temperature`; optional integer `sea_level_height` (64), boolean `has_altitude` (true), and integer **`temperatureTimeCycleTicks`** (0, clamped nonnegative).

```json
{
  "temperature": 20.0,
  "sea_level_height": 64,
  "has_altitude": true,
  "temperatureTimeCycleTicks": 24000
}
```

The camelCase key is intentional. Do not rename it to snake_case.

## Temperature: consumed items and blocks

`JsonTemperatureConsumable`: root array. Each object requires `group` (`FOOD` or `DRINK`), integer `temperature_level`, and integer `duration` (ticks).

```json
[
  {"group": "FOOD", "temperature_level": -1, "duration": 1200}
]
```

Positive levels heat, negative levels cool. Use a nonzero level: the constructor assigns effect objects only for positive/negative values.

`JsonTemperatureConsumableBlock` has the same fields plus optional `properties` string-to-string map, default `{}`:

```json
[
  {
    "group": "FOOD",
    "temperature_level": -1,
    "duration": 1200,
    "properties": {"bites": "0"}
  }
]
```

The item/block must actually reach a supported consumption hook. Merely giving an arbitrary non-consumable item a JSON entry does not create new use behavior.

## Temperature: fuel

`JsonTemperatureFuelItem`: required `thermal_type` string and integer `duration`. `ThermalTypeEnum.get` accepts `HEATING`, `COOLING`, and `BROKEN` case-insensitively. HEATING/COOLING are the useful fuel types; BROKEN is the neutral internal state.

```json
{"thermal_type": "HEATING", "duration": 1200}
```

Use positive durations in ticks. The codec does not make every syntactically valid integer meaningful gameplay.

## Thirst: items, blocks and fluids

`JsonThirstConsumable` and `JsonThirstBlock` both use a **root array**. Each entry requires integer `hydration` and float `saturation`. Optional `effects` defaults to `[]`; optional `properties` defaults to `{}`.

```json
[
  {
    "hydration": 2,
    "saturation": 0.5,
    "effects": [
      {
        "effect": "minecraft:hunger",
        "duration": 200,
        "chance": 0.25,
        "amplifier": 0
      }
    ],
    "properties": {}
  }
]
```

`JsonMobEffect` requires string `effect` (registered effect ID) and integer `duration` in ticks; optional float `chance` defaults to 1.0 and clamps to [0,1]; optional integer `amplifier` defaults to 0 and clamps nonnegative. Amplifier zero is effect level I.

For consumables, properties match flat item NBT values; numeric values are compared as numbers and other values as strings. This is **not nested SNBT matching**. Block/fluid properties use state matching. Include a `{}` fallback if all variants should have a default; the consumer chooses the applicable variant.

## Body damage: resistance

`JsonBodyPartResistance`: optional floats, all default zero:

```json
{
  "body_resistance": 0.0,
  "head_resistance": 2.0,
  "chest_resistance": 0.0,
  "right_arm_resistance": 0.0,
  "left_arm_resistance": 0.0,
  "legs_resistance": 0.0,
  "feet_resistance": 0.0
}
```

These are resistance modifiers. Equipment reconciliation removes/reapplies changes; they are not extra body-part health.

## Body damage: healing

`JsonHealingConsumable` requires integer `healing_charges`, float `healing_value`, integer `healing_time`; optional integer `recovery_effect_duration` and `recovery_effect_amplifier` default zero.

```json
{
  "healing_charges": 1,
  "healing_value": 2.0,
  "healing_time": 200,
  "recovery_effect_duration": 200,
  "recovery_effect_amplifier": 0
}
```

Healing/recovery times are ticks. Keep healing time positive. Data configures an existing supported healing-use pathway; it is not a generic registration of arbitrary item right-click behavior.

## Body damage: damage distribution

`JsonBodyPartsDamageSource` requires string `damage_distribution` and array of string `body_parts`.

```json
{
  "damage_distribution": "ONE_OF",
  "body_parts": ["HEAD", "CHEST"]
}
```

Distribution values are `NONE`, `ONE_OF`, `ALL`. ONE_OF randomly selects a listed part and requires a nonempty list. Valid part names: `HEAD`, `RIGHT_ARM`, `LEFT_ARM`, `CHEST`, `RIGHT_LEG`, `RIGHT_FOOT`, `LEFT_LEG`, `LEFT_FOOT`.

The manager matches the resource key's **path** against the damage source's message ID, not the full damage-type registry key. Most namespaces still require a loaded mod. Avoid ambiguous duplicate message-ID paths in different namespaces.

## Air dimension profiles

`JsonAirQualityDimension`: optional lower-case `base_quality` (default `green`), optional `bounded_qualities` array (default `[]`).

Each bound requires `quality` (`green`, `blue`, `yellow`, `red`); integer `min` and `max` are optional, defaulting to integer minimum/maximum.

```json
{
  "base_quality": "yellow",
  "bounded_qualities": [
    {"quality": "green", "min": 0, "max": 256},
    {"quality": "red", "min": -64, "max": 0}
  ]
}
```

Bounds are `[min,max)`, and `max` must exceed `min`. First matching bound wins. Otherwise `base_quality` applies. Config overrides, Nether's fallback, eye fluids and nearby providers can take precedence; see [Air Internals](Air-System-Internals).

## Synchronization limitations

Reload registration and network sync are different things. This release registers all 14 listeners, sends 12 datasets in `FabricDataSyncHandler.syncAll`, and registers 13 client receivers. Temperature consumable blocks have a receiver but are omitted by that send method. Air profiles have no dedicated dataset sync message.

Server evaluation of air still occurs, but do not claim identical remote client queries for a server-only custom profile or blanket sync parity for all datasets. Include a separate-client test when building packs that depend on client-visible state.

## Source of truth

Read [the codecs](https://github.com/Atlasroar/LegendarySurvivalOverhaul/tree/lso-fabric-1-20-1/src/main/java/sfiomn/legendarysurvivaloverhaul/api/data/json) and [listeners](https://github.com/Atlasroar/LegendarySurvivalOverhaul/tree/lso-fabric-1-20-1/src/main/java/sfiomn/legendarysurvivaloverhaul/common/listeners) together with their consumers. Codecs define shape, not every semantic constraint. JSON can parse successfully and still be unusable because a registry ID, mod, matching state or consumption hook is absent.

[Getting started](Datapack-Customization) | [Air/slot recipes](Datapack-Air-and-Slot-Recipes) | [Developer Hub](Developer-Hub)
