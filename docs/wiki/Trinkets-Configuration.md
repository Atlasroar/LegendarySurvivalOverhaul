# Trinkets Configuration

**LSO - Trinkets**, introduced in v2.2.0, provides nine server-synchronized settings in `config/legendarysurvivaloverhaul/trinkets.toml`: one mode switch and eight item-specific slot lists.

## Defaults

| Field | Item | Default list |
| --- | --- | --- |
| `useConfiguredSlots` | All eight configurable accessories | `true` |
| `thermometerSlots` | Thermometer | `["legs/belt"]` |
| `netherChaliceSlots` | Nether Chalice | `["chest/necklace"]` |
| `spongeSlots` | Sponge | `["chest/back"]` |
| `heatResistanceRingSlots` | Heat Resistance Ring | `["hand/ring"]` |
| `coldResistanceRingSlots` | Cold Resistance Ring | `["hand/ring"]` |
| `thermalResistanceRingSlots` | Thermal Resistance Ring | `["hand/ring"]` |
| `firstAidSuppliesSlots` | First Aid Supplies | `["hand/glove"]` |
| `waterPurifierSlots` | Water Purifier | `["head/face"]` |

The **Respirator has no field** and retains its existing Trinkets/datapack rules.

## Edit lists in-game

Open LSO - Trinkets using Mod Menu or `/configure legendarysurvivaloverhaul`. Edit a list, add/remove entries, and save/apply.

Entries are lowercase `group/slot` strings matching:

```text
[a-z0-9_.-]+/[a-z0-9_.-]+
```

Examples: `hand/ring`, `chest/back`, `head/hat`. Do not enter `trinkets:hand/ring`, `HAND`, or an item registry ID.

Multiple entries permit multiple locations; they do not duplicate the item. Duplicate IDs are equivalent to one allowed location. Empty lists deliberately disable new insertion for that accessory.

Example TOML, edited only while the instance is stopped:

```toml
useConfiguredSlots = true
thermometerSlots = ["legs/belt", "hand/ring"]
waterPurifierSlots = ["chest/necklace"]
firstAidSuppliesSlots = []
```

## Modes and priority

With `useConfiguredSlots = true`, the configured lists replace these eight items' tag-based slot acceptance. Other slot predicates and the item's equip restrictions remain relevant; a custom slot that never evaluates a compatible predicate may still reject insertion.

With `false`, LSO leaves the original `trinkets:tag` predicate and datapack assignments active. Use this when a pack should own slot assignments through tags.

The server's synchronized config governs players. Changing your local file on someone else's server does not grant new equip permissions.

## Existing items, effects and limitations

- Editing rules does not relocate, delete or force-unequip existing items.
- Normal removal restrictions remain, including Binding Curse.
- Configuring an allowed slot does not create it, allocate capacity, or assign it to the player.
- Custom slots from other mods/datapacks are supported if they exist and their validators permit the item.
- Missing player slot definitions generate server warnings. Confirm exact case and slot/group spelling.
- Effects continue through existing equipped-item lookup/modifier paths; re-equip and check behavior after changing an assignment.

For developers: [Trinkets Integration Internals](Trinkets-Integration-Internals). For new slots/tags: [Air and Slot Recipes](Datapack-Air-and-Slot-Recipes).
