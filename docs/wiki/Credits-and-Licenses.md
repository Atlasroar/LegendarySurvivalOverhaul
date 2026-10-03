# Credits and Licenses

Credits identify upstream work; they do not grant permission to redistribute every asset shown on this wiki. Check individual source licenses/notices before copying.

## License

This project's own source code and original assets are licensed under **LGPL-2.1** (GNU Lesser General Public License, version 2.1), matching the original author's intended license for Legendary Survival Overhaul. The full text is in the repository's root [`LICENSE.txt`](https://github.com/Atlasroar/LegendarySurvivalOverhaul/blob/lso-fabric-1-20-1/LICENSE.txt).

Note for history: the root `LICENSE.txt` previously shipped in this repository (and still present as-is in Sfiomn's original upstream repository) was the unedited default Minecraft Forge MDK template license file — Forge/FML's own LGPL notice, not a license statement about LSO's own code. That template explicitly states Forge's LGPL covers Forge/FML itself, and that mods built on it are *not* automatically bound by that license for their own code. This repository's `LICENSE.txt` has been replaced with the actual LGPL-2.1 terms to make Sfiomn's and this project's intended license explicit and unambiguous going forward.

## Project and integrated features

| Project/contributor | Contribution | License/notice boundary |
| --- | --- | --- |
| **Sfiomn** | Original Legendary Survival Overhaul | LGPL-2.1; retain authorship notices |
| **Atlasroar** | Fabric port maintenance, release/testing coordination | LGPL-2.1; port changes retain applicable project and third-party notices |
| **Petra (gamma-delta) / Thin Air \| Miner's Lung** | Original air-quality mechanics and assets | Code MIT (copyright 2022 Petra); see [gamma-delta/MinersLung](https://github.com/gamma-delta/MinersLung). Assets separately All Rights Reserved with express authorization recorded for this project |
| **Fuzs / Thin Air** | Continued upstream maintenance/distribution | See [Fuzss/thinair](https://github.com/Fuzss/thinair) and notices |
| **AlphaMode** | Upstream Thin Air 1.19.2 port credit | Historical upstream contribution |
| **Penguin_Spy / Thinner Air** | Inspired some air-quality ideas | Attribution-only credit; MPL-2.0 upstream at [Penguin_Spy/thinner_air](https://codeberg.org/Penguin_Spy/thinner_air/src/branch/1.20.1) — no code reused, so no MPL obligation applies to LSO |
| **Fuzs / Overflowing Bars** | Health-bar rendering adaptation and authorized icon reuse | Renderer MPL-2.0; assets have a separate authorized-use notice |
| **PeteMC / HardcoreLite** | Feature inspiration for the Enchanted Golden Apple shield-health/heart-container-repair bonus and death heart-loss mechanics (v2.5.0+) | Attribution/inspiration only — LSO implements its own logic on its existing heart-container/shield-health systems, not a reuse of HardcoreLite's code; see [HardcoreLite (1.20.1-Fabric)](https://github.com/MC-Mods-Pete/HardcoreLite/tree/1.20.1-Fabric) |

LSO includes license texts and asset-use notices under [`src/main/resources/META-INF/licenses`](https://github.com/Atlasroar/LegendarySurvivalOverhaul/tree/lso-fabric-1-20-1/src/main/resources/META-INF/licenses). These are shipped in the jar. Preserve them when redistributing the mod.

Thin Air is integrated into LSO; players do not need to install a separate copy. The MIT source license is not a blanket MIT license for its images, textures or models. The asset permission recorded for this project must not be assumed to apply to another distributor's new use.

The Overflowing Bars adaptation is `client/render/OverflowingBarsHealthRenderer.java`; its icon sheet is kept separate from LSO's overlay. MPL obligations apply to that licensed source, not automatically every unrelated project file.

## Libraries and APIs

| Project | Credits/source | Terms reference |
| --- | --- | --- |
| Fabric Loader / Fabric API | [FabricMC](https://fabricmc.net/) | Respective upstream licenses |
| Trinkets | [Emily and contributors](https://github.com/emilyploszaj/trinkets/tree/1.20.1) | MIT |
| Cardinal Components | [OnyxStudios and contributors](https://github.com/OnyxStudios/Cardinal-Components-API) | Upstream license, bundled base/entity modules |
| Fzzy Config | [fzzyhmstrs](https://github.com/fzzyhmstrs/fconfig/tree/1.20.1) | TDL-M; do not jar-in-jar Fzzy Config |
| Fabric Language Kotlin | [FabricMC](https://github.com/FabricMC/fabric-language-kotlin) | Upstream runtime/library licenses |
| Mod Menu | [TerraformersMC](https://github.com/TerraformersMC/ModMenu/tree/1.20.1) | Upstream license; optional integration |
| Serene Seasons / GlitchCore | [Glitchfiend](https://github.com/Glitchfiend) | Optional integration, upstream terms |
| Minecraft | Mojang/Microsoft | Proprietary game; follow applicable distribution rules |

A dependency API license does not imply all media on its download page share that license. This table is an attribution guide, not legal advice or a replacement for upstream license files.

## Wiki images and provenance

| Image | Where used | Source and interpretation |
| --- | --- | --- |
| Original LSO feature overview | Home / survival guide | [Original project description](https://modrinth.com/mod/legendary-survival-overhaul); depicts upstream features, not a v2.2.0 Fabric screenshot |
| Thin Air gallery artwork | Air configuration | [Thin Air on Modrinth](https://modrinth.com/mod/thin-air), Petra/Fuzs project gallery |
| Trinkets inventory illustration | Trinkets and Accessories | [Trinkets on Modrinth](https://modrinth.com/mod/trinkets), upstream project gallery |

The Thin Air and Trinkets images are embedded from their published Modrinth CDN URLs, with captions on the relevant pages. No downloaded gallery copies are redistributed in this repository/wiki.

Thin Air artwork URL:

```text
https://cdn.modrinth.com/data/ll2RO0er/images/b206d9b13e5cea444615c48f04ebff195fa838b2.png
```

Trinkets illustration URL:

```text
https://cdn.modrinth.com/data/5aaWibi9/images/56224f13887cdd914a9624a18eb845dae0bcdfc0.png
```

Upstream gallery art is reference material. Thin Air's original dependency list and advertised height thresholds are **not** LSO's installation requirements or defaults. A stock Trinkets illustration is not proof of LSO's precise configured slots.

External media can change or become unavailable. For new LSO screenshots, record mod version, config state and capture provenance, and avoid including personal server/chat information.

[Developer Hub](Developer-Hub) | [Installation](Installation-and-Upgrading)
