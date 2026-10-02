# Troubleshooting

## Game will not start

Check Minecraft **1.20.1**, Fabric Loader **0.15.0+**, and Java **17**. Install every [required dependency](Installation-and-Upgrading) using compatible builds.

For v2.0.0, Fzzy Config and Fabric Language Kotlin replace Forge Config API Port. Check for duplicate LSO jars or an accidental Forge build. Preserve the crash report and `logs/latest.log`.

## Config button missing or labels too long

Install compatible **Mod Menu** and **Fzzy Config** builds. The screen is provided by Fzzy Config, not by a separate LSO screen factory. You can also try `/configure legendarysurvivaloverhaul`.

The full v2.0.0 release uses **LSO - Client**, **LSO - Common**, etc. Beta.1 used the longer titles; update to the full release.

## Migrated settings look wrong

Backups are stored alongside the new TOML files as `.toml.forge-backup`. Invalid enums are skipped with a warning; out-of-range numbers are clamped.

Do not delete backups or overwrite files while the game is running. Preserve the legacy backup, resulting TOML, and log, then report the affected setting and expected value.

## Air drains at unexpected heights

Nether ambient air is YELLOW at all tested heights, and End air is RED. Nearby lava can override that with RED. Portals and soul blocks can influence provider results, but hazards must not be hidden by safer sources.

Check nearby source distances, air-provider radii, dimension profile overrides, breathing equipment, and Free Breathing effects. Test in Survival; Creative behavior is not a reliable proxy for Survival air drain.

## Air protection does not fill the bar

Turtle Helmet protection stops drain but does not automatically restore all missing air. Refill in suitable air or use an air-restoring item. Check helmet durability and active effects.

## HUD overlaps

Check Client offsets and optional HUD mods. Overflowing Bars support includes tested shield/armor separation and vehicle-row correction; other HUD mods may use different layouts. Include a screenshot and the complete HUD-mod list.

## Cold Hunger disappears after `/effect`

This is expected outside dangerous cold: temperature state owns the effect and clears it when the condition ends.

## Serene Seasons cards or warnings missing

Enable season cards. For Wet/Dry cards, also enable tropical seasons. Check seasonal crop settings and out-of-season growth behavior when testing bonemeal warnings.

## Report an issue

Use [GitHub Issues](https://github.com/Atlasroar/LegendarySurvivalOverhaul/issues). Include:

- Minecraft, Java, loader, LSO, and relevant mod/dependency versions.
- Reproduction steps, expected behavior, and actual behavior.
- `latest.log` and any crash report, with sensitive information removed.
- Relevant config/datapack changes.
- Screenshots for visual issues; dimension, coordinates, and nearby sources for air issues.

Multiplayer reports should say whether the issue occurs on the server, one client, or all clients.
