# Air Quality and Breathing

LSO integrates Thin Air's mechanics and content. Poor air can drain oxygen **outside water**, and an exhausted supply causes suffocation damage. A separate Thin Air installation is not required.

## Default dimension profiles

| Dimension | Ambient behavior |
| --- | --- |
| Overworld | GREEN from Y=0 through Y=255; YELLOW outside that range |
| Nether | YELLOW, with gradual air drain |
| End | RED, draining like underwater air |
| Unconfigured dimensions | GREEN |

Dimension/height profiles are data-driven. Nether handling also preserves a YELLOW fallback. Nearby air providers can change the result; do not use height alone to diagnose air quality.

## Nearby sources

| Source | Quality |
| --- | --- |
| Source or flowing lava | RED within the configured radius (default 3 blocks) |
| Portals/gateways | GREEN providers |
| Soul fire, soul torches, soul campfires, soul lanterns | BLUE providers |

Hazardous nearby sources take precedence over safer sources when both are in range. A portal near the player must not hide lava or Nether hazards. Oak logs are not GREEN providers.

Provider radii are configurable under **LSO - Common**. Air-provider tags and dimension profiles also support datapack customization.

## Items

| Item | Purpose |
| --- | --- |
| Safety Lantern | Indicates current air quality; supports dye-locking and axe-unlocking |
| Signal Torch | Cosmetic torch variant toggled through the configured normal-torch interaction |
| Air Bladder / Reinforced Air Bladder | Portable air support; both were tested in-game |
| Soulfire Bottle | Restores air; tested in-game |
| Respirator | Trinkets breathing equipment for YELLOW air |
| Turtle Helmet | Protects against YELLOW and RED air; loses 1 durability per 15 seconds of protection |

Breathing equipment prevents drain; it does not imply instant refilling of depleted oxygen. Refill in suitable air or use an air-restoring item.

The vanilla Water Breathing effect, potions (including splash/lingering variants), and tipped arrows are named **Free Breathing** in English because the effect also protects against bad air. This is a display-name change, not a replacement of vanilla registry IDs.

## Validation

Dimension behavior, lava/portal boundaries, soul sources, lantern indications, air bladders, bottles, and Turtle Helmet protection were tested across the Overworld, Nether, and End. Multiplayer testing remains open.

If air appears wrong, record eye/player position, dimension, nearby source distances, worn equipment, active effects, and relevant settings. See [Troubleshooting](Troubleshooting).
