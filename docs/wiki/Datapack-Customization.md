# Datapack Customization

LSO combines [in-game configuration](Configuration) with server datapack data. Config switches and numeric settings do not replace the item, block, biome, dimension, and damage-source datasets.

## Data-driven surfaces

Runtime data covers temperature consumables/blocks/items/biomes/dimensions/fuels/mounts, hydration consumables and blocks, localized damage sources, healing consumables, equipment resistance, and air-quality dimension profiles.

Inspect the existing default JSON for the exact schema before creating an override. Do not invent field names or carry Forge-only paths into a Fabric datapack without checking the runtime listener.

Default resources and implementation:

- [Generated data](https://github.com/Atlasroar/LegendarySurvivalOverhaul/tree/lso-fabric-1-20-1/src/generated/resources/data)
- [Main resources](https://github.com/Atlasroar/LegendarySurvivalOverhaul/tree/lso-fabric-1-20-1/src/main/resources)
- [Equipment resistance test datapack](https://github.com/Atlasroar/LegendarySurvivalOverhaul/tree/lso-fabric-1-20-1/test-datapacks/equipment-resistance-test)

## Reload and synchronization

Fabric server-data reload listeners load the survival datasets. Player joins and successful datapack reloads synchronize the survival data to clients. Air-quality profiles may be provided under custom datapack namespaces.

Use Minecraft 1.20.1-compatible datapack metadata. Test one override at a time, run `/reload` with appropriate permission, inspect server logs for parsing errors, and verify the actual in-game behavior. A successful reload does not prove a modifier was applied to the intended item or dimension.

Back up worlds/configs before testing. For equipment resistance, compare equipped and unequipped behavior and check that modifiers disappear after removal.

## Build workflow distinction

The Fabric build packages checked-in generated resources. Forge datagen task execution is intentionally omitted; this does not disable runtime datapack loading.
