# Air Configuration

**LSO - Air** contains 27 synchronized controls in `config/legendarysurvivaloverhaul/air.toml`. It was introduced with 25 controls in v2.1.0; v2.3.0 adds two compatibility/protection settings.

![Thin Air item artwork](https://cdn.modrinth.com/data/ll2RO0er/images/b206d9b13e5cea444615c48f04ebff195fa838b2.png)

*Upstream artwork from [Thin Air on Modrinth](https://modrinth.com/mod/thin-air), credited to Petra and Fuzs. It illustrates the source project's items, not an LSO config screenshot. Upstream defaults/dependencies may differ from LSO.*

## General and provider ranges

| Field | Default | Range / meaning |
| --- | --- | --- |
| `airQualityEnabled` | `true` | Enables custom air handling. False restores vanilla handling |
| `enableSignalTorches` | `true` | Enables the cosmetic normal/signal torch interaction |
| `drownedChoking` | `100` | 0-72000 air removed by Drowned attacks; 0 disables |
| `yellowAirProviderRadius` | `6.0` | 1-32 blocks |
| `blueAirProviderRadius` | `6.0` | 1-32 blocks; includes soul sources |
| `redAirProviderRadius` | `3.0` | 1-32 blocks; includes lava |
| `greenAirProviderRadius` | `9.0` | 1-32 blocks; includes portals |

Large radii are expensive: the search volume grows cubically. Use modest values and measure server tick performance, especially with many sensitive entities.

## Dimension and height controls

| Field | Default | Meaning |
| --- | --- | --- |
| `overrideVanillaDimensionProfiles` | `false` | Opt in to the vanilla-dimension controls below |
| `overworldMinY` | `0` | Inclusive minimum eye-block Y; -2048 to 2048 |
| `overworldMaxY` | `255` | Inclusive maximum eye-block Y; -2048 to 2048 |
| `overworldAir` | `GREEN` | Overworld air within the range |
| `overworldOutsideAir` | `YELLOW` | Overworld air outside the range |
| `netherAir` | `YELLOW` | Nether ambient quality at all heights |
| `endAir` | `RED` | End ambient quality at all heights |
| `unconfiguredDimensionAir` | `GREEN` | Fallback only for dimensions without a profile |

Qualities are `GREEN`, `BLUE`, `YELLOW`, `RED`. The menu changes **ambient** quality; eye-block fluid/providers and nearby sources are evaluated first.

**Height means the block containing the entity's eyes**, not the block under its feet. Bounds here are inclusive. If maximum is below minimum, LSO warns and uses the minimum as the effective maximum; fix the stored settings rather than relying on that correction.

When overrides are disabled, existing datapack behavior stays active, including the explicit Nether YELLOW behavior. Custom dimension profiles are not replaced by the vanilla override switch.

Example: to make the Nether breathable away from hazards, enable the override switch and set `netherAir` to `GREEN`. Lava can still create RED air.

## Breathing and protection

| Field | Default | Allowed range |
| --- | --- | --- |
| `yellowDrainInterval` | `4` ticks | 1-72000; outside Nether |
| `netherYellowDrainInterval` | `2` ticks | 1-72000 |
| `redDrainInterval` | `1` tick | 1-72000; also affects submerged air |
| `airDrainAmount` | `1` | 1-300 air per successful drain attempt |
| `greenAirRefillAmount` | `4` | 1-300 air per GREEN tick |
| `breathingEquipmentDamageInterval` | `300` ticks | 0-72000; 0 disables wear |
| `suffocationDamage` | `2.0` health points | 0-100; outside water only, 0 disables damage |

**20 ticks = one second at normal tick speed.** Larger intervals mean slower drain/wear. Drain attempts are aligned with world game time, not a private timer for each player. Respiration can skip attempts, and Free Breathing/equipment can prevent drain. Equipment normally loses one durability per wear interval.

Damage uses health points (2 points = one vanilla heart), not a percentage. Vanilla underwater drowning damage is unchanged; RED interval/amount controls do alter underwater air loss while air quality is enabled. Large drain steps are prevented from skipping vanilla's `-20` drowning threshold.

## Air Bladders

| Field | Default | Range |
| --- | --- | --- |
| `airBladderRechargeAmount` | `4` per use tick | 1-300 |
| `airBladderRefillAmount` | `4` per use tick | 1-300 |
| `airBladderCooldown` | `150` ticks | 0-72000 |

Recharge applies in GREEN air. In non-refilling air, restoring one air point consumes one durability, with dispensing limited by remaining durability and the entity's maximum air. Cooldown follows filling the player's bar in non-refilling air. Values apply to both bladder variants; their item capacities remain different.

## Optional compatibility and mask protection

| Stored field | Default | Behavior |
| --- | --- | --- |
| `sulfurCaveAirEnabled` | `true` | With VanillaBackport installed, `minecraft:sulfur_caves` has YELLOW ambient air using `netherYellowDrainInterval` |
| `respiratorBlocksSulfurNausea` | `true` | **Respirator Blocks Nausea**: an equipped Respirator prevents new Nausea effects from any source, even without Backport |

The Nausea setting retains its beta.1 file key to preserve saved preferences. Its scope is now broader than sulfur gas. Holding a mask does not protect; equipping it in head equipment or a Trinkets slot does. Existing Nausea is not cured. Protection is independent of `airQualityEnabled` and does not add separate durability wear.

Sulfur air requires `airQualityEnabled`. Eye fluids and nearby providers take precedence; otherwise the sulfur biome rule takes precedence over dimension/height profiles, including opt-in vanilla dimension overrides. Disable the sulfur toggle to return to the normal dimension profile. The profile uses the biome at eye position.

The targeted upstream is **VanillaBackport 1.20.1's 1.2 development branch** with its matching Platform dependency. Do not assume older public 1.1.x releases contain sulfur caves. These dependencies remain optional for LSO.

## Recommended testing

Start with defaults. Change one setting, save, and test in Survival away from provider blocks. Then test provider boundaries, protection, and refilling separately. Keep an original config/world backup and log any unexpected result.

For the precise runtime algorithm and datapack half-open bounds, see [Air System Internals](Air-System-Internals) and [Air and Slot Recipes](Datapack-Air-and-Slot-Recipes).
