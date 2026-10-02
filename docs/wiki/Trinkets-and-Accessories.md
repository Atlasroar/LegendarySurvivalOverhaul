# Trinkets and Accessories

LSO uses [Trinkets](https://modrinth.com/mod/trinkets) for accessory inventory slots. These are separate from vanilla armor/hand slots.

![Upstream Trinkets accessory inventory](https://cdn.modrinth.com/data/5aaWibi9/images/56224f13887cdd914a9624a18eb845dae0bcdfc0.png)

*“Trinkets UI” image from the [Trinkets Modrinth gallery](https://modrinth.com/mod/trinkets), credited to the Trinkets project. It illustrates group/slot navigation; it is not a screenshot of LSO's config editor or a promise that every depicted slot is populated.*

## LSO accessories

| Item | Default location | Role |
| --- | --- | --- |
| Thermometer | `legs/belt` | Temperature information/display integration |
| Nether Chalice | `chest/necklace` | Allows lava drinking through the supported thirst interaction |
| Sponge | `chest/back` | Prevents wetness accumulation when detected held/equipped |
| Heat Resistance Ring | `hand/ring` | Heat resistance |
| Cold Resistance Ring | `hand/ring` | Cold resistance |
| Thermal Resistance Ring | `hand/ring` | Thermal resistance |
| First Aid Supplies | `hand/glove` | Body-part recovery support |
| Water Purifier | `head/face` | Blocks the LSO Thirst effect when detected held/equipped |
| Respirator | `head/face` | Protection against YELLOW air; **excluded from configurable slot lists** |

Actual modifiers and behavior depend on configuration and datapack data. Moving an accessory does not automatically increase its power or available capacity.

## Equip and relocate

Use the accessory inventory UI or right-click auto-equip where supported by the item. The Thermometer displays its temperature reading and then delegates to normal Trinkets item use, retaining auto-equip.

v2.2.0 adds [LSO - Trinkets](Trinkets-Configuration), allowing server-authorized changes to the first eight items' allowed slots. Defaults match the table. Multiple entries allow different locations; empty lists prevent new equipping.

An edited list does not remove or move an item already equipped. Take it out and reinsert to test the new assignment. Normal restrictions such as Binding Curse still apply. An empty list does not make an equipped item disappear.

## Understanding slot names

`hand/ring` means group `hand`, slot `ring`. It is not a vanilla `EquipmentSlot` enum. A player must already have the slot defined and assigned.

LSO requests these player slot types: `chest/back`, `chest/necklace`, `feet/aglet`, `feet/shoes`, `hand/glove`, `hand/ring`, `head/face`, `head/hat`, `legs/belt`. Capacities come from Trinkets slot definitions and other mods/modifiers.

For creating custom slots, use the [developer recipes](Datapack-Air-and-Slot-Recipes). For insertion validation and modifier lifecycle, see [Trinkets Integration Internals](Trinkets-Integration-Internals).
