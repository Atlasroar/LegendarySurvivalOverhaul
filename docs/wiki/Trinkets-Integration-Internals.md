# Trinkets Integration Internals

v3.0.0 optionally integrates with Trinkets 3.7.2 for Minecraft 1.20.1. The upstream [1.20.1 source](https://github.com/emilyploszaj/trinkets/tree/1.20.1) uses Yarn mappings; snippets here use the port's Mojang mappings.

## Registration and item types

`WearableTrinketItem` extends vanilla `Item`, so content registration does not load Trinkets classes. Thermometer and Nether Chalice inherit this class; the resistance rings, Sponge, First Aid Supplies and Water Purifier also use it. After content registration, a presence-guarded `TrinketsIntegration.register()` attaches a `WearableTrinket` adapter to these items and installs the slot predicate wrapper.

The Respirator is registered as a plain `Item` and is not in the configurable eight-item map. Its existing tag/renderer behavior remains untouched.

Slot and entity data are separate:

- `data/trinkets/slots/<group>/<slot>.json` defines type, capacity, icon and predicates.
- `data/trinkets/entities/<file>.json` assigns types to entities.
- `data/trinkets/tags/items/<group>/<slot>.json` lists items accepted by the `trinkets:tag` predicate.

Configuring a string in `trinkets.toml` changes none of those definitions.

## Why overriding canEquip alone is insufficient

Upstream `TrinketSlot.canInsert` checks the slot's validator predicates first, then asks the item's `canEquip`. If the item is not in the new slot's tag, a `canEquip` override alone cannot open that slot.

LSO therefore wraps the existing registered `trinkets:tag` predicate after content registration:

```java
TrinketsApi.registerTrinketPredicate(tagId, (stack, slot, entity) -> {
    Set<String> allowed = configuredSlots(stack);
    return allowed == null
            ? original.apply(stack, slot, entity)
            : TriState.of(allowed.contains(slotId(slot)));
});
```

This excerpt is from `TrinketSlotConfig`. For unconfigured items the original predicate is invoked. For configured LSO items the list can grant the new slot or deny the old tag-based slot.

`WearableTrinket.canEquip` additionally enforces the same map, preserving interface defaults:

```java
return Trinket.super.canEquip(stack, slot, entity)
        && TrinketSlotConfig.canEquip(stack, slot);
```

This restriction matters for slots with other validators such as `trinkets:all`. Other predicate ordering/semantics remain Trinkets' responsibility. A custom slot that rejects before accepting the item is not overridden by an LSO config list.

## Baked data and updates

`Config.Baked.trinketSlots` is a **volatile immutable map** from item `ResourceLocation` to a set of `group/slot` IDs. Baking copies validated lists into sets and replaces the map in one assignment.

`useConfiguredSlots = false` publishes an empty map; configured lookup returns null and the original tag behavior resumes. An empty **item set** is different from an empty **map**: it intentionally rejects every new slot for that item.

Lists validate syntax with `[a-z0-9_.-]+/[a-z0-9_.-]+`. This permits custom names but does not prove the slot exists. Startup, successful server reload and config updates warn about requested IDs absent from player slot definitions.

LSO-owned config synchronization drives client insertion checks with or without the optional Fzzy editor. Fzzy registration remains `RegisterType.BOTH` when installed. Actual remote-client permissions/synchronization should be tested rather than inferred from single-player.

## Equip paths

`TrinketItem.equipItem` scans empty accessory inventories and calls `TrinketSlot.canInsert`. The same predicate API is available to quick-move checks. Manual insertion and right-click auto-equip therefore share the configured acceptance surface.

The Thermometer's override displays a temperature message, then calls `super.use`, retaining the equip path. When adding another specialized accessory, preserve whichever superclass behavior you intend rather than assuming an overridden `use` auto-equips.

## Effects and attribute modifiers

`WearableTrinket.getModifiers` uses `TemperatureDataManager.getItem(itemId)` and `BodyDamageDataManager.getBodyResistanceItem(itemId)`. It adds temperature and resistance `AttributeModifier`s using Trinkets' per-slot UUID.

Vanilla equipment has a separate server-tick reconciliation path in `FabricEquipmentAttributeHooks`, using persistent per-equipment-slot UUIDs and removing modifiers when gear/data/config no longer apply.

v3.2.4 caches built-in temperature calculations per
player/equipment slot. Full copied stacks detect item, count, damage,
enchantment and other NBT changes, including legacy coat-tag migration.
Live temperature-data values and the current coat strength detect datapack,
sync, config and in-place resistance-data changes without waiting for a timer.
The cache uses weak player keys and contains no player references in its values.

This fast path is limited to the two built-in item/coating modifiers with the
native item-data provider and temperature utility. Custom modifiers,
subclasses or replacement temperature providers/utilities retain per-tick
calculation. Body-resistance data and equipment eligibility are still read
each tick. Attribute reconciliation is never skipped: it repairs removed or
changed slot modifiers and immediately clears disabled/unequipped values.
Trinkets' own accessory modifier lifecycle is unchanged.

`TrinketsUtil.findEquippedItem` searches equipped stacks by tag; `isTrinketItemEquipped` checks both hands and equipped components. Keep those semantics in mind: an accessory's effect can have held-item support even though its inventory slot is configured.

`TrinketsUtil` is an absence-safe facade: without Trinkets it retains hand/head checks, returns no accessory equipment, and does not attempt auto-equip. It does not create vanilla accessory slots or a new way to equip the Respirator.

Do not add modifiers with a random new UUID each tick, or reimplement the whole modifier path just to relocate an item.

## Existing stacks and removal

Changing config only alters acceptance for new insertion. LSO does not rebuild slot inventories, delete stacks, resize capacity or forcibly unequip gear. Existing modifiers/effects follow the established Trinkets lifecycle.

The feature does not override `canUnequip`. Binding Curse and normal Trinkets removal rules remain intact. Packs that change slot capacities are using a different API surface and should separately test item retention/drop behavior.

## Integration boundaries

The global predicate wrapper is intentionally narrow but other mods may also replace that predicate. Load-order interactions need testing; do not assume unrelated predicate replacement is conflict-free.

If another mod should own datapack slot tags, disable Use Configured Slots. To add slot types/capacity, use [the recipes](Datapack-Air-and-Slot-Recipes). Do not edit LSO config to an undefined ID and expect a new UI slot.

[Player configuration](Trinkets-Configuration) | [Architecture](Architecture-and-APIs) | [Internal Jars](Internal-Jars-and-Dependencies) | [Trinkets API source](https://github.com/emilyploszaj/trinkets/tree/1.20.1/src/main/java/dev/emi/trinkets/api)
