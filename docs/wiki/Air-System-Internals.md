# Air System Internals

Primary sources: `api/airquality/AirQualityUtil`, `AirQualityLevel`, `api/data/manager/AirQualityDataManager`, `common/events/airquality/AirQualityHooks`, and `mixin/LivingEntityAirQualityMixin`.

## Location resolution

The implementation uses a bounded nearby-block scan rather than Thin Air's per-chunk provider-position capability.

```text
air quality disabled -> GREEN
eye block is bubble column -> GREEN
eye block has nonempty fluid -> RED
eye block is a lit/eligible tagged provider -> provider quality
nearby provider in its own radius -> worst nearby quality
otherwise -> ambient dimension/height quality
```

An unlit block with a `LIT` property is not a provider. The nearby scan explicitly checks `FluidTags.LAVA`, covering source and flowing lava without inventing a nonexistent `minecraft:flowing_lava` block ID.

Qualities are ordered `GREEN`, `BLUE`, `YELLOW`, `RED`. Among nearby providers the highest ordinal wins; RED returns immediately. Provider precedence is evaluated before ambient quality, allowing soul/portal sources to affect otherwise hazardous dimensions. “Hazards win” refers to nearby provider comparison, not simply taking the maximum of ambient and all providers.

Distances are squared Euclidean distances from block centers to the entity's eye position. Each candidate uses its own radius; the outer scan cube uses the maximum configured radius. Blocks outside loaded areas are skipped.

## Cache and cost

`AirQualityUtil` caches per living entity in a synchronized `WeakHashMap`. A cached result is reused for fewer than 10 ticks only when dimension and eye block match and game time has not moved backwards.

Cache invalidates naturally on movement/dimension change/expiry. `Config.Baked.bakeAir()` also clears it after config sync/edits. Weak keys avoid permanently retaining removed entities.

For maximum radius `r`, the scan visits up to `(2 * ceil(r) + 1)^3` positions before short-circuiting. A 32-block limit is a validation bound, not a recommendation for server performance. Do not call the uncached compute method repeatedly per frame.

## Ambient precedence

When `overrideVanillaDimensionProfiles` is enabled, configured Overworld/Nether/End quality is returned. Overworld uses inclusive eye-block bounds from the config.

Otherwise Nether is explicitly YELLOW; other dimensions query `AirQualityDimensionListener`. Missing profiles return the configured unprofiled fallback.

v2.3.0 adds an earlier ambient rule in `AirQualityUtil`: if Backport is present, global air and sulfur toggles are enabled, and the eye-position biome is `minecraft:sulfur_caves`, return YELLOW. This is after fluids/providers but before the dimension manager, so it also precedes vanilla dimension overrides. YELLOW draining in that biome uses the Nether interval. Disable the sulfur toggle for normal profile evaluation.

Datapack bounds differ:

```java
// JsonAirQualityDimension.BoundedAirQuality:
return value >= min && value < max;
```

The first matching bounded entry wins. `max <= min` is invalid. To cover eye-block Y=0 through 255 in a datapack, use `min: 0, max: 256`.

Air profiles are loaded server-side with no dedicated client dataset packet. Oxygen changes are authoritative server state. Lantern/model queries also have client call sites; standalone remote-client consistency of custom server air datapacks remains a validation limitation.

## Air supply tick

The mixin captures air at `LivingEntity.baseTick` HEAD. It applies LSO's result once after vanilla's update (TAIL), with an earlier injection before vanilla's drowning check when oxygen is exhausted. A sentinel prevents double application.

The custom path only applies while enabled and for types in `legendarysurvivaloverhaul:air_quality_sensitive`; invulnerable players are excluded. On the client, it restores the tick-entry air instead of predicting vanilla dry-air refill between server updates.

```text
GREEN  -> +greenAirRefillAmount
BLUE   -> 0
YELLOW -> -airDrainAmount on its interval, subject to Respiration/protection
RED    -> -airDrainAmount on its interval, subject to Respiration/protection
new air = min(maximum air, original air + change)
```

YELLOW uses a separate Nether interval. Intervals are based on `level.getGameTime() % interval`; they do not guarantee a fresh grace period when entering hazardous air.

Respiration uses a random check with bound `enchantment level + 1`. It changes the probability of a drain attempt succeeding, not the configured interval itself.

## Protection and wear

YELLOW uses item tag `legendarysurvivaloverhaul:breathing_equipment`; RED uses `heavy_breathing_equipment`. The lookup checks head equipment first, then an equipped Trinkets item for players.

Free Breathing (vanilla Water Breathing) and use of an item in `air_refiller` also protect where applicable. Protection returns zero change: it prevents drain rather than guaranteeing air refill.

Protective equipment loses one durability when world time is divisible by the configured wear interval. Zero disables wear. The current break notification uses the HEAD equipment broadcast even when protection came from a Trinkets item; do not assume a custom Trinkets break-packet path here.

Nausea prevention is separate: `FabricMobEffectHooks.shouldCancelEffect` recognizes vanilla `MobEffects.CONFUSION` and calls `TrinketsUtil.isRespiratorEquipped`. Both vanilla addEffect overloads are intercepted. It checks the exact Respirator item in head equipment or the player's equipped Trinkets component, not hands or all items in a breathing tag. The Air setting retains beta.1's `respiratorBlocksSulfurNausea` key. No Backport presence check, ambient quality requirement, cure operation or extra durability charge is involved.

## Suffocation

Outside water, reaching `-20` air resets it to zero and applies configured drowning-source damage if above zero. This adds consequences to bad-air depletion outside vanilla's underwater branch.

Inside water, the result is clamped to at least `-20` so a large configured step cannot skip vanilla's exact drowning threshold. Vanilla underwater damage amount remains owned by vanilla.

## Air Bladders

`AirBladderItem` implements both variants. In GREEN air, damaged stacks recharge. Otherwise, if air and durability remain available, use transfers up to the configured rate:

```java
while (remaining-- > 0
        && entity.getAirSupply() < entity.getMaxAirSupply()
        && stack.getDamageValue() < stack.getMaxDamage()) {
    // Restore one air; spend one durability through the existing item damage API.
}
```

Recharge clamps damage to zero. Dispensing stops at remaining capacity. Filling a player's bar in non-refilling air applies configured cooldown on release. The ordinary/reinforced item capacities are 327 and 1962 durability in the registry; the rate config does not change those capacities.

## Extension/test checklist

Test eye-height boundaries and provider overlap separately. Check source and flowing lava, lit/unlit soul blocks, water/bubble columns, dimension transitions, equipment wear, Respiration, and depleted oxygen. Compare server/client behavior on a separate process. Do not change provider precedence accidentally while optimizing scanning.

[Air settings](Air-Configuration) | [Air datapacks](Datapack-Air-and-Slot-Recipes) | [Architecture](Architecture-and-APIs)
