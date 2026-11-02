# Compatibility Hook Internals

This page is the code-level companion to the player-facing [Mod Compatibility](Mod-Compatibility) page: it shows *how* each cross-mod compatibility mechanism is implemented, with source references and the PRs that introduced them. Read Mod Compatibility first for the gameplay-facing explanation of what each mechanic does.

## Pattern: presence-gated optional integration

Every optional integration in this codebase follows the same shape: a static `boolean <mod>Loaded` flag (set once at init via Fabric Loader's mod-presence API) gates any call into that mod's classes, so the integration code never loads a missing mod's classes. See `LegendarySurvivalOverhaul.trinketsLoaded`, `.sereneSeasonsLoaded`, etc., and their use in `client/LegendarySurvivalOverhaulClient.onInitializeClient()`:

```java
if (LegendarySurvivalOverhaul.sereneSeasonsLoaded) {
    ItemProperties.register(ItemRegistry.SEASONAL_CALENDAR.get(), id("time"), new SeasonalCalendarTimeProperty());
    ItemProperties.register(ItemRegistry.SEASONAL_CALENDAR.get(), id("seasontype"), new SeasonalCalendarSeasonTypeProperty());
}
```

LevelZ, HardcoreLite-inspired mechanics, Item Descriptions, and Field Guide below go a step further: they require **no presence check and no compile-time dependency at all**, because they only communicate through vanilla attributes, Minecraft's lang-file conventions, or Field Guide's own pure-data auto-populate strategy.

## Optional libraries and compatibility cleanup (v3.0.0, PR #51)

[#51](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/51) makes Trinkets, Fzzy Config, and Kotlin optional. `WearableTrinketItem` extends vanilla `Item`; the guarded `TrinketsIntegration.register()` attaches API-linked `WearableTrinket` adapters only when Trinkets is present. `TrinketsUtil` keeps supported hand/head checks without loading missing API types. This does not create accessory slots or a new vanilla Respirator equip path.

Nine dependency-free configuration schemas own defaults and validation. Bundled NightConfig handles TOML files, and LSO synchronizes eight gameplay configs independently of the optional Fzzy editor. Fzzy adapters remain isolated under `config.fzzy`; installing Fzzy still requires Kotlin for Fzzy itself. See [Architecture and APIs](Architecture-and-APIs#config-layer) and [Internal Jars and Dependencies](Internal-Jars-and-Dependencies).

The same PR removes the requested compatibility namespaces/generators/defaults and Quark assets while retaining Refabricated and Fabric BetterEnd data. Refreshing, Purity, and Reservoir share `CanteenEnchantment` applicability/treasure/trade rules. `EnchantmentHelperMixin` also filters them from unrelated level-enchanted loot, because that vanilla path checks the broad enchantment category instead of `canEnchant`.

## LevelZ: deferred death-respawn heal (PR #34/#35)

[LevelZ](https://github.com/Globox1997/LevelZ/tree/1.20) grants its own level-based bonus to the vanilla `generic.max_health` attribute via its own `AttributeModifier`. LSO's health overhaul adds its own max-health delta as a single fixed-UUID modifier on the same attribute, so the two stack correctly regardless of load order — **except** on death respawn, where ordering between two independent mods' Fabric API listeners is not guaranteed.

Introduced in [#34 "Add LevelZ health compatibility: defer death-respawn heal by one tick"](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/34) and released in [#35](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/35):

```java
// common/capabilities/ModCapabilities.java
private static final Set<UUID> PENDING_RESPAWN_FULL_HEAL = ConcurrentHashMap.newKeySet();
private static final Set<UUID> PENDING_RESPAWN_HEART_LOSS = ConcurrentHashMap.newKeySet();
```

`copyPlayerState` (registered on `ServerPlayerEvents.COPY_FROM`) used to call `player.setHealth(player.getMaxHealth())` immediately on death. That could snapshot an **incomplete** max health if LevelZ's own `AFTER_RESPAWN` listener (which re-applies its level bonus) happens to run after LSO's `COPY_FROM` handler. Instead, death respawns now queue the player's UUID and defer both the full heal and the heart-loss floor check to that player's **next server tick**:

```java
// ModCapabilities.copyPlayerState, death branch (alive == false)
if (Config.Baked.heartsLostOnDeath > 0)
    PENDING_RESPAWN_HEART_LOSS.add(player.getUUID());
else
    HealthUtil.updatePlayerMaxHealthAttribute(player);
PENDING_RESPAWN_FULL_HEAL.add(player.getUUID());
```

```java
// ModCapabilities.onPlayerTick, server side, TickPhase.START
if (phase == TickPhase.START && PENDING_RESPAWN_HEART_LOSS.remove(player.getUUID()))
    HealthUtil.loseHearth(player, Config.Baked.heartsLostOnDeath);

if (phase == TickPhase.START && PENDING_RESPAWN_FULL_HEAL.remove(player.getUUID()))
    player.setHealth(player.getMaxHealth());
```

By the start of the player's next tick, every mod's respawn-time attribute listener (registration order notwithstanding) has already run, so `getMaxHealth()` reads the true, fully-combined value. The world/dimension-change heal path (`alive == true`) is unaffected — no other mod recomputes attributes synchronously there, so it still heals immediately. `PENDING_RESPAWN_FULL_HEAL`/`PENDING_RESPAWN_HEART_LOSS` are cleared on disconnect to avoid a stale queued heal firing after a player leaves and rejoins.

## HardcoreLite-inspired Enchanted Golden Apple and death heart-loss (PR #36/#37)

Inspired by (not copied from) [HardcoreLite](https://github.com/MC-Mods-Pete/HardcoreLite/tree/1.20.1-Fabric); introduced in [#36](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/36), released in [#37](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/37). Two mechanics layered on LSO's existing Shield Health / Heart Container systems:

**Enchanted Golden Apple bonus**, applied in `FabricConsumableHooks.onFinishUsingItem` — fires at the head of `ItemStack#finishUsingItem`, i.e. before vanilla applies its own Absorption effect for the item:

```java
// common/events/FabricConsumableHooks.java
private static void applyEnchantedGoldenAppleBonus(ItemStack stack, Player player) {
    if (!Config.Baked.healthOverhaulEnabled
            || !Config.Baked.absorptionEffectOverride
            || !Config.Baked.enchantedGoldenAppleOverrideEnabled
            || !stack.is(Items.ENCHANTED_GOLDEN_APPLE))
        return;

    HealthCapability health = CapabilityUtil.getHealthCapability(player);
    health.addShieldHealth((float) Config.Baked.enchantedGoldenAppleShieldHealth);
    health.addAdditionalHealth((float) (Config.Baked.enchantedGoldenAppleHeartContainersRepaired * 2));
    HealthUtil.updatePlayerMaxHealthAttribute(player);

    FabricMobEffectHooks.markEnchantedGoldenAppleHandled(player.getUUID());
}
```

`markEnchantedGoldenAppleHandled` sets a per-player flag that the generic Absorption-override hook checks and clears on its next invocation:

```java
// common/events/FabricMobEffectHooks.java
if (!player.level().isClientSide && !ENCHANTED_GOLDEN_APPLE_HANDLED.remove(player.getUUID())) {
    HealthCapability health = CapabilityUtil.getHealthCapability(player);
    health.addShieldHealth(2);
}
return true; // cancel vanilla Absorption regardless
```

This is the dedup mechanism that makes the two bonuses **not stack**: the Enchanted Golden Apple's larger, item-specific bonus runs first and marks the player as handled; the generic override (which would otherwise also grant +2 Shield Health for *any* Absorption source, e.g. a regular Golden Apple) sees the flag, skips its own bonus, but still cancels vanilla's Absorption effect either way.

**Death heart-loss**, applied from the same deferred-tick path as the LevelZ fix above:

```java
// util/internal/HealthUtilInternal.java
@Override
public void loseHearth(Player player, int amountLost) {
    HealthCapability healthCapability = CapabilityUtil.getHealthCapability(player);

    int minHeartLimit = (int) player.getAttributeValue(AttributeRegistry.PERMANENT_HEART.get());
    // max losable heart amount = max player health - minHeartLimit
    int actuallyLostHearts = Math.min(
            (int) Math.ceil(getPlayerStableMaxHealth(player) / 2.0) - minHeartLimit, amountLost);

    if (actuallyLostHearts > 0)
        healthCapability.setAdditionalHealth(healthCapability.getAdditionalHealth() - actuallyLostHearts * 2);
    updatePlayerMaxHealthAttribute(player);
}
```

The floor is a **clamp**, not a hard stop at a configured heart count: `getPlayerStableMaxHealth(player)` already reflects any installed LevelZ level bonus (because this runs from the deferred-tick path, after LevelZ's own respawn attribute math has finished), so a LevelZ player's effective floor rises with their level — this is the "LevelZ precedence" behavior documented on [Mod Compatibility](Mod-Compatibility#levelz-health-compatibility). Unlike HardcoreLite, reaching the floor never forces Spectator mode; `loseHearth` simply stops reducing hearts.

## Item Descriptions, Mod Descriptions, and Field Guide (PR #38/#39/#40)

Both integrations are **pure data**, requiring no Java integration code, compile-time dependency, or presence check at all.

[Item Descriptions](https://github.com/cassiancc/Item-Descriptions) reads lang-file entries of the form `lore.<namespace>.<id>`. LSO ships one for every item and block it registers in `assets/legendarysurvivaloverhaul/lang/en_us.json`:

```json
"lore.legendarysurvivaloverhaul.sun_fern_leaf": "Used to craft Warm String."
```

If Item Descriptions is installed, these entries appear automatically (Item Descriptions prefers a mod's own translations over a separate [Mod Descriptions](https://github.com/cassiancc/Mod-Descriptions) resource pack entry); if it is not installed, the lang entries are simply unused strings with no runtime cost.

[Field Guide](https://github.com/evanbones/Field-Guide)'s datapack category is a static JSON file, `data/legendarysurvivaloverhaul/fieldguide/categories/flora.json`:

```json
{
    "sort_index": 100,
    "icon": "legendarysurvivaloverhaul:textures/gui/fieldguide/flora_icon.png",
    "contents": [
        { "type": "auto_populate", "strategy": "mod_plants:legendarysurvivaloverhaul" }
    ]
}
```

`auto_populate` with strategy `mod_plants:<namespace>` is a Field Guide-native mechanism that scans the given namespace's registered plant-type blocks/items itself — LSO does not enumerate its flora items in this file, hook into Field Guide's scanner, or register a custom `EntityType` for it to find. The category name comes from `category.legendarysurvivaloverhaul.fieldguide.flora` in the same lang file as the Item Descriptions entries above, and any LSO item that ends up in a populated Field Guide entry automatically shows LSO's own `lore.legendarysurvivaloverhaul.*` text as its prefilled "Take notes" note, purely because both features read the same lang-file convention — there is no explicit code link between the two mods.

The tab icon is a standalone sepia-toned recolor of the real Sun Fern Leaf item sprite (not the live item texture, not a placeholder design) — see [#39 "v2.6.1: Sepia-tone Field Guide flora icon from real Sun Fern Leaf sprite"](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/39) — because Field Guide renders category icons as raw GUI textures rather than through the item renderer, so the item's true-color texture would not match Field Guide's own sepia tab-icon palette.

[#40 "v2.6.2: Flora item Uses: descriptions"](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/40) extended the flora `lore.*` entries to state each plant's crafting purpose (e.g. "Used to craft Warm String"), so the same Field Guide prefilled note doubles as a "what is this plant for" hint, not just a flavor description.

## Extension checklist

- Prefer the LevelZ/Item-Descriptions/Field-Guide pattern (vanilla attributes, lang-file conventions, or pure data) over a compile-time dependency whenever the target mod's integration surface allows it — it needs no `modCompileOnly` entry, no presence-check gate, and keeps working even if the target mod's internal API changes between versions.
- When two LSO mechanics could both apply to the same event (like the two Absorption-override paths above), use an explicit per-player "already handled" marker rather than relying on registration/call order between them.
- When deferring work across a death/respawn boundary to avoid read-before-write races with another mod's own listener, queue by UUID and consume on the player's very next tick — do not try to guess or enforce listener registration order between independent mods.

[Mod Compatibility](Mod-Compatibility) | [Architecture and APIs](Architecture-and-APIs) | [Developer Hub](Developer-Hub)
