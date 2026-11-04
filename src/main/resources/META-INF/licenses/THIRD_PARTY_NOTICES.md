# Third-party notices

## Scorchful and Frostiful

Copyright TheDeathlyCow and respective project contributors. The following
imports/adaptations remain under GNU LGPL version 3. Full license texts are
`Scorchful-LGPL-3.0.txt` and `Frostiful-LGPL-3.0.txt`; the incorporated GPL v3
terms are reproduced in `nightconfig-GPL-3.0.txt`.

LSO adaptation date: 2026-11-02.
Compatibility revisions on 2026-11-04 add wetness-immunity checks,
composable friction adjustments and player-attributed blade damage.

Scorchful source: https://github.com/TheDeathlyCow/scorchful/tree/d74712e9b5fc4348aa8ad9c417ce938daf8bc0a1

- Unmodified `textures/block/{crimson_lily,warped_lily}.png` and shared
  `models/block/nether_lily.json`, relocated to LSO's asset namespace.
- `common/blocks/NetherLilyBlock.java` adapts upstream `NetherLilyBlock`,
  `CrimsonLilyBlock` and `NetherLilyBehaviours`: Mojang mappings, LSO wetness
  and canteens, vanilla splash effects/sounds, water-potion validation,
  nylium/rooted-dirt substrates and natural forest patches. No rooted
  blocks, farm structures, Thermoo API or Scorchful water skins are included.
- Root repository license added in upstream commit
  `1b51ffed952fedb29330515394524e5ab635b2b6`; the older all-rights-reserved
  mod metadata is not copied into LSO.

Frostiful source: https://github.com/TheDeathlyCow/frostiful/tree/f017b15a2b23a6474d03d8e4b4e9e244247ffa24

- Unmodified non-armored `fur_{helmet,chestplate,leggings,boots}.png` and
  `frostiful_fur_layer_{1,2}.png`, relocated to LSO's existing snow clothing
  texture paths. Existing LSO IDs, stats, durability and recipes remain.
- Unmodified `textures/item/ice_skates.png` and `textures/entity/skates.png`,
  relocated to LSO's namespace.
- `common/items/armor/IceSkating.java` and
  `mixin/LivingEntityIceSkatingMixin.java` adapt `IceSkater` and
  `LivingEntityMovementMixin`: mapped friction hook, locally computed state,
  distinct LSO tag/attribute UUID, no CCA flags or Thermoo, vanilla particles.
- `client/render/IceSkateRenderer.java` adapts `IceSkateModel` blade geometry,
  using Fabric ArmorRenderer with LSO fur boots instead of the upstream
  feature-renderer wiring.

All modifications are in this repository's corresponding source files and
are available with the release sources. These notices do not relicense
unrelated existing LSO or third-party files. Thermoo was studied for API
comparison only; none of its code or assets is copied.

## NightConfig

The unmodified NightConfig `core` and `toml` 3.6.7 libraries by TheElectronWill
and contributors are bundled as separate JARs under `META-INF/jars` to provide
standalone TOML configuration without Fzzy Config or Kotlin. They are licensed
under GNU LGPL v3.0; the license and accompanying GPL terms are included as
`nightconfig-LGPL-3.0.txt` and `nightconfig-GPL-3.0.txt`.

Upstream source and releases: https://github.com/TheElectronWill/Night-Config
Versioned source: https://github.com/TheElectronWill/Night-Config/tree/v3.6.7
Maven coordinates: `com.electronwill.night-config:core:3.6.7` and
`com.electronwill.night-config:toml:3.6.7`.

The libraries remain separate and may be replaced with modified compatible
versions under their license; LSO does not modify their source.

## Overflowing Bars

The health-bar rendering adaptation in
`sfiomn/legendarysurvivaloverhaul/client/render/OverflowingBarsHealthRenderer.java`
is based on Overflowing Bars by @heyitsfuzs and is licensed under the Mozilla
Public License 2.0. The full license text is included as
`overflowingbars-MPL-2.0.txt`.

`assets/legendarysurvivaloverhaul/textures/gui/overflowingbars_icons.png` is
adapted from Overflowing Bars' `icons.png`. Its original notice is included as
`overflowingbars-assets-notice.txt`; reuse was authorized by the asset author.
The asset is kept separate from LSO's existing overlay sheet.

Upstream source: https://github.com/Fuzss/OverflowingBars/tree/1.20.1

## Thin Air

The air-quality subsystem is adapted from Thin Air by Petra (gamma-delta),
originally published as Miner's Lung, and licensed under the MIT License; the
full license text is included as `thinair-MIT.txt`.

Thin Air assets are separately All Rights Reserved. Their use in this project
was expressly authorized by the asset rights holder; see
`thinair-assets-notice.txt`.

Original source: https://github.com/gamma-delta/MinersLung
Continued upstream maintenance: https://github.com/Fuzss/thinair

## Thinner Air (credit only, no code reused)

Penguin_Spy's Thinner Air, licensed under the Mozilla Public License 2.0,
inspired some of LSO's air-quality ideas. No source from Thinner Air is
included or adapted in this project, so no MPL-2.0 obligation applies here;
this entry exists solely to credit the inspiration.

Upstream source: https://codeberg.org/Penguin_Spy/thinner_air
