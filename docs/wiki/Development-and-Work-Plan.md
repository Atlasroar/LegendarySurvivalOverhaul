# Development and Work Plan

## Build

The project uses JDK 17, Gradle 8.12, and Fabric Loom 1.10.5.

Windows PowerShell:

```powershell
$env:JAVA_HOME = "C:\path\to\jdk-17"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
.\gradlew.bat build
```

macOS/Linux:

```sh
./gradlew build
```

Artifacts are written to `build/libs`. Development commands are `runClient` and `runServer`. Reaching a title screen is not proof of working gameplay or UI.

## Architecture

- Fabric APIs and focused mixins replace Forge runtime event hooks.
- Cardinal Components stores persistent player survival data.
- Fabric networking handles requests and survival dataset synchronization.
- Runtime datapack reload listeners preserve data-driven behavior.
- Fzzy Config update/sync hooks refresh the shared `Config.Baked` facade.
- Client-only rendering and input stay on the client side.
- Checked-in generated resources are packaged; Forge datagen execution is excluded.

Before restoring an excluded integration, inspect build exclusions and registration paths. Removing an exclusion alone is not a complete Fabric port.

## Closed work

| Area | Status |
| --- | --- |
| Build, metadata, registration | Completed |
| Persistent player state and networking | Completed |
| Gameplay hooks and Forge parity audit | Completed; intentional platform differences documented |
| HUD, tooltips, Overflowing Bars | Completed; tested visual fixes |
| Survival blocks and equipment | Completed tracked slices; tested in-game |
| Thin Air mechanics and dimensional fixes | Released and tested across all three vanilla dimensions |
| Turtle Helmet / Free Breathing | Completed |
| Fzzy Config / Mod Menu / legacy migration | Released in v2.0.0 through PR #31 |
| SemVer and obsolete branch/PR cleanup | Completed |

## Open validation

Broader multiplayer/dedicated-server gameplay, real operator permissions/config synchronization, and further in-game edit coverage remain open. Dedicated-server config registration and migration were tested, but are not a substitute for multiplayer gameplay testing. The project currently has no automated Java test sources.

## Next phase

Add additional mod integrations after the user selects targets and desired behavior. Keep dependencies optional where appropriate, preserve current behavior, validate with and without the target mod, and document exact support boundaries.

Origins and Meds and Herbs remain intentionally unsupported; Forge datagen is a deliberate developer-workflow omission, not an unfinished gameplay task.

## Documentation maintenance

The wiki page sources live in `docs/wiki` in the main repository. Keep them aligned with the README and [detailed port history](https://github.com/Atlasroar/LegendarySurvivalOverhaul/blob/lso-fabric-1-20-1/docs/Fabric-Port-Wiki.md).

Update player-facing pages and release notes when behavior or dependencies change. Record what was actually built/tested; distinguish known limitations from historical issues fixed in later releases.
