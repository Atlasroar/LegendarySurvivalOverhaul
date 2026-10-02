# Datapack Air and Slot Recipes

Create a [Minecraft 1.20.1 datapack](Datapack-Customization) with `pack_format: 15`. Paths below are inside its root. These are illustrative pack changes, not additional defaults shipped with LSO.

## Custom dimension profile

For dimension `example:highlands`, create:

`data/example/legendarysurvivaloverhaul/air_quality/dimensions/highlands.json`

```json
{
  "base_quality": "yellow",
  "bounded_qualities": [
    {"quality": "green", "min": 0, "max": 128},
    {"quality": "blue", "min": 128, "max": 192}
  ]
}
```

This makes eye-block heights 0..127 GREEN, 128..191 BLUE, and everything else YELLOW, absent a higher-precedence eye-fluid/provider result. It does **not** create the dimension itself.

For `minecraft:overworld`, use `data/minecraft/.../dimensions/overworld.json`. Leave Override Vanilla Dimension Profiles off if the datapack should control it. Nether remains hardcoded YELLOW when that switch is off; use the Air config's opt-in Nether override for a different ambient result.

At boundary tests, remember the player's eye block differs from feet Y. Overlapping bounds use first match; order deliberately.

## Add a clean-air provider

`data/legendarysurvivaloverhaul/tags/blocks/green_air_providers.json`

```json
{
  "replace": false,
  "values": ["minecraft:lantern"]
}
```

The GREEN provider radius comes from Air config. This tag grants provider behavior to the block; it does not change its recipe, light level or model. If a candidate also belongs to a more hazardous provider tag/overlapping source, review [lookup priority](Air-System-Internals).

Use `blue_air_providers`, `yellow_air_providers`, or `red_air_providers` for other qualities. Prefer `replace: false` to retain shipped providers.

## Make an existing helmet protective

Add the item to both tags for YELLOW and RED protection:

`data/legendarysurvivaloverhaul/tags/items/breathing_equipment.json`

```json
{"replace": false, "values": ["minecraft:diamond_helmet"]}
```

`data/legendarysurvivaloverhaul/tags/items/heavy_breathing_equipment.json`

```json
{"replace": false, "values": ["minecraft:diamond_helmet"]}
```

This is equipment protection, not automatic GREEN air. It prevents hazardous drain and follows the configured durability interval. Tags alone do not turn a normal armor item into a Trinkets item or register a new wearable API implementation.

## Add an entity to breathing evaluation

`data/legendarysurvivaloverhaul/tags/entity_types/air_quality_sensitive.json`

```json
{"replace": false, "values": ["minecraft:villager"]}
```

This extends the custom air tick eligibility. Test mob air/drowning behavior and server load; it does not add a player-style HUD to that entity.

## Add a custom Trinkets slot

The example adds **chest/lso_charm** to the existing chest group, avoiding a new group's layout decisions.

`data/trinkets/slots/chest/lso_charm.json`

```json
{
  "amount": 1,
  "order": 50,
  "icon": "trinkets:gui/slots/necklace",
  "validator_predicates": ["trinkets:tag"]
}
```

`icon` identifies a texture without `textures/` or `.png`; Trinkets adds both. This example reuses its standard necklace icon. New textures require a client resource pack/mod, not just server datapack JSON.

Assign the type to players:

`data/trinkets/entities/lso_charm_players.json`

```json
{
  "replace": false,
  "entities": ["minecraft:player"],
  "slots": ["chest/lso_charm"]
}
```

Use the **trinkets namespace** for both files: its loaders filter to that namespace. The entity filename is descriptive, whereas group/slot filename determines the slot ID.

The inspected 1.20.1 entity loader does not implement entity-tag assignments even though parsing recognizes `#...`; use explicit entity IDs.

## Choose who owns acceptance

With Use Configured Slots on, add `chest/lso_charm` to the chosen LSO item's list in **LSO - Trinkets**. For example, allow Water Purifier in `head/face` and `chest/lso_charm`. The wrapper supplies acceptance without needing an item tag for that configurable item.

With Use Configured Slots off, create the slot tag:

`data/trinkets/tags/items/chest/lso_charm.json`

```json
{
  "replace": false,
  "values": ["legendarysurvivaloverhaul:water_purifier"]
}
```

This is also appropriate for other tag-based items. For the eight configured LSO accessories, tags do **not** override an active configured list. Respirator remains outside that list system.

Adding this tag opens the new slot; it does not remove the item's membership in any old slot tag. To move rather than broaden tag-based acceptance, adjust old tags deliberately and retain other mods' members.

## Merge and capacity cautions

Trinkets slot resources merge across packs. Without `replace`, `amount` uses the maximum contributed amount, not an additive sum. Group properties include `slot_id` and `order`; slot properties include `amount`, `order`, `icon`, `validator_predicates`, `quick_move_predicates`, `tooltip_predicates`, and `drop_rule`.

`replace` affects specific upstream merge operations; it is not a universal “erase all slot data” switch. Read [SlotLoader](https://github.com/emilyploszaj/trinkets/blob/1.20.1/src/main/java/dev/emi/trinkets/data/SlotLoader.java) before reducing capacities or replacing predicate sets. A non-tag validator can still reject a configured LSO item.

## Verification

Run `/reload`, inspect logs for unknown slot/group IDs and LSO slot warnings, then reopen inventory. Test direct insertion, right-click equipment, quick-move and removal. Test with configured slots enabled/disabled and after reload/rejoin.

Changing an LSO allowed-slot list preserves already equipped stacks. Reducing/deleting **actual Trinkets slot capacity** is different and requires separate retention/drop testing. Back up inventories/worlds first.

[Trinkets config](Trinkets-Configuration) | [Trinkets internals](Trinkets-Integration-Internals) | [Schema reference](Datapack-Schema-Reference)
