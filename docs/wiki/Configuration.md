# Configuration

v2.0.0 uses [Fzzy Config](https://github.com/fzzyhmstrs/fconfig/tree/1.20.1) for all **230 options**.

## Open the editor

Use LSO's config button in **Mod Menu**, or run:

```text
/configure legendarysurvivaloverhaul
```

The seven config titles use the short **LSO - ...** format.

| Config | Scope |
| --- | --- |
| LSO - Common | Shared/core settings, difficulty, air quality, provider ranges |
| LSO - Temperature | Temperature mechanics and related effects |
| LSO - Seasons | Seasonal integration settings |
| LSO - Thirst | Hydration and exhaustion |
| LSO - Health | Player health overhaul |
| LSO - Body Damage | Body-part health, damage, and secondary effects |
| LSO - Client | HUD, visual, and client preferences |

`client` settings remain local. The other six configs synchronize the server's values to clients. Operators can edit server settings in-game. Real multiplayer permission/synchronization testing remains an open validation task.

Related options are grouped, with descriptions and validated numeric ranges. Small numeric ranges use sliders. Read the description before changing a setting.

## Files and applying changes

Files are stored at:

```text
config/legendarysurvivaloverhaul/<name>.toml
```

Names are `common`, `temperature`, `seasons`, `thirst`, `health`, `body_damage`, and `client`.

In-game update/sync hooks refresh `Config.Baked`, which gameplay and HUD code read. Settings take effect without restarting unless the affected behavior only reads them at startup. For manual file editing, stop the instance first and restart afterward; do not assume external edits hot-reload.

## Legacy migration

LSO recognizes old quoted Forge keys, backs up the legacy file, loads Fzzy defaults, then copies mapped settings onto validated fields. The original file is kept as:

```text
<name>.toml.forge-backup
```

Mappings are packaged in `assets/legendarysurvivaloverhaul/forge_config_migration.json`.

Dedicated-server checks verified enum, boolean, integer, double, string-list, and double-list migration. An invalid enum was skipped with a warning; an out-of-range radius of 99 was clamped to 32.

Keep the backup until you have checked your settings. If conversion looks wrong, preserve both files and the log when [reporting an issue](Troubleshooting).

## LSO - Air (v2.1.0)

v2.1.0 adds an eighth synced config, **LSO - Air**, stored in `air.toml`. Its 25 options move the seven existing Common air controls and add 18 controls. The total is 248 options.

| Group | Controls |
| --- | --- |
| General | Air quality toggle, Signal Torches, Drowned choking |
| Air Providers | GREEN/BLUE/YELLOW/RED provider radii |
| Dimensions and Height | Opt-in vanilla-dimension overrides, Overworld inclusive eye-height range and inside/outside quality, Nether/End quality, unprofiled-dimension fallback |
| Breathing and Protection | YELLOW/Nether YELLOW/RED drain intervals, drain amount, GREEN refill rate, equipment durability interval, outside-water suffocation damage |
| Air Bladders | Recharge amount, air refill amount, cooldown |

Defaults preserve v2.0.0 behavior. Enable **Override Vanilla Dimension Profiles** to use the Overworld/Nether/End controls; custom dimensions still use datapacks. A reversed Overworld range logs a warning and uses the minimum as the effective maximum.

Intervals are in ticks (20 ticks = one second). Equipment wear and bladder cooldown can be disabled with 0. Outside-water suffocation damage can be disabled with 0; vanilla underwater damage is unchanged. RED drain settings also affect air loss when submerged. Respiration and breathing protection continue to apply.

The first launch imports Common air values only if no `air.toml` exists. Fzzy Common is backed up as `common.toml.air-backup`; Forge Common uses `.forge-backup`. Later launches keep Air's values rather than re-importing Common. Air edits invalidate cached results.
