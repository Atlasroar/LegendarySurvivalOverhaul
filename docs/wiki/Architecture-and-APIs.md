# Architecture and APIs

Examples use **Mojang mappings**, matching `loom.officialMojangMappings()` in this repository. Upstream Trinkets examples often use Yarn names; translate names instead of mixing mapping sets.

## Platform/dependency APIs

| API | Build version | Usage |
| --- | --- | --- |
| Fabric Loader | 0.19.2 | Entry points, config directory, mod presence checks (metadata minimum 0.15.0) |
| Fabric API | 0.92.6+1.20.1 | Lifecycle/ticks, networking, interactions, resources, biome modification and client hooks |
| Cardinal Components | 5.2.2 base/entity | Persistent player survival component |
| Trinkets | 3.7.2 | Optional accessory slots, item adapters, predicates, equipped lookup and attributes |
| Fzzy Config | 0.7.7+1.20.1 | Optional editor and config sync/update callbacks |
| Fabric Language Kotlin | 1.12.1+kotlin.2.0.20 | Required by optional Fzzy Config, not by LSO itself |
| NightConfig | 3.6.7 core/toml | Bundled standalone TOML persistence |
| Mod Menu | 7.2.2 local runtime | Optional editor discovery/metadata |
| Minecraft/DFU codecs | Minecraft 1.20.1 | `ResourceLocation`, registries/tags, NBT and JSON codec parsing |

Cardinal Components base/entity are embedded in the published jar. Fzzy Config is not embedded; its distribution terms prohibit that packaging. Serene Seasons/GlitchCore are compile-only integration dependencies, not LSO requirements.

## Initialization lifecycle

`LegendarySurvivalOverhaul.onInitialize()` establishes paths and loaded-mod flags, registers/bakes configs, registers server request channels, registers content, installs the Trinkets adapters/predicate wrapper if present, wires survival managers/reload listeners, and then registers gameplay/tick/lifecycle hooks.

`TemperatureUtil.internal`, `ThirstUtil.internal`, `BodyDamageUtil.internal`, `WetnessUtil.internal` and `HealthUtil.internal` are assigned implementations during runtime initialization. Do not call facade methods during premature static initialization.

## State and persistence

`PlayerSurvivalComponents.PLAYER_SURVIVAL` is the Cardinal Components key:

```java
ResourceLocation key = new ResourceLocation(
        "legendarysurvivaloverhaul", "player_survival");
```

`PlayerSurvivalComponent` contains temperature, wetness, thirst, health, food and localized body-damage state. Its NBT methods persist temperature, wetness, thirst, health, body damage and the first-spawn immunity flag. The food object is present but is not serialized by those methods.

Example accessor after initialization:

```java
import sfiomn.legendarysurvivaloverhaul.common.capabilities.PlayerSurvivalComponents;

var survival = PlayerSurvivalComponents.PLAYER_SURVIVAL.get(player);
var temperatureState = survival.temperature();
var hydrationState = survival.thirst();
var injuries = survival.bodyDamage();
```

These state objects are not immutable DTOs. Avoid direct mutation that bypasses dirty flags, attribute updates or sync. Prefer the relevant facade/established method and verify its implementation.

## Config layer

In v3.0.0, nine dependency-free `ConfigData` schemas define defaults and validation through `ConfigValue`. `FileConfigStore` reads/writes their existing flat TOML files using bundled NightConfig. Optional API types must stay outside this path.

When Fzzy Config and Kotlin are installed, isolated `config.fzzy` adapters register through `ConfigApiJava.registerAndLoadConfig`. Their fields derive defaults and constraints from the same schemas rather than maintaining a second set of values. The client editor uses `RegisterType.CLIENT`; the other eight use `RegisterType.BOTH`, with the same `legendarysurvivaloverhaul:<name>` IDs and translations.

Editor updates copy into the schemas and call `Config.bake`. Runtime consumers continue to read `Config.Baked`. Air additionally clears its location cache; Trinkets replaces a volatile immutable map.

LSO's `config_sync` channel synchronizes the eight gameplay configs even when client and server have different optional UI libraries. Client-only settings are excluded; received values are validated before application, and local preferences are restored on disconnect. The optional editor's own synchronization remains supported. Real multiplayer/UI interaction still needs an in-game check.

Field labels use `legendarysurvivaloverhaul.<config>.<field>`, descriptions use `.desc` or `@Comment`, and group labels follow the config/group key. Keep translations aligned with field names.

## Survival facades

Representative callable signatures:

```java
float target = TemperatureUtil.getPlayerTargetTemperature(player);
float local = TemperatureUtil.getWorldTemperature(level, position);
AirQualityLevel quality = AirQualityUtil.getAirQualityAtLocation(entity);
```

Target temperature is **not** current body temperature. World temperature queries may be costly; cache/tick them appropriately rather than computing them for every render fragment.

For temporary temperature attributes:

```java
private static final UUID HEAT_MODIFIER =
        UUID.fromString("9d640c62-f2dc-4d1d-901d-fd1f334f60b1");

// On the authoritative server, when applying your feature:
TemperatureUtil.addTemperatureModifier(player, 1.0, HEAT_MODIFIER);
// When it ends:
var heating = player.getAttribute(AttributeRegistry.HEATING_TEMPERATURE.get());
var cooling = player.getAttribute(AttributeRegistry.COOLING_TEMPERATURE.get());
if (heating != null) heating.removeModifier(HEAT_MODIFIER);
if (cooling != null) cooling.removeModifier(HEAT_MODIFIER);
```

This is a contextual integration example, not a complete entrypoint. Import `java.util.UUID`, LSO's `api.temperature.TemperatureUtil` and `registry.AttributeRegistry`. The facade writes both heating/cooling attributes and does not expose a matching removal helper; remove your UUID from both. Give your feature its own UUID; do not generate one every tick.

Manager facades include `TemperatureDataManager`, `ThirstDataManager`, `BodyDamageDataManager` and `AirQualityDataManager`. They delegate to registered listeners. Datapacks are the preferred way to alter manager content.

## Requests, threads and synchronization

`FabricServerNetworkHandler` receives `legendarysurvivaloverhaul:drink_block_fluid` and `legendarysurvivaloverhaul:body_part_healing_time`. Handlers schedule player/world work with `server.execute(...)`.

The drink request is intent rather than an arbitrary hydration value: the server rechecks survival/config state, hydration and the looked-at fluid within 3 blocks. The healing message contains NBT-selected body part/item/flags and delegates to the server healing handler. Do not treat existing packet code as a security-hardened template or trust client-provided fields in a new adapter; validate against authoritative state.

`FabricDataSyncHandler.send(...)` encodes into `FriendlyByteBuf` and sends a channel named `legendarysurvivaloverhaul:sync_<path>`. `FabricDataSyncReceiver` decodes before scheduling `client.execute(...)`.

**Exact current coverage:** 14 server reload listeners are registered. `syncAll()` sends **12 datasets**; the client registers **13 receivers** (including temperature consumable blocks, which are not sent by `syncAll()`). Air profiles have no custom dataset sync packet. Do not repeat the historical blanket claim that all 14 datasets are synchronized.

Join and successful reload call `syncAll()`. Player-state synchronization is a separate path through survival tick/dirty handling and Cardinal Components.

## Hooks and side separation

Use `FabricInteractionCallbacks` for interactions, `FabricEquipmentAttributeHooks` for vanilla equipment reconciliation, and the focused mixins for call sites without suitable Fabric events. Examples include `PlayerDamageMixin`, `LivingEntityAirQualityMixin`, `ItemStackConsumableMixin`, and `ServerLevelSleepMixin`.

HUD/render code is registered from client code. Dedicated-server validation must not load `Minecraft`, `GuiGraphics`, client renderers or screen classes through a common entrypoint.

## Validation boundaries

Review actual packet, lifecycle and mixin paths when adding integrations. Single-player tests share a JVM and can hide missing remote-client sync or side errors. Test a dedicated server with a separate client before claiming remote synchronization parity.

[Source](https://github.com/Atlasroar/LegendarySurvivalOverhaul/tree/lso-fabric-1-20-1/src/main/java/sfiomn/legendarysurvivaloverhaul) | [Datapacks](Datapack-Customization) | [Air Internals](Air-System-Internals)
