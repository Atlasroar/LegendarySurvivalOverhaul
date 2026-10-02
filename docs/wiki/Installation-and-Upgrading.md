# Installation and Upgrading

## Requirements for v2.0.0

| Component | Requirement |
| --- | --- |
| Minecraft | 1.20.1 |
| Java | 17 |
| Fabric Loader | 0.15.0 or newer |
| Fabric API | A build for Minecraft 1.20.1 |
| [Fzzy Config](https://modrinth.com/mod/fzzy-config) | 0.7.7+1.20.1 or a newer compatible 1.20.1 build |
| [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) | Required by Fzzy Config; use a version satisfying its dependency requirements |
| [Trinkets](https://modrinth.com/mod/trinkets) | 3.7.2 or newer, for 1.20.1 |
| Cardinal Components | Base/entity modules are bundled in the published LSO jar |

Fzzy Config is installed separately, not embedded in LSO. [Mod Menu](https://modrinth.com/mod/modmenu) is optional but recommended for the config button. Overflowing Bars is optional.

## Install

1. Create a Fabric 1.20.1 instance with Java 17.
2. Download the mod jar from the [latest full release](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v2.0.0).
3. Place it and the required dependency jars in your instance's `mods` directory.
4. Launch the game. With Mod Menu installed, open LSO's config button to customize the settings.

For a dedicated server, install LSO and its required server-compatible dependencies there as well. Do not install the client-only Mod Menu on the server. Broader dedicated-server gameplay testing remains open.

## Upgrade from 1.x

Back up the world and `config/legendarysurvivaloverhaul` directory first. Replace the old LSO jar; do not leave multiple LSO versions installed.

Install **Fzzy Config and Fabric Language Kotlin**. Forge Config API Port is no longer needed by LSO, but keep it if another installed mod requires it.

On first launch, legacy Forge-format config files are converted automatically. Original files are retained as `<name>.toml.forge-backup`. Invalid enum values generate warnings; numeric values outside supported ranges are clamped. See [Configuration](Configuration) for details.

The Thin Air mechanics are integrated into LSO; a separate Thin Air installation is not required. This documentation does not claim compatibility with running both implementations together.

Older Fabric prereleases remain available for history. They are not the recommended starting point. See [Releases and Versioning](Releases-and-Versioning).
