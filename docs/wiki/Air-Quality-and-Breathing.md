# Air Quality and Breathing

LSO integrates Thin Air's mechanics and content. Poor air can drain oxygen **outside water**, and an exhausted supply causes suffocation damage. A separate Thin Air installation is not required.

## Default dimension profiles

| Dimension | Ambient behavior |
| --- | --- |
| Overworld | GREEN from Y=0 through Y=255; YELLOW outside that range |
| Nether | YELLOW, with gradual air drain |
| End | RED, draining like underwater air |
| Unconfigured dimensions | GREEN |

These heights use the block containing your **eyes**, not your feet. Overworld/End profiles are data-driven; Nether is explicitly YELLOW unless the Air config's vanilla override switch is enabled. Nearby air providers can change the result; do not use height alone to diagnose air quality.

## What the colors mean

| Quality | Oxygen behavior at defaults |
| --- | --- |
| GREEN | Refills 4 air per tick, up to your maximum |
| BLUE | Neither drains nor refills |
| YELLOW | Drains 1 air per 4 ticks; in the Nether, per 2 ticks |
| RED | Drains 1 air per tick |

Rates are configurable. Respiration can skip drain attempts; protection can stop them. Blue soul sources provide a place to conserve remaining air, not automatically recover a depleted bar.

## Nearby sources

| Source | Quality |
| --- | --- |
| Source or flowing lava | RED within the configured radius (default 3 blocks) |
| Portals/gateways | GREEN providers |
| Soul fire, soul torches, soul campfires, soul lanterns | BLUE providers |

Hazardous nearby providers take precedence over safer **nearby providers** when both are in range. A portal must not hide nearby lava. Sources are evaluated before ambient dimension quality, so a safe provider can change the Nether/End ambient result. Oak logs are not GREEN providers.

Provider radii and breathing behavior are now configurable under **[LSO - Air](Air-Configuration)**, not Common. Air-provider tags and dimension profiles also support [datapack customization](Datapack-Air-and-Slot-Recipes).

## Items

| Item | Purpose |
| --- | --- |
| Safety Lantern | Indicates current air quality; supports dye-locking and axe-unlocking |
| Signal Torch | Cosmetic torch variant toggled through the configured normal-torch interaction |
| Air Bladder / Reinforced Air Bladder | Portable air support; both were tested in-game |
| Soulfire Bottle | Restores air; tested in-game |
| Respirator | Trinkets breathing equipment for YELLOW air; prevents new Nausea from any source while equipped when enabled |
| Turtle Helmet | Protects against YELLOW and RED air; loses 1 durability per 15 seconds of protection at defaults |

Breathing equipment prevents drain; it does not imply instant refilling of depleted oxygen. Refill in suitable air or use an air-restoring item.

The Safety Lantern reports the local query rather than guaranteeing safety around it. Dye-locking can intentionally fix its visible appearance. Signal Torches are cosmetic, not a new clean-air source. Recharge an Air Bladder by using it in GREEN air; use it in hazardous air to transfer its remaining capacity into your air bar.

The vanilla Water Breathing effect, potions (including splash/lingering variants), and tipped arrows are named **Free Breathing** in English because the effect also protects against bad air. This is a display-name change, not a replacement of vanilla registry IDs.

## Validation

Dimension behavior, lava/portal boundaries, soul sources, lantern indications, air bladders, bottles, and Turtle Helmet protection were tested across the Overworld, Nether, and End. Multiplayer testing remains open.

If air appears wrong, record eye/player position, dimension, nearby source distances, worn equipment, active effects, and relevant settings. See [Troubleshooting](Troubleshooting).

## Optional sulfur caves

With sulfur-enabled VanillaBackport installed and its Air toggle enabled, `minecraft:sulfur_caves` is a YELLOW ambient zone using the Nether drain interval. Fluids and nearby providers still take precedence. This targets Backport's 1.20.1 / 1.2 development branch and matching Platform build; it does not install either dependency.

The Respirator now blocks new Nausea through vanilla effect application, rather than relying on a particular gas implementation. Wear it; simply holding it does not protect. Other Nausea sources are also prevented. It does not remove an already active effect.

For implementation details, see [Air System Internals](Air-System-Internals). For all 27 controls, see [Air Configuration](Air-Configuration).
