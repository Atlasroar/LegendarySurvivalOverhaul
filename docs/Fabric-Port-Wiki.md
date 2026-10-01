# Fabric 1.20.1 Port: Development and Release Wiki

This is the working reference for the Fabric port of Legendary Survival Overhaul (LSO). It is written for players testing releases, developers changing the port, and AI coding agents continuing the work. Keep it current whenever a port slice, compatibility decision, test result, or release changes.

> **Wiki hosting:** The repository's GitHub Wiki is disabled and GitHub did not accept attempts to enable it. This versioned page is the canonical wiki source until Wiki hosting is available.

## Project target

- Minecraft **1.20.1**, Fabric Loader, Fabric API, and **Java 17**.
- Gradle 8.12 and Fabric Loom 1.10.5.
- Preserve LSO's existing survival systems and datapack-driven configuration wherever possible.
- Prefer Fabric API and small adapters over adding a required gameplay library. Optional compatibility integrations must remain optional.
- Ship clearly labeled prereleases in complete, testable slices rather than implying the port is feature-complete.
- Ask for focused visual feedback whenever the client is opened for user testing. State what to inspect before launching it.

## Current status

The current public artifact is [Fabric 1.20.1 HUD test build `v1.20.1-2.4.7-fabric.2`](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v1.20.1-2.4.7-fabric.2). It contains the initial Fabric survival slice, startup fixes, and restored HUD indicators including thirst. The released HUD has built and reached an integrated world in development; the user has confirmed that release looks good. The in-progress next slice restores the custom shield/broken-heart overlay. Visual feedback found it was overlapping the armor row, so the added hearts are being moved 9 pixels upward.

The port is still incomplete. In particular, the health-bar replacement, cold-hunger food overlay, several Forge event surfaces, data generation, and some optional integrations still need Fabric replacements or an explicit decision to remain omitted.

## Step-by-step port history

### 1. Port foundation and Java target

- Created the Fabric Loom build for Minecraft 1.20.1.
- Set Java source/target compatibility to Java 17 and adjusted the build tooling so it runs on JDK 17.
- Replaced Forge player capabilities with Cardinal Components and established Fabric-side registration/initialization for the mod's registries and runtime systems.

### 2. Common gameplay hooks and registries

- Migrated common registry and lookup usage to Fabric/Minecraft APIs.
- Replaced Forge event hooks for selected survival block interactions, including hydration and temperature-related interactions.
- Ported client factories and selected item-use feedback to Fabric callbacks.
- Preserved the existing LSO temperature model rather than requiring Thermoo. Fabric API is the primary platform layer; Balm was not added because the inspected branch was not compatible with the 1.20.1/Java 17 target.

### 3. Client item use and request networking

- Moved calendar, clock, compass, map, and recovery-compass client messages into Fabric client callbacks.
- Added Fabric client/server request channels for fluid drinking and body-part healing.
- Revalidated player state on the server before applying requests; state-changing work is scheduled on the server thread.

### 4. Lifecycle, survival components, and datapacks

- Migrated player login/initialization and world-load setup to Fabric lifecycle events.
- Adapted the existing JSON datapack listeners to Fabric server-data reload registration.
- Added client/server Fabric networking for all 14 survival datapack datasets, with synchronization on player join and successful datapack reload.
- Migrated client tick behavior such as temperature effects, body-health key handling, optional thermometer polling, and delayed datapack warning display.

### 5. First installable compatibility slice

- Excluded Forge-only data generators, event subscribers, unsupported renderers, and selected optional integrations from the Fabric source set so the core could build.
- Retained runtime datapack loading; excluding developer-side data generation does not remove datapack resource loading from the mod.
- Published the first installable Fabric test artifact as `v1.20.1-2.4.7-fabric`.

### 6. Startup and configuration fixes

Testing the first client exposed an attribute-registration crash and initialization/configuration issues. The fixes:

- Ensure custom attributes are registered before the player-attribute mixin requests them.
- Bake configuration values before runtime access.
- Defer body-damage config list reads so enum initialization does not capture uninitialized values.
- Declare the Cardinal Components player-survival component in Fabric metadata.
- Set Forge Config API Port to **8.0.3**, the compatible 1.20.1 version. Forge Config API Port is required by this port; the supplied crash was not caused by it being missing.

These fixes were published in `v1.20.1-2.4.7-fabric.1`. Development validation built successfully and reached an integrated world.

### 7. Fabric survival HUDs

- Added `FabricHudCallbacks` using Fabric API's `HudRenderCallback`.
- Converted the temperature, wetness, and body-damage indicators from Forge overlay entry points into Fabric-callable render methods.
- Restored the thirst renderer, including hydration, saturation, exhaustion, consumable preview, thirst effects, and configured offsets.
- Kept its vertical position 8 pixels above the initial Fabric placement after visual feedback; `hydrationBarOffsetY` remains additive.
- Advanced HUD animation timers from Fabric client ticks.
- Used Overflowing Bars' optional Fabric ObjectShare values (`overflowingbars:leftHeight` / `overflowingbars:rightHeight`) as mutable shared heights. Overflowing Bars remains optional; it is not a hard dependency.

These changes are in `v1.20.1-2.4.7-fabric.2`. The user confirmed the released HUD looks good. This does not mean every Forge HUD feature has been restored.

### 8. Health shield and broken-heart overlay (in progress)

- Ported the custom health overlay from Forge's `IGuiOverlay` entry point to the Fabric HUD callback.
- Reads and reserves the optional shared left HUD height for its additional shield/broken-heart rows; estimates vanilla health rows from maximum health when Overflowing Bars is absent.
- Keeps vanilla hearts in place and draws only LSO's shield/broken-heart extension, gated by the health-overhaul config and survival HUD visibility.
- The first visual test confirmed the shield heart appeared but overlapped the armor row; the current fix moves it upward by 9 pixels. Validate row placement with ordinary and extended maximum health, shield health, body damage, and optional Overflowing Bars before calling it complete.

## Release and edit notes

All current artifacts are prereleases for testing, not claims of feature parity with Forge. Use Java 17 and install the required Fabric dependencies specified in `fabric.mod.json`, including Forge Config API Port 8.0.3 and Cardinal Components.

| Version | Notes |
| --- | --- |
| `v1.20.1-2.4.7-fabric` | First installable Fabric compatibility/test slice. Core items and survival systems were retained; several Forge-only systems and optional integrations were omitted. |
| `v1.20.1-2.4.7-fabric.1` | Fixes startup/config/component-registration issues found by launcher testing; uses Forge Config API Port 8.0.3. |
| `v1.20.1-2.4.7-fabric.2` | Restores thirst, temperature, wetness, and body-damage HUD indicators. Thirst Y placement was adjusted by -8 pixels. Health-bar replacement and cold-hunger food overlay remain omitted. |

### Latest released artifact

- File: `legendarysurvivaloverhaul-1.20.1-2.4.7-fabric.jar`
- Tag: `v1.20.1-2.4.7-fabric.2`
- SHA-256: `3C18655FBBBB4D3E084ADBF431FF7D2FC032E2ECA9E4E993DD0DF8B3EB329E19`
- Release page: <https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v1.20.1-2.4.7-fabric.2>

## Feature and compatibility notes

### Working in the current test slice

- Creative inventory registration and item icons.
- Core heater/chiller behavior and wearable armor, as visually confirmed by the user.
- Fabric player survival components and selected lifecycle/gameplay hooks.
- Server-data JSON reload listeners and 14-dataset client synchronization.
- Thirst, temperature, wetness, and body-damage HUD indicators.
- Optional Overflowing Bars shared-height integration.

### Not yet restored or not fully validated

- Shield/broken-heart placement in the newly ported health overlay still needs user visual validation.
- Cold-hunger food-bar overlay.
- Forge-specific health/thirst screen overlay ordering and remaining GUI effects.
- Forge event subscriber behavior not yet represented by Fabric callbacks/mixins.
- Forge datagen providers and selected optional-mod integrations.
- HUD overlap with Overflowing Bars and other third-party HUD mods.
- Multiplayer/dedicated-server behavior beyond the specific networking paths already ported.

Do not describe excluded features as supported. Check `build.gradle` source exclusions and references from client/common initializers before restoring a class; removing an exclusion alone is not a port.

### Optional integrations and library choices

- **Overflowing Bars:** optional; interoperate through its Fabric shared HUD-height values. Do not add it as a required dependency.
- **Forge Config API Port:** required by the current configuration implementation; use the 1.20.1-compatible 8.0.3 release.
- **Cardinal Components:** stores player survival component data on Fabric.
- **Thermoo:** not required. LSO's model and data-driven configuration are being kept intact; optional interoperability may be considered later.
- **Balm:** not required; the inspected source branch targeted a substantially newer Minecraft/Java stack and was not a compatible drop-in.

## Build and test

Use JDK 17. On Windows:

```powershell
$env:JAVA_HOME = "C:\path\to\jdk-17"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"
.\gradlew.bat build --no-daemon --console=plain
```

Run a development client with `.\gradlew.bat runClient`. Before opening it for user feedback, state the exact visual or behavioral checks requested (for example: “In Survival, gain LSO shield health and check the shield/broken-heart icons above vanilla hearts; confirm the thirst and temperature indicators remain visible”). Do not treat reaching the title screen as proof that a visual feature behaves correctly.

The project has no automated Java test sources at present; builds are the primary automated check. For each slice, record build/runtime evidence, artifact name and SHA-256, supported features, known omissions, and the specific player feedback requested.

## Change log for port maintainers

When continuing the port:

1. Read this page and the current `build.gradle` exclusions before changing feature scope.
2. Preserve core survival behavior; replace Forge hooks at their actual call sites and keep client-only code on the client side.
3. Update this page in the same change as implementation. Add user-facing release notes separately in the GitHub prerelease description.
4. Record what changed, what remains omitted, what was built/launched, and exact artifact/tag/hash.
5. If the GitHub Wiki becomes enabled, publish/synchronize this versioned page there; until then, this file is the canonical discoverable wiki.
