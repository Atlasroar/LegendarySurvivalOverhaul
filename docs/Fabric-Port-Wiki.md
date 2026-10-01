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

The current public artifact is [Fabric 1.20.1 test build `v1.20.1-2.4.7-fabric.8`](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v1.20.1-2.4.7-fabric.8). It includes the earlier survival port slices plus Trinkets support, shield-health handling, First Aid Supplies updates, and the verified Vulnerability and Hard Falling effects.

The port is still incomplete. In particular, the health-bar replacement, some Forge event surfaces, data generation, and selected optional integrations still need Fabric replacements or an explicit decision to remain omitted.

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

### 8. Health shield and broken-heart overlay

- Ported the custom health overlay from Forge's `IGuiOverlay` entry point to the Fabric HUD callback.
- Reads and reserves the optional shared left HUD height for its additional shield/broken-heart rows; estimates vanilla health rows from maximum health when Overflowing Bars is absent.
- Keeps vanilla hearts in place and draws only LSO's shield/broken-heart extension, gated by the health-overhaul config and survival HUD visibility.
- The first visual test confirmed the shield heart appeared but overlapped the armor row. The 9-pixel upward adjustment was visually confirmed to clear the armor row while leaving the vanilla hearts and thirst/temperature HUD correct. Optional Overflowing Bars placement and multi-row health/body-damage cases still need testing.
- Published in `v1.20.1-2.4.7-fabric.3`.

### 9. Cold-hunger food overlay

- Connected the existing LSO cold-hunger replacement food textures to Fabric's HUD callback, gated by temperature configuration, survival HUD visibility, and the cold-hunger effect.
- The first visual test did not show the cold icons even though the client log confirmed that the Cold Food effect was applied. The callback now explicitly enables blending, resets tint, disables depth testing while replacing the vanilla row, and restores depth testing afterward.
- The user confirmed the cold icons now appear and the thirst bar looks correctly placed after another 3-pixel upward adjustment (fixed offset -11 pixels, with the configured Y offset still additive).
- The user also reported that `/effect` durations, including infinite duration, are cleared after about two seconds. This matches LSO's existing temperature-state logic: `TemperatureCapability.applySecondaryEffects` removes Cold Hunger when the player is no longer in dangerous cold. It is not a duration countdown bug; the effect is intentionally owned by the temperature state. Validate sustained behavior in an environment that keeps the player at frostbite temperature.
- The Java 17 build and integrated-client launch both succeed. Published in `v1.20.1-2.4.7-fabric.4`; the artifact SHA-256 is `A71866493A8DD35D079E7DC780E7CA82B3D7EF35E95DBDDCDE902827DF2D854D`.

### 10. Thirst exhaustion gameplay hooks

- Replaced three excluded Forge thirst-exhaustion hooks with Fabric callbacks/mixin: attacking an attackable entity adds configured hydration and food exhaustion, successfully breaking a breakable block adds configured hydration exhaustion, and jumping adds configured hydration exhaustion.
- All three paths preserve the existing thirst-enabled, active-thirst, non-creative/non-spectator gate and apply exhaustion server-side only.
- The block-break hook runs only after a successful break, preventing thirst exhaustion for canceled breaks.
- A Java 17 Gradle build succeeded, and the user confirmed in the Modrinth profile that jumping, block breaking, and attacks cause thirst exhaustion; attacks also cause food exhaustion. Creative and Spectator preserve hydration. No manual config or source change was needed after testing.
- Published in `v1.20.1-2.4.7-fabric.5`; the artifact SHA-256 is `260CA2FA72DA21B23C72AFE3BC2FD6E0A7FD5D70BF6D5460E28CDF3055F5EB86`.

### 11. Consumable finish effects

- Replaced the player item-finish Forge hook with a Fabric mixin at `ItemStack.finishUsingItem`.
- Completed player consumables apply configured temperature changes and thirst values on the server; configured body-part healing is also applied to consumables that are not `BodyHealingItem` instances.
- Existing dedicated body-healing items retain their own finish behavior and are excluded from the generic healing hook to avoid duplicate healing.
- The first Modrinth profile launch exposed an invalid mixin callback signature: `ItemStack.finishUsingItem` returns an `ItemStack`, so its injection must use `CallbackInfoReturnable<ItemStack>`. Corrected the callback signature.
- Initial testing confirmed hydration consumables work but localized body damage never occurs. The first Fabric replacement targeted `LivingEntity.actuallyHurt`, but Minecraft 1.20.1 overrides that method in `Player`, so the superclass injection never ran for players. The mixin now targets `Player.actuallyHurt` directly, where it restores configured damage distribution and hit-location selection.
- Reviewed [Body-Health-System](https://github.com/SrGnis/Body-Health-System) and [Body-Health-System-FORKED](https://github.com/32bitx64bit/Body-Health-System-FORKED) for damage-application and hit-routing approaches. This port keeps LSO's supplemental body-damage model, JSON damage-source rules, and hitbox utilities rather than replacing vanilla player health.
- The client-side generic healing callback is preserved so configured healing consumables can open the body-selection UI; temperature and hydration effects remain server-side.
- The user confirmed the corrected client launches, localized body damage occurs, and healing items work. Hydration consumables had also been confirmed working; temperature-consumable behavior has not yet been separately confirmed.
- Published in `v1.20.1-2.4.7-fabric.6`; artifact SHA-256: `BF47CCB6953612AC0BE0BAA1FE8DEB9EBE0B25BF0D27235D1DC9FF1DF3885B34`.

### 12. Temperature immunity after death

- Ported the Forge player-respawn temperature-immunity behavior to Fabric's `ServerPlayerEvents.AFTER_RESPAWN`.
- Grants the configured immunity only when the old player is dead (a death respawn), and only when temperature and the feature are enabled.
- The Java 17 build succeeds. The user confirmed in-game that a death respawn grants Temperature Immunity for the configured 90 seconds.
- Dimension-change behavior, including returning alive from the End, is explicitly deferred to a later focused test plan once more features are working as intended.
- Merged and published in `v1.20.1-2.4.7-fabric.7`.

### 13. Mob-effect interception

- Replaced the excluded Forge effect-applicable handler with mixins on both `LivingEntity.addEffect` overloads.
- When the health overhaul and absorption override are enabled, Absorption is converted to two shield-health points and the vanilla effect is denied. The user confirmed Golden Apples now grant the expected shield hearts and vanilla Absorption is gone.
- Blocks the LSO Thirst effect while the player has the Water Purifier in either hand or a Trinkets slot.
- Replaced the Fabric Curios stub with Trinkets 3.7.2+ support, including equip/use behavior, slot assignments for the LSO accessories, and data-driven temperature attribute modifiers on equipped LSO accessories.
- Dedicated Trinkets slot placement and long-term wearable-item behavior need further testing.

### 14. Health HUD, shield damage, and First Aid Supplies follow-up

- Moved broken-heart icons down 10 pixels toward the health row. The user reports the visual display largely works, with overall HUD presentation still requiring future refinement.
- Routed post-mitigation player damage through the health-overhaul shield pool before vanilla health and localized body damage are applied. Damage fully absorbed by shield health no longer causes body-part damage.
- Refreshes First Aid Supplies held/equipped detection every server tick so switching between hands and Trinkets slots takes effect immediately. The user confirmed First Aid Supplies now heals body parts.
- The user confirmed Golden Apples grant shield hearts and First Aid Supplies heals. Shield depletion on damage and detailed HUD placement remain candidates for follow-up testing.

### 15. Vulnerability and Hard Falling damage

- Ported Forge's custom damage multipliers to LivingEntity and Player damage paths before LSO shield absorption and localized body damage.
- Vulnerability retains Forge's current damage-source exclusions and amplifier multiplier. Hard Falling boosts fall damage and plays the configured sound.
- Included in [prerelease `.8`](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v1.20.1-2.4.7-fabric.8). The Java 17 Gradle build succeeded, and the user confirmed both effects behave as expected in-game.

### 16. Recovery after sleeping

- Restores the Forge sleep-finished recovery behavior when the server wakes sleepers after a successful night skip.
- Players who slept long enough regain the configured ratio of each body part's maximum health and player maximum health. Broken-heart and maximum-health attributes are refreshed when applicable.
- Java 17 Gradle build succeeded. The user confirmed sleep recovery behaves as intended in-game.

### 17. Projectile headshots

- Restores the configured unhelmeted headshot multiplier when projectile impact designation hits the head, with the Forge headshot sound.
- Preserves Forge ordering: body-part damage is based on post-shield damage before the headshot multiplier is applied to remaining player health damage.
- Java 17 build succeeded, and the user confirmed the configured multiplier behaves as intended in-game.

### 18. Debug-screen coordinate filtering

- Restored the `Hide Info From Debug` client option for F3: coordinates are replaced by the compass hint, block/facing details are removed, and targeted information is reduced to its label.
- Java 17 build succeeded. The user confirmed the debug values are hidden when the option is enabled; testing the disabled setting remains useful.

### 19. Low-hydration vision blur

- Restored the client option that gradually applies the vanilla `blobs2` post effect as hydration falls below the configured threshold, and clears LSO's effect when hydration recovers or the player enters Creative/Spectator.
- The Fabric implementation does not replace another active post effect.
- Java 17 build succeeded. The user confirmed the effect behaves as intended in-game.

### 20. Heat effect follow-up

- The user confirmed Heat Thirst at a forced temperature of 34 and Heat Stroke with periodic damage at 40; both clear after returning to 20.
- Test required Survival with thirst active, dangerous heat and secondary effects enabled, no heat/temperature immunity, and a non-Peaceful LSO difficulty. Heat Thirst starts at the `HEAT_STROKE` state (32.5+); Heat Stroke damage also requires at least 35 temperature.

### 21. Equipment resistance data modifiers

- Restored Forge's item-attribute data behavior for temperature and body-part resistance on supported vanilla equipment slots. Modifiers reconcile on server ticks and are removed when gear is unequipped, disabled, or no longer has corresponding data.
- LSO Trinkets also receive data-driven body-part resistance alongside their existing temperature modifiers.
- The user tested a Snow Helmet datapack entry and confirmed the equipment modifier systems work as expected in-game.
- The reusable test datapack is in `test-datapacks/equipment-resistance-test`.

### 22. Temperature warning overlays

- Restored the Forge heat-stroke and frostbite full-screen texture overlays, gradual fades, early/critical warning sounds, immunity checks, and client configuration gates on Fabric.
- The implementation uses Fabric's client tick and HUD callbacks. Java 17 build succeeded, and the user confirmed the configured overlays, fade transitions, and sounds produce the expected results at forced temperature thresholds.

### 23. Serene Seasons season cards

- Restored the optional season-card overlay with its existing dimension delay, normal/tropical season detection, fade timing, and configured screen offsets.
- Registered card updates and rendering through Fabric client tick, connection, and HUD callbacks. The integration remains optional and requires Serene Seasons.
- The user confirmed temperate season cards appear after enabling `Season Cards Enabled`.
- The first Wet/Dry card test used `Tropical Seasons Enabled = false`, which selects normal seasons rather than Wet/Dry cards. The test profile option was enabled for the follow-up.
- Clarified the tropical-season config comment, which previously contradicted itself.
- After enabling `Tropical Seasons Enabled`, the user confirmed all temperate and Wet/Dry cards appear as intended.
- The out-of-season bonemeal warning remains unverified: multiple callback/mixin approaches failed to display it in the user's test profile despite seasonal crops being enabled and `out_of_season_crop_behavior = 1` (can't grow). It is deferred as a low-priority issue; do not consider the warning functional.

### 24. Item tooltips

- Re-enabled the shared tooltip handler on Fabric through `ItemTooltipCallback`, restoring LSO temperature attribute coloring, merged hand modifier sections, armor coat text, temperature consumable effects, body-healing details, shade details, and hydration-consumable effect text.
- Restored the hydration/saturation tooltip image through a client-only `ItemStack.getTooltipImage` mixin, while preserving any tooltip image already supplied by the item. When an existing image takes precedence, hydration and saturation are displayed as text instead.
- Build succeeded. In-game verification is pending.

## Release and edit notes

All current artifacts are prereleases for testing, not claims of feature parity with Forge. Use Java 17 and install the required Fabric dependencies specified in `fabric.mod.json`, including Forge Config API Port 8.0.3 and Cardinal Components.

| Version | Notes |
| --- | --- |
| `v1.20.1-2.4.7-fabric` | First installable Fabric compatibility/test slice. Core items and survival systems were retained; several Forge-only systems and optional integrations were omitted. |
| `v1.20.1-2.4.7-fabric.1` | Fixes startup/config/component-registration issues found by launcher testing; uses Forge Config API Port 8.0.3. |
| `v1.20.1-2.4.7-fabric.2` | Restores thirst, temperature, wetness, and body-damage HUD indicators. The initial thirst Y placement was adjusted by -8 pixels. |
| `v1.20.1-2.4.7-fabric.3` | Adds the LSO shield/broken-heart HUD overlay. After visual feedback, the overlay was moved up 9 pixels to clear the armor row. Includes this versioned port wiki. |
| `v1.20.1-2.4.7-fabric.4` | Adds the cold-hunger food overlay, moves the thirst row up another 3 pixels, and records user visual validation. Cold Hunger remains governed by temperature state and is removed when dangerous cold ends. |
| `v1.20.1-2.4.7-fabric.5` | Ports configured thirst exhaustion for jumping, successful block breaks, and attacks; attacks also apply food exhaustion. In-game tests confirmed all three thirst triggers, attack food exhaustion, and hydration preservation in Creative/Spectator. |
| `v1.20.1-2.4.7-fabric.6` | Adds generic consumable finish effects and restores player body-part damage by injecting at `Player.actuallyHurt`. In-game tests confirmed body damage, healing items, hydration consumables, and successful client launch. |
| `v1.20.1-2.4.7-fabric.7` | Restores configured temperature immunity after death. User verified the default 90-second duration. Dimension-change testing is deferred to a later test plan. |
| `v1.20.1-2.4.7-fabric.8` | Adds Trinkets integration, Absorption-to-shield conversion, shield-first player damage processing, First Aid Supplies detection updates, and Vulnerability/Hard Falling damage behavior. Golden Apple shield conversion, First Aid healing, Vulnerability, and Hard Falling were verified in-game. |

### Latest released artifact

- File: `legendarysurvivaloverhaul-1.20.1-2.4.7-fabric.jar`
- Tag: `v1.20.1-2.4.7-fabric.8`
- SHA-256: `AD6B1C7E40CD1D02895CEFA23905E84DE5A0EC1AE720479CD4DA72F587E22332`
- Release page: <https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v1.20.1-2.4.7-fabric.8>

## Feature and compatibility notes

### Working in the current test slice

- Creative inventory registration and item icons.
- Core heater/chiller behavior and wearable armor, as visually confirmed by the user.
- Fabric player survival components and selected lifecycle/gameplay hooks.
- Server-data JSON reload listeners and 14-dataset client synchronization.
- Thirst, temperature, wetness, and body-damage HUD indicators.
- LSO shield/broken-heart HUD overlay (user-verified placement above the armor row).
- Cold-hunger food-bar overlay is visually confirmed in `.4`. Its active duration is managed by the temperature system, not by command duration overrides.
- Configured thirst exhaustion from jumping, successful block breaking, and attacking is user-validated in `.5`; attack food exhaustion also works, and Creative/Spectator do not lose hydration from those triggers.
- Localized body damage and healing items are user-validated in `.6`; hydration consumables work. Temperature-consumable behavior still needs separate validation.
- Death-respawn temperature immunity is user-validated in `.7` at the configured default 90-second duration. Dimension-change testing is intentionally deferred.
- The user confirmed the configured F3 debug filter hides debug values when enabled; disabling the option still needs verification.
- Optional Overflowing Bars shared-height integration.

### Not yet restored or not fully validated

- Overflowing Bars overlap and multi-row health/body-damage placement still need validation.
- Forge-specific health/thirst screen overlay ordering.
- Forge event subscriber behavior not yet represented by Fabric callbacks/mixins.
- Forge datagen providers and selected optional-mod integrations.
- HUD overlap with Overflowing Bars and other third-party HUD mods.
- Multiplayer/dedicated-server behavior beyond the specific networking paths already ported.
- Remaining excluded Forge event behaviors and event edge cases.
- Temperature-consumable behavior and healing recovery over time need further in-game validation.

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
