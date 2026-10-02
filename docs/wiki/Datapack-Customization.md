# Datapack Customization

Datapacks customize survival values for existing registry content without recompiling LSO. They do not replace the config editor, create arbitrary item interactions or add texture assets.

## Minimal Minecraft 1.20.1 pack

Create `MySurvivalPack` inside the world's `datapacks` folder. Its root contains `pack.mcmeta`:

```json
{
  "pack": {
    "pack_format": 15,
    "description": "Survival balance overrides for LSO 2.2.0"
  }
}
```

For a zip, `pack.mcmeta` must be at the archive root, not inside another enclosing folder.

```text
MySurvivalPack/
  pack.mcmeta
  data/
    minecraft/
      legendarysurvivaloverhaul/
        thirst/consumables/apple.json
        temperature/items/diamond_helmet.json
        air_quality/dimensions/overworld.json
```

## Resource IDs, not pack IDs

The general pattern is:

```text
data/<target_namespace>/legendarysurvivaloverhaul/<category>/<dataset>/<target_path>.json
```

For `minecraft:apple`, use the minecraft namespace and apple filename. For `examplemod:gear/helmet`, use `data/examplemod/legendarysurvivaloverhaul/temperature/items/gear/helmet.json`. Most listeners filter namespaces to installed mods.

For an LSO item the repeated name is correct:

```text
data/legendarysurvivaloverhaul/legendarysurvivaloverhaul/temperature/items/thermal_resistance_ring.json
```

The first name is the item's namespace; the second is the listener directory. Using your pack's branding namespace instead changes the looked-up ID and usually will not affect the intended item.

## Recipe 1: apple hydration

`data/minecraft/legendarysurvivaloverhaul/thirst/consumables/apple.json`

```json
[
  {
    "hydration": 2,
    "saturation": 0.5,
    "effects": [],
    "properties": {}
  }
]
```

The root is an array, even for one variant. Consume an apple while below maximum hydration. A full bar can hide the change.

## Recipe 2: cold-resistant helmet

`data/minecraft/legendarysurvivaloverhaul/temperature/items/diamond_helmet.json`

```json
{
  "temperature": 0.0,
  "heat_resistance": 0.0,
  "cold_resistance": 2.0,
  "thermal_resistance": 0.0
}
```

This root is an object. Compare equipped versus unequipped behavior in cold conditions; body-temperature changes take time. Resistance units are attribute modifiers, not necessarily a fixed reduction in displayed Celsius.

To also change localized head resistance, add the **separate** entry:

`data/minecraft/legendarysurvivaloverhaul/body_damage/items/diamond_helmet.json`

```json
{"head_resistance": 2.0}
```

Both listeners can target the same item independently.

## Recipe 3: Overworld air profile

`data/minecraft/legendarysurvivaloverhaul/air_quality/dimensions/overworld.json`

```json
{
  "base_quality": "yellow",
  "bounded_qualities": [
    {"quality": "green", "min": 0, "max": 256}
  ]
}
```

This includes eye-block Y=0..255, not Y=256. Leave Air's vanilla dimension override switch off so the profile is used. Water/nearby providers can take precedence.

See [Air and Slot Recipes](Datapack-Air-and-Slot-Recipes) for custom dimensions, provider tags, protective equipment, and custom Trinkets slots.

## Reload and verify

1. Back up the world/config and add the pack.
2. Run `/datapack list` and enable the pack if needed.
3. Run `/reload`; inspect the server log for parse errors and dataset loaded counts.
4. Test the exact target while the related feature is enabled and your state permits a measurable change.
5. Rejoin with a separate client if the change depends on client queries/UI.

Use server operator permissions for commands. Restart when changing a startup-only config or when testing full join initialization.

## Priority and conflicts

Use `/datapack list` to inspect competing packs. The highest-priority file for the same target/resource path wins in ordinary LSO JSON listeners; array entries do not merge between those files.

Vanilla tags use their own `replace` flag. Trinkets slot/entity definitions merge using Trinkets-specific rules. Configurable LSO accessory acceptance takes precedence over tags while Use Configured Slots is enabled.

## Frequent mistakes

| Symptom | Check |
| --- | --- |
| No change, no parse error | Target namespace/path, installed mod and actual consumer/use hook |
| Parse failure | Object versus root array; required fields; numbers/booleans versus quoted strings |
| Temperature block variant ignored | Correct state property names and string values |
| Consumable NBT variant ignored | Flat matching only; not nested SNBT |
| Air profile ignored | Config override, Nether fallback, actual dimension ID and provider priority |
| Helmet effect persists unexpectedly | Test removal/reload and server-tick reconciliation, not just tooltip text |
| Server works but client display differs | Dataset sync coverage; do not assume all 14 listeners synchronize |
| Accessory tag ignored | Use Configured Slots may own acceptance instead |

## Further reference

[Schema Reference](Datapack-Schema-Reference) lists all 14 listener directories, root shapes, fields, defaults and important semantic limits. [Architecture](Architecture-and-APIs) explains reload/networking separation.

Source examples are under [`src/generated/resources/data`](https://github.com/Atlasroar/LegendarySurvivalOverhaul/tree/lso-fabric-1-20-1/src/generated/resources/data) and [`src/main/resources/data`](https://github.com/Atlasroar/LegendarySurvivalOverhaul/tree/lso-fabric-1-20-1/src/main/resources/data). Always use the resource and codec from the release you target.
