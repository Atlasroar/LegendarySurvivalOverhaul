# Mod Compatibility

This page distinguishes implemented paths from general compatibility assumptions. Unlisted mods are not automatically supported or incompatible.

## Libraries

Fabric API, Fzzy Config, Fabric Language Kotlin, and Trinkets are required. Cardinal Components base/entity modules are bundled. See [Installation and Upgrading](Installation-and-Upgrading) for versions.

Forge Config API Port is only needed by older LSO versions, or by other installed mods that still depend on it.

## Optional integrations

| Mod | Implemented support and limits |
| --- | --- |
| Mod Menu | Config button supplied through Fzzy Config; LSO supplies icon, project/contact links, and wiki metadata. |
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

## Future integrations

Additional mod support is the next development phase. Targets and desired interactions must be selected before implementation. New adapters should remain optional where possible and preserve behavior without the target mod installed.

Compatibility reports should include exact mod versions, dependency versions, logs, and a small reproduction case.
