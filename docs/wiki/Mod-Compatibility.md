# Mod Compatibility

This page distinguishes implemented paths from general compatibility assumptions. Unlisted mods are not automatically supported or incompatible.

## Libraries

Fabric API, Fzzy Config, Fabric Language Kotlin, and Trinkets are required. Cardinal Components base/entity modules are bundled. See [Installation and Upgrading](Installation-and-Upgrading) for versions.

Forge Config API Port is only needed by older LSO versions, or by other installed mods that still depend on it.

## Optional integrations

| Mod | Implemented support and limits |
| --- | --- |
| Mod Menu | Config button supplied through Fzzy Config; LSO supplies icon, project/contact links, and wiki metadata. |
| LevelZ | No conflict with level-based max-health bonuses; death-respawn heal deferred so it never overwrites LevelZ's attribute bonus. Confirmed in-game: the bonus remains stable across death/respawn. See "LevelZ health compatibility" below. |
| Overflowing Bars | Shared HUD heights, shield/armor separation, broken-heart rendering, and vehicle-row gap correction; tested in-game. |
| Serene Seasons | Season cards (including tropical Wet/Dry), temperature integration, and out-of-season bonemeal warning. Cards and warning were tested in-game. |
| Supplementaries | Ordinary consumable finish-use flow uses LSO's existing hook; this is not a claim of a complete custom integration. |

For tropical Serene Seasons cards, enable **Tropical Seasons Enabled** as well as season cards. The bonemeal warning was checked with seasonal crops enabled and the out-of-season behavior set to disallow growth.

## Explicitly excluded or not required

| Mod/workflow | Status |
| --- | --- |
| Origins | Original adapter intentionally unsupported in this Fabric port. |
| Meds and Herbs | Original integration targets a Forge-only mod; unsupported. |
| Curios | Replaced by Trinkets for this port, not required. |
| Thermoo | Not required; LSO retains its own temperature model. |
| Balm | Not required; not adopted as a platform dependency. |
| Create, Aether, Dimensional Doors air adapters | Outside the implemented Thin Air integration scope. |
| Separate Thin Air installation | Not required; coexistence is not claimed as tested. |

## Trinkets and integrated Thin Air

Trinkets is the accessory API, not Curios. **LSO - Trinkets** customizes allowed slots for eight LSO accessories. It does not create slot types or change other mods' items. The Respirator/face mask is excluded; its existing face-slot behavior remains.

Custom Trinkets slot datapacks work only when the type exists and is assigned to players. While Use Configured Slots is enabled, the configured lists own those eight items' acceptance rather than their normal item tags. Disable it for tag-based pack control. See [Trinkets Configuration](Trinkets-Configuration) and [slot recipes](Datapack-Air-and-Slot-Recipes).

Thin Air mechanics are built into LSO, with the dependencies/defaults listed in this wiki. The original Thin Air download page's Forge/Puzzles Lib requirements are not LSO requirements. Do not install a second Thin Air implementation expecting automatically compatible duplicate mechanics.

## Modded biomes and terrain (v2.3.0)

| Project | Support boundary |
| --- | --- |
| [Terralith](https://github.com/Stardust-Labs-MC/Terralith/tree/1.20) | Registered biomes use native climate; biome temperature overrides work with both mod and datapack installations, without requiring a matching mod ID |
| [Tectonic](https://github.com/Apollounknowndev/tectonic/tree/v2/1.20) | Terrain uses normal LSO biome/altitude evaluation; no custom biome table or terrain patch |
| [Lithostitched](https://github.com/Apollounknowndev/lithostitched/tree/1.20.1) | Worldgen infrastructure, not a set of biomes; no mandatory LSO dependency or direct API adapter |

No hand-tuned Terralith temperature presets are shipped. Biome JSON temperature is a Minecraft climate value, not Celsius. Existing altitude, season, weather, nearby-block and underground modifiers continue to contribute.

v2.3.0 does not change the default Overworld air-height limits: Tectonic's taller terrain can still encounter high-altitude YELLOW air. Adjust Air profiles deliberately if your pack should differ.

## LevelZ health compatibility

[LevelZ](https://github.com/Globox1997/LevelZ/tree/1.20) (requires its [LibZ](https://github.com/Globox1997/LibZ/tree/1.20) library) can grant a level-based bonus to the `generic.max_health` attribute via its own `EntityAttributeModifier`. LSO's health overhaul already adds its own max-health delta as a single fixed-UUID modifier without touching the attribute's base value or other mods' modifiers, so the two mods' bonuses stack correctly regardless of load order.

The one real conflict: on death, `ModCapabilities.copyPlayerState` healed the player to `getMaxHealth()` immediately during `ServerPlayerEvents.COPY_FROM`. LevelZ reapplies its own max-health attribute bonus later, from its own `AFTER_RESPAWN` listener — Fabric doesn't guarantee ordering between independent mods' listeners. That could snapshot an incomplete (pre-LevelZ-bonus) max health on a death respawn. LSO's death-respawn heal is now deferred to the player's next server tick (instead of healing immediately during `COPY_FROM`), so it always reads the final, fully-combined max health — LevelZ's bonus is never clobbered or read too early. The world/dimension-change heal path is unaffected, since LevelZ does not recompute attributes synchronously there.

Confirmed in-game (v2.4.0): the LevelZ health bonus remains stable across death and respawn, operating as intended.

LibZ itself only supplies shared GUI/network/config/registry utilities for LevelZ; it has no attribute or health logic of its own, so no additional adapter is needed for it.

## Optional VanillaBackport sulfur support

[VanillaBackport](https://github.com/ItsBlackGear/VanillaBackport/tree/1.20.1) support targets the **1.2 development branch** and its matching [Platform](https://github.com/ItsBlackGear/Platform/tree/1.20.1) dependency. Sulfur caves use `minecraft:sulfur_caves`, not the mod namespace.

With the Air toggle on, that biome has YELLOW ambient air using the Nether drain interval. Eye fluids and nearby providers retain priority; the sulfur rule precedes ambient dimension profiles. The installed-mod check keeps this rule inactive without Backport.

An equipped Respirator now prevents new vanilla Nausea effects from any source, independently of Backport. Both `LivingEntity.addEffect` overloads pass through the existing LSO effect hook. No fragile upstream gas-method mixin or direct Platform dependency is required. This does not cure existing Nausea or protect a merely held mask.

The user tested beta.1 biome/air behavior in-game successfully. Mask protection was revised after a reported failure and passed isolated server assertions; revised in-game protection remains pending. The synthetic upstream-shape fixture is not full real-mod/client validation.

## Future integrations

Additional mod support is the next development phase. Targets and desired interactions must be selected before implementation. New adapters should remain optional where possible and preserve behavior without the target mod installed.

Compatibility reports should include exact mod versions, dependency versions, logs, and a small reproduction case.
