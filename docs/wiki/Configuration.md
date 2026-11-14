# Configuration

v3.2.2 supports **264 settings across nine configs** without extra configuration libraries. **Fzzy Config is strongly recommended, not required**, for in-game editing; Mod Menu supplies a convenient entry to it. Installing Fzzy Config still requires its Fabric Language Kotlin dependency. v2.8.1 and earlier require both libraries.

## Open and save

With Fzzy Config installed, open LSO's config button in **Mod Menu**, or use:

```text
/configure legendarysurvivaloverhaul
```

Read descriptions before editing and apply/save changes using the editor. Titles use **LSO - ...**.

Without Fzzy Config, edit the same TOML files while the instance is stopped and restart to apply changes. Installing or removing the optional editor should not require deleting configuration files.

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

LSO's own config-sync channel sends gameplay settings independently of Fzzy Config, so clients and servers can have different optional editor installations. Client-only settings are excluded, received values are validated before application, and local preferences are restored on disconnect.

**v3.2.4 performance work:** gameplay values still get checked every
20 server ticks, but unchanged settings reuse their serialized snapshot.
Joining players receive current values immediately, even before the next
poll, and server shutdown clears the snapshot cache. Client-only edits do
not rebuild gameplay JSON. This does not add external TOML hot-reloading or
change editing permissions.

Edit/sync hooks refresh the baked values used by gameplay. Air changes clear cached quality results; with Trinkets installed, its changes replace the insertion lookup. Some other options are only consulted during startup, so they still require a restart. Actual multiplayer packet flow and exhaustive in-game edit/permission validation remain open.

## Upgrading existing files

| Upgrade | Behavior and backup |
| --- | --- |
| Forge Config API Port format to current TOML | Quoted legacy keys map onto validated schema fields, with or without Fzzy; original `<name>.toml.forge-backup` is kept |
| Common air settings to Air | Imported only if `air.toml` does not exist; Common backed up as `common.toml.air-backup` |
| Existing Air file | Takes precedence; Common values do not overwrite it |
| First Trinkets launch | Writes defaults matching shipped LSO slot tags; there is no previous Trinkets config to migrate |
| 2.x to 3.0.0 | Existing flat TOML files remain usable; no config deletion or optional editor installation is needed |

Out-of-range numbers are clamped by validators; invalid legacy enums generate warnings. Keep backups until you verify the result.

## What settings cannot do

The Air menu controls ambient vanilla profiles and breathing behavior, not the identities of every provider block. Provider and equipment membership remain datapack tags.

The Trinkets settings change allowed locations for eight accessories only when Trinkets is installed. They do **not** create inventory slots, increase capacity, relocate existing stacks, or change the Respirator. Custom slots must already be provided by a mod/datapack.

For per-item data and environment rules, use the [datapack guides](Datapack-Customization).

## Detailed settings

**Added in v3.2.0:** `wetCoolingRateMultiplier` in `temperature.toml` defaults to
`1.5` (range `0.1` to `100`) and multiplies cooling-only temperature steps
at any nonzero wetness in every biome/dimension. Set `1` for the old speed.
It requires `wetnessEnabled`; it does not speed warming or force cooling
when the adjusted target remains warmer. It multiplies with IW snowstorm
cooling (default combined rate `3x`), never overshooting the target.
`wetMultiplier` remains the saturation-scaled target offset (`-10` at full
wetness), independently configurable. Existing configs receive the new default
without deleting files. v3.1.0 had 263 settings; v3.2.0 has 264.

**Added in v3.1.0:** `temperature.toml` has the default-enabled `immersiveWeatheringStormsEnabled` switch. It independently controls optional Atlasroar IW 2.5.0+ storm/wind penalties, requires `temperatureEnabled`, and has no effect without IW. Existing configs receive the new default without deleting files. Ambient cold/heat secondary-effect switches do not disable it. See [storm compatibility](Mod-Compatibility#immersive-weathering-storms-v310) for drink protection and shelter rules.

- [Air Configuration](Air-Configuration): every field/default/range and profile priority.
- [Trinkets Configuration](Trinkets-Configuration): slot-list syntax, examples and tag fallback.
- [Troubleshooting](Troubleshooting): migration and invalid-slot diagnostics.
