# Credits and Licenses

Credits identify upstream work; they do not grant permission to redistribute every asset shown on this wiki. Check individual source licenses/notices before copying.

## Project and integrated features

| Project/contributor | Contribution | License/notice boundary |
| --- | --- | --- |
| **Sfiomn** | Original Legendary Survival Overhaul | Retain authorship notices; check original project's distribution terms |
| **Atlasroar** | Fabric port maintenance, release/testing coordination | Port changes retain applicable project and third-party notices |
| **Petra / Thin Air** | Original air-quality mechanics and assets | Code MIT (copyright 2022 Petra); assets separately All Rights Reserved with express authorization recorded for this project |
| **Fuzs / Thin Air** | Upstream maintenance/distribution | See upstream repository and notices |
| **AlphaMode** | Upstream Thin Air 1.19.2 port credit | Historical upstream contribution |
| **Petra / Miner's Lung** | Upstream Thin Air inspiration | Attribution, not an LSO runtime dependency |
| **Fuzs / Overflowing Bars** | Health-bar rendering adaptation and authorized icon reuse | Renderer MPL-2.0; assets have a separate authorized-use notice |

LSO includes license texts and asset-use notices under [`src/main/resources/META-INF/licenses`](https://github.com/Atlasroar/LegendarySurvivalOverhaul/tree/lso-fabric-1-20-1/src/main/resources/META-INF/licenses). These are shipped in the jar. Preserve them when redistributing the mod.

The retained root [`LICENSE.txt`](https://github.com/Atlasroar/LegendarySurvivalOverhaul/blob/lso-fabric-1-20-1/LICENSE.txt) describes Forge/FML and LGPL/MCP notices. It should **not** be treated as an unambiguous blanket license grant for all original LSO code/artwork. Verify the original project's terms and obtain permission where needed; this wiki does not invent a new license for upstream content.

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
