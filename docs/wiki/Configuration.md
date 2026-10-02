# Configuration

v2.3.0 uses **Fzzy Config** for **259 settings across nine configs**. Mod Menu supplies a convenient entry to the editor; it is not required for the configuration files themselves.

## Open and save

Open LSO's config button in **Mod Menu**, or use:

```text
/configure legendarysurvivaloverhaul
```

Read descriptions before editing and apply/save changes using the editor. Titles use **LSO - ...**.

| Title | File | Main purpose |
| --- | --- | --- |
| LSO - Common | `common.toml` | Difficulty, shared settings, compass/map/debug behavior, food exhaustion |
| LSO - Air | `air.toml` | 27 controls, including optional sulfur-cave air and Respirator Nausea prevention |
| LSO - Trinkets | `trinkets.toml` | Tag override switch and eight accessory slot lists |
| LSO - Temperature | `temperature.toml` | Temperature, wetness, dangerous temperatures, immunity and environmental behavior |
| LSO - Seasons | `seasons.toml` | Seasonal integration |
| LSO - Thirst | `thirst.toml` | Hydration, exhaustion and thirst effects |
| LSO - Health | `health.toml` | Player health overhaul, including Enchanted Golden Apple bonus and death heart-loss (`enchantedGoldenAppleOverrideEnabled`, `heartsLostOnDeath`, `permanentHearts`) |
| LSO - Body Damage | `body_damage.toml` | Body-part health, damage and secondary effects |
| LSO - Client | `client.toml` | HUD placement, rendering and visual preferences |

Files live in `config/legendarysurvivaloverhaul`. Manual editing should be done while the instance is stopped, followed by a restart; external file changes are not documented as hot-reloading.

## Client versus server

`client` is local. The other eight configs use server-synchronized values; operators can edit server settings. In single-player, the integrated server governs these gameplay settings. A non-operator client cannot use a local Trinkets or Air file to override a server's rules.

Edit/sync hooks refresh the baked values used by gameplay. Air changes clear cached quality results; Trinkets changes replace the insertion lookup. Some other options are only consulted during startup, so they still require a restart. Exhaustive real-multiplayer permissions/sync validation remains open.

## Upgrading existing files

| Upgrade | Behavior and backup |
| --- | --- |
| Forge Config API Port format to Fzzy | Quoted legacy keys map onto validated fields; original `<name>.toml.forge-backup` is kept |
| Common air settings to Air | Imported only if `air.toml` does not exist; Fzzy Common backed up as `common.toml.air-backup` |
| Existing Air file | Takes precedence; Common values do not overwrite it |
| First Trinkets launch | Writes defaults matching shipped LSO slot tags; there is no previous Trinkets config to migrate |

Out-of-range numbers are clamped by validators; invalid legacy enums generate warnings. Keep backups until you verify the result.

## What settings cannot do

The Air menu controls ambient vanilla profiles and breathing behavior, not the identities of every provider block. Provider and equipment membership remain datapack tags.

The Trinkets menu changes allowed locations for eight accessories. It does **not** create inventory slots, increase capacity, relocate existing stacks, or change the Respirator. Custom slots must already be provided by a mod/datapack.

For per-item data and environment rules, use the [datapack guides](Datapack-Customization).

## Detailed settings

- [Air Configuration](Air-Configuration): every field/default/range and profile priority.
- [Trinkets Configuration](Trinkets-Configuration): slot-list syntax, examples and tag fallback.
- [Troubleshooting](Troubleshooting): migration and invalid-slot diagnostics.
