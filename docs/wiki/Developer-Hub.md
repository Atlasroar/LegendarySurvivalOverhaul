# Developer Hub

This section is for Java mod developers, pack authors, and maintainers. It describes the **Fabric 1.20.1 implementation shipped in v2.3.0**, not Forge APIs or a newer Fabric networking/config API.

## Recommended reading order

1. [Build and Work Plan](Development-and-Work-Plan): toolchain, validation, supported scope.
2. [Architecture and APIs](Architecture-and-APIs): initialization, persistent state, networking and configuration.
3. [Datapack Customization](Datapack-Customization) and [Schema Reference](Datapack-Schema-Reference): practical pack development.
4. [Air Internals](Air-System-Internals) and [Trinkets Internals](Trinkets-Integration-Internals): feature-level implementation.
5. [Air and Slot Recipes](Datapack-Air-and-Slot-Recipes): custom dimension and accessory examples.
6. [Credits and Licenses](Credits-and-Licenses): redistribution obligations before copying code/assets.

## Source map

The active branch is [`lso-fabric-1-20-1`](https://github.com/Atlasroar/LegendarySurvivalOverhaul/tree/lso-fabric-1-20-1). Java packages are under `src/main/java/sfiomn/legendarysurvivaloverhaul`.

| Path/package | Responsibility |
| --- | --- |
| `LegendarySurvivalOverhaul` | Common initializer, config/content registration and manager wiring |
| `client/LegendarySurvivalOverhaulClient` | Client setup, receivers, rendering, tooltip and input registration |
| `api/` | Facades, enums, data codecs and manager interfaces |
| `util/internal/` | Implementations behind survival facades |
| `config/` | Fzzy config classes, baked values and migration |
| `common/capabilities/` | Survival state objects and Cardinal Components attachment |
| `common/listeners/` | Server datapack parsing/loading |
| `common/events/` | Fabric callbacks, equipment hooks and air tick logic |
| `common/integration/trinkets/` | Equipped-item lookup and configurable slot rules |
| `network/` and `client/network/` | Request packets and dataset synchronization |
| `mixin/` | Focused replacements for Forge hooks at vanilla call sites |
| `src/main/resources` | Metadata, mixin config, hand-maintained resources and notices |
| `src/generated/resources` | Checked-in generated data/assets, packaged by Loom |
| `test-datapacks/` | Focused manual test resources |
| `docs/wiki/` | Versioned sources for this wiki |

## Choosing the smallest extension surface

| Goal | Prefer |
| --- | --- |
| Adjust rates, difficulty or allowed accessory slots | Fzzy settings |
| Add survival values for an existing item/block/dimension | Datapack entry |
| Add a provider or breathing-equipment member | Vanilla datapack tag |
| Add inventory slot type/capacity | Trinkets slot and entity data |
| Add new runtime behavior | Focused Fabric callback/API adapter; mixin only where no suitable hook exists |
| Read player environment for another mod | LSO facade after initialization, with side/thread discipline |

Do not patch generated files and Java bootstrap definitions independently when both define the same content. Keep runtime resources and code aligned.

## API stability

LSO's `api` package is useful for integrations, but package naming alone is not a promise of a separately versioned stable SDK. Compile against the exact release, inspect signatures, and test after upgrades. Classes in `util/internal`, `common` and mixins are implementation details.

Use a mandatory dependency only when your mod truly requires LSO. Optional adapters must not load LSO-linked classes when LSO is absent. In this port, Trinkets is a required dependency; optional integrations such as Serene Seasons use separate presence checks.

## Review checklist

Preserve behavior without the target mod. Keep client imports/rendering out of dedicated-server initialization. Execute world/player mutations on the server thread. Reconcile/remove transient modifiers rather than stacking duplicate attributes. Match the current JSON codecs and resource IDs. Surface parse/migration failures in logs.

Document the tested artifact, exact mod/dependency versions, defaults versus changed behavior, and untested cases. A successful build or server startup is not proof of a visual or multiplayer feature.

[Architecture](Architecture-and-APIs) | [Datapacks](Datapack-Customization) | [Credits](Credits-and-Licenses)
