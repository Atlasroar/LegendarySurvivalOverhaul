# Developer Hub

This section is for Java mod developers, pack authors, and maintainers. It describes the **Fabric 1.20.1 implementation shipped in v3.2.0**, not Forge APIs or a newer Fabric networking/config API.

## Recommended reading order

1. [Build and Work Plan](Development-and-Work-Plan): toolchain, validation, supported scope.
2. [Architecture and APIs](Architecture-and-APIs): initialization, persistent state, networking and configuration.
3. [Internal Jars and Dependencies](Internal-Jars-and-Dependencies): bundled modules, required downloads, and optional libraries.
4. [Datapack Customization](Datapack-Customization) and [Schema Reference](Datapack-Schema-Reference): practical pack development.
5. [Air Internals](Air-System-Internals) and [Trinkets Internals](Trinkets-Integration-Internals): feature-level implementation.
6. [Client Input and Keybindings](Client-Input-and-Keybindings) and [Compatibility Hook Internals](Compatibility-Hook-Internals): worked implementation examples.
7. [Air and Slot Recipes](Datapack-Air-and-Slot-Recipes): custom dimension and accessory examples.
8. [Pull Request Changelog](Pull-Request-Changelog): merged changes grouped by subsystem.
9. [Credits and Licenses](Credits-and-Licenses): redistribution obligations before copying code/assets.

## Source map

The active branch is [`lso-fabric-1-20-1`](https://github.com/Atlasroar/LegendarySurvivalOverhaul/tree/lso-fabric-1-20-1). Java packages are under `src/main/java/sfiomn/legendarysurvivaloverhaul`.

| Path/package | Responsibility |
| --- | --- |
| `LegendarySurvivalOverhaul` | Common initializer, config/content registration and manager wiring |
| `client/LegendarySurvivalOverhaulClient` | Client setup, receivers, rendering, tooltip and input registration |
| `api/` | Facades, enums, data codecs and manager interfaces |
| `util/internal/` | Implementations behind survival facades |
| `config/` | Dependency-free schemas, TOML persistence, config synchronization, baked values and migration |
| `config/fzzy/` | Presence-gated optional Fzzy editor adapters sharing schema defaults/validation |
| `common/capabilities/` | Survival state objects and Cardinal Components attachment |
| `common/listeners/` | Server datapack parsing/loading |
| `common/events/` | Fabric callbacks, equipment hooks and air tick logic |
| `common/integration/trinkets/` | Equipped-item lookup and configurable slot rules |
| `registry/KeyMappingRegistry` | Keybind registration and saved-key migration; see [Client Input and Keybindings](Client-Input-and-Keybindings) |
| `network/` and `client/network/` | Request packets and dataset synchronization |
| `mixin/` | Focused replacements for Forge hooks at vanilla call sites |
| `src/main/resources` | Metadata, mixin config, hand-maintained resources and notices |
| `src/generated/resources` | Checked-in generated data/assets, packaged by Loom |
| `test-datapacks/` | Focused manual test resources |
| `src/test/` and `test-runtime/` | Config regressions and opt-in dedicated-server/resource checks |
| `docs/wiki/` | Versioned sources for this wiki |

## Choosing the smallest extension surface

| Goal | Prefer |
| --- | --- |
| Adjust rates, difficulty or allowed accessory slots | TOML settings or the optional Fzzy editor; slot rules require Trinkets |
| Add survival values for an existing item/block/dimension | Datapack entry |
| Add a provider or breathing-equipment member | Vanilla datapack tag |
| Add inventory slot type/capacity | Trinkets slot and entity data |
| Add new runtime behavior | Focused Fabric callback/API adapter; mixin only where no suitable hook exists |
| Read player environment for another mod | LSO facade after initialization, with side/thread discipline |

Do not patch generated files and Java bootstrap definitions independently when both define the same content. Keep runtime resources and code aligned.

## API stability

LSO's `api` package is useful for integrations, but package naming alone is not a promise of a separately versioned stable SDK. Compile against the exact release, inspect signatures, and test after upgrades. Classes in `util/internal`, `common` and mixins are implementation details.

Use a mandatory dependency only when your mod truly requires LSO. Optional adapters must not load LSO-linked classes when LSO is absent. Starting with v3.0.0, Trinkets and Fzzy Config are optional too: keep their API-linked classes behind presence checks, as with Serene Seasons. Use `-PwithoutOptionalIntegrations` to run a development instance without Trinkets, Fzzy Config, or Kotlin.

## Review checklist

Preserve behavior without the target mod. Keep client imports/rendering out of dedicated-server initialization. Execute world/player mutations on the server thread. Reconcile/remove transient modifiers rather than stacking duplicate attributes. Match the current JSON codecs and resource IDs. Surface parse/migration failures in logs.

Document the tested artifact, exact mod/dependency versions, defaults versus changed behavior, and untested cases. A successful build or server startup is not proof of a visual or multiplayer feature.

[Architecture](Architecture-and-APIs) | [Datapacks](Datapack-Customization) | [Credits](Credits-and-Licenses)
