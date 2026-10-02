# Troubleshooting

## Game will not start

Check Minecraft **1.20.1**, Fabric Loader **0.15.0+**, and Java **17**. Install every [required dependency](Installation-and-Upgrading) using compatible builds.

From v2.0.0 onward, Fzzy Config and Fabric Language Kotlin replace Forge Config API Port. Check for duplicate LSO jars or an accidental Forge build. Preserve the crash report and `logs/latest.log`.

## Config button missing or labels too long

Install compatible **Mod Menu** and **Fzzy Config** builds. The screen is provided by Fzzy Config, not by a separate LSO screen factory. You can also try `/configure legendarysurvivaloverhaul`.

The full v2.0.0 release uses **LSO - Client**, **LSO - Common**, etc. Beta.1 used the longer titles; update to the full release.

## Migrated settings look wrong

Backups are stored alongside the new TOML files as `.toml.forge-backup`. Invalid enums are skipped with a warning; out-of-range numbers are clamped.

Do not delete backups or overwrite files while the game is running. Preserve the legacy backup, resulting TOML, and log, then report the affected setting and expected value.

## Air drains at unexpected heights

At defaults, Nether ambient air is YELLOW and End air is RED. Nearby lava is RED. Nearby providers are evaluated before ambient quality; among nearby providers the worst wins. A safe source does not hide a nearby lava source, but can replace an ambient dimension result.

Check nearby source distances, air-provider radii, dimension profile overrides, breathing equipment, and Free Breathing effects. Test in Survival; Creative behavior is not a reliable proxy for Survival air drain.

## Air protection does not fill the bar

Turtle Helmet protection stops drain but does not automatically restore all missing air. Refill in suitable air or use an air-restoring item. Check helmet durability and active effects.

## Air settings missing

Use v2.1.0 or later. Open **LSO - Air**, not Common. The file is `config/legendarysurvivaloverhaul/air.toml`; older Common air fields migrate automatically when the Air file does not exist. Check backups and startup logs before editing files.

## Accessory will not enter its configured slot

Use v2.2.0 and open **LSO - Trinkets**. IDs must be lower-case `group/slot` (for example `chest/necklace`), without a namespace. The slot must exist and be assigned to players. Config does not create it.

Check Use Configured Slots, the correct item's list, empty lists, other slot predicates, and logs for missing-slot warnings. With override on, old tags do not independently grant another slot to those eight items; with override off, the original tags control acceptance.

After defining custom slots via a datapack, run `/reload` and reopen inventory. See [slot recipes](Datapack-Air-and-Slot-Recipes).

## Configured item stayed in its old slot

Expected: changing a list affects **new insertion**. It does not forcibly move/delete already equipped items. Remove and reinsert deliberately; normal Binding Curse restrictions still apply.

## Respirator is missing from Trinkets config

Intentional: the Thin Air face mask/Respirator is excluded. Its normal face-slot rules are unchanged. A separate datapack is a different customization surface, not a ninth editable accessory list.

## Datapack parses but does not change gameplay

Check actual target namespace/path, array versus object roots, feature toggles, config overrides and the specific consumption/equipment hook. Reload registration is not a guarantee of client synchronization. See [Schema Reference](Datapack-Schema-Reference) for the current 12-send/13-receiver limitation.

## Respirator does not prevent Nausea

Use v2.3.0, not beta.1's upstream-specific gas interception. Equip the mask in its Trinkets face slot; holding it does not protect. Confirm **LSO - Air > Respirator Blocks Nausea** is enabled on the server. Its stored field name remains `respiratorBlocksSulfurNausea`.

The protection prevents **new** applications; wait for an existing effect to expire or remove it before testing. The tooltip's “Prevents Nausea while equipped” line appears only when the setting is enabled. VanillaBackport is not required for this protection.

Mods that mutate the active effect map directly rather than using vanilla `addEffect` can bypass this hook; report exact builds if that occurs.

## Sulfur caves are not YELLOW

Confirm a sulfur-enabled VanillaBackport 1.2 development build and its matching Platform dependency, global Air Quality and Sulfur Cave Air toggles, and the actual biome at eye height (`minecraft:sulfur_caves`). Nearby providers and eye fluids take precedence. Old public Backport releases may not contain the biome.

## High Tectonic mountains drain air

Default Overworld air is YELLOW outside eye-block Y=0..255. This is separate from native biome climate compatibility. Use Air's opt-in height/profile controls or a datapack air profile if different limits are intended.

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
