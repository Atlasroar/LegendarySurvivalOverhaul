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
