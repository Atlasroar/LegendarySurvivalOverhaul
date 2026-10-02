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

The latest public artifact is the [Fabric 1.20.1 `.24` prerelease](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v1.20.1-2.4.7-fabric.24). The next test build removes oak logs as GREEN providers and explicitly recognizes lava fluid states for RED air within the existing 3-block default radius. The Nether behavior is deferred for later investigation. The first full (non-prerelease) release remains [`v1.20.1-2.4.7-fabric.15`](https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v1.20.1-2.4.7-fabric.15). Air-quality behavior remains pending in-game verification.

The port is considered feature-complete pending further in-game testing, particularly multiplayer/dedicated-server validation. Every tracked gameplay slice — survival systems, HUD/tooltip rendering, sewing table and coat application, heater/cooler multiblocks, and the Forge event-subscriber audit — is user-verified in a single-player/integrated-server world as of `.14`. A full audit of the three Forge-only event files found no remaining behavioral gaps: all differences from Forge are either intentional feature drops (Origins, Meds and Herbs), necessary platform adaptations forced by different Fabric hook points (bonemeal, damage pipeline), or pre-existing Forge behavior that was never different on Fabric (the armor coat tooltip, which never showed a numeric resistance delta on Forge either). Forge-only datagen execution and selected optional integrations remain intentionally omitted.

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
- The user later verified that death-respawn temperature immunity remains active across a Nether portal trip and return before its timer expires.
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
- The warning is injected into Serene Seasons' own client-side `SeasonalCropGrowthHandler.applyBonemeal` event. Its Fabric/GlitchCore callback can cancel the interaction client-side, so a server-side warning is never sent for blocked crops. Crop fertility uses Serene Seasons' full namespaced IDs. The user confirmed the warning appears in-game with seasonal crops enabled and `out_of_season_crop_behavior = 1` (can't grow).

### 24. Item tooltips

- Re-enabled the shared tooltip handler on Fabric through `ItemTooltipCallback`, restoring LSO temperature attribute coloring, merged hand modifier sections, armor coat text, temperature consumable effects, body-healing details, shade details, and hydration-consumable effect text.
- Restored the hydration/saturation tooltip image through a client-only `ItemStack.getTooltipImage` mixin, while preserving any tooltip image already supplied by the item. When an existing image takes precedence, hydration and saturation are displayed as text instead.
- The user confirmed hydration tooltips for apples and melon, Rotten Flesh's thirst effect and values, and the displayed values for Refreshing enchantment levels I-III. Temperature tooltips work for tested temperature-related items.
- Added rendering of the armor's configured temperature, heat-resistance, cold-resistance, and thermal-resistance values using localized attribute labels. The user confirmed Snow and Desert armor show the correct tooltip values and colors.

### 25. HUD layer rewrite foundation (released in `.10`)

- Replaced the single Fabric `HudRenderCallback` with mixin anchors after vanilla's combined status-bar pass and at the end of `Gui.render`, separating bars from screen-wide overlays/cards.
- Adapted Overflowing Bars' health renderer and imported its icon sheet for LSO's health-overhaul HUD when Overflowing Bars itself is absent. If the optional mod is loaded, its own health rendering remains authoritative; the existing ObjectShare spacing integration is retained.
- Used Puzzles Lib's per-element rendering hooks as a design reference. Its Fabric mixin does not expose hooks for the combined health/armor/food/air status-bar pass, so LSO uses local anchors rather than adding Puzzles Lib as a runtime dependency.
- The user confirmed vanilla health rendering at 40 health, including extra rows and vanilla effect styles; armor toughness also does not overlap the armor bar. Subsequent in-game tests confirmed shield/armor row separation, broken-heart row placement, and foreground rendering over the health containers.
- The first spacing correction shifted both the armor row and LSO hearts into the same row. Broken hearts are drawn into the last visible slots of the custom renderer's single ten-heart row, replacing the separate broken-heart row. The user reported that additional broken hearts were wrapping into a row above regular hearts and overlapping shield hearts; the overlay now caps its slot count to the renderer's visible row instead of wrapping based on stable max health. After confirming the slots, the user reported that broken sprites were behind the regular health layer; LSO heart sprites now render at a foreground Z offset above the custom health renderer.
- Shield hearts occupy their own row at the vanilla armor-row position; armor shifts upward only while shield hearts are present. Shield layers alternate yellow and orange every 10 hearts. The user confirmed the final broken-heart layering and the intended shield/armor placement in `.10`.
- Third-party attribution, Overflowing Bars' MPL-2.0 text, and the separately authorized asset notice are included under `src/main/resources/META-INF/licenses/`.

### 26. Forge event-subscriber audit (released in `.11` and `.12`)

- Compared the excluded Forge event subscribers with Fabric callbacks, mixins, and lifecycle hooks. Core thirst interactions, consumable effects, survival exhaustion, damage/body-part handling, sleep recovery, mob-effect interception, login/respawn behavior, natural-regeneration gamerule setup, client timers, season cards, and HUD callbacks already have Fabric equivalents.
- Restored the excluded loot-table injections with Fabric's loot-table modify event: heart fragments and resistance rings in their configured chests, First Aid Supplies in pillager outposts, Purity books in Nether chests, Water Purifiers from drowned, sponges from fishing treasure, and Nether Chalices from piglin bartering. The original weights and counts are preserved.
- Replaced the excluded Forge biome modifiers with Fabric biome modifications for ice ferns in cold Overworld biomes and sun ferns/water plants in hot Overworld biomes, using base-temperature selectors.
- Increased the placement rarity interval for ice ferns, sun ferns, and water plants from once every 10 chunks to once every 15 chunks. Their checked-in placed-feature JSON is updated alongside the Java bootstrap definitions so the packaged datapack uses the same rate.
- Water-plant seeds can now be planted on farmland and grass blocks while retaining sand support. Gold fern placement was checked against the shared `GoldFernFeature`: both cold and hot fern configurations pass through the configured gold-chance conversion; gold-block survival support now matches the corresponding fern substrates, including taiga dirt variants and snow for ice ferns.
- The user confirmed in-game that loot, Purity anvil behavior, world feature generation, and plant transparency appear correct. This follow-up adjusts spawn frequency and water-plant substrates after that validation.
- Restored Purity's anvil side effect in `AnvilMenuMixin`: a canteen output with the Purity enchantment immediately converts existing normal water to purified water without mutating the input stack. The old grindstone handler had no behavior beyond comments; purified contents already persist through enchantment removal.
- Restored the Forge debug-filter scope so position/target information is hidden only outside Creative and Spectator modes.
- Meds and Herbs compatibility is closed as intentionally unsupported: it is Forge-only, and the user confirmed that Fabric compatibility can be dropped. No integration or generated medkit data is included in the Fabric port.
- Still omitted: Forge-only datagen execution. Supplementaries has a Fabric 1.20.1 build and its lunch-basket delegates to the selected item's normal finish-use path, which LSO already handles; no adapter was needed. Generated recipes, advancements, models, and block loot are checked into `src/generated/resources`.
- Origins-specific Fabric compatibility and generated data have been removed at the user's direction; Origins is intentionally unsupported in this port.
- The out-of-season warning is injected into Serene Seasons' client-side `SeasonalCropGrowthHandler.applyBonemeal` event. Serene Seasons' Fabric/GlitchCore callback may cancel bonemeal client-side, so a server-side warning hook did not work. The user confirmed the warning with seasonal crops enabled and `out_of_season_crop_behavior = 1` (can't grow).

## Current next-work plan after `.12`

### Closed or already verified

- Origins and Meds and Herbs are intentionally unsupported; no Fabric adapter work remains for either.
- Supplementaries' ordinary lunch-basket finish-use flow is covered by LSO's existing item-finish hook, so no Supplementaries-specific adapter is planned.
- Temperature consumables, tonic recovery over time, the F3 filter in both states, and death-respawn immunity across dimension changes are user-verified.
- Vanilla heater and cooler fuel data is present in the generated resources. The listener must retain vanilla entries as well as optional-mod entries.
- Heater and cooler, including fuel consumption, lit state, and thermal fuel persistence across save/reload, are user-verified in-game.
- Heater multiblock drop behavior (breaking the base vs. top block) is user-verified in-game.
- The sewing table opens correctly, the warm/cold string recipes craft correctly, and coat application to armor works as expected; all user-verified in-game.

### Remaining port-wide validation

- Exercise multiplayer and dedicated-server behavior beyond the networking paths already tested in an integrated world.

Forge datagen task execution remains omitted; checked-in generated runtime resources are used by the Fabric build, so restoring that developer workflow is not a gameplay prerequisite.

### 27. Overflowing Bars HUD compatibility fixes (released in `.14`)

- Reverse-engineered Overflowing Bars v8.0.1's Fabric mixin behavior by decompiling its jar (no public source repository exists), since its shared-height contract was only partially matched by the original integration.
- Fixed shield hearts overlapping the armor row by calling `OverflowingBarsUtil.reserveLeftHeight(...)` after drawing shield hearts, reporting their row height back through the shared `overflowingbars:leftHeight` value, which Overflowing Bars never receives by default (its `toughness.leftSide` config defaults to `false`).
- Fixed broken hearts disappearing entirely when Overflowing Bars is installed: Overflowing Bars cancels vanilla `Gui.renderHearts` at `HEAD` when its `health.allowLayers` config (default `true`) is set, which silently skipped LSO's old `TAIL` injection into the same method. Replaced it with a `@ModifyArgs` capture of the heart-rendering coordinates at the call site in `renderPlayerHealth` (which always executes regardless of what the callee does), then draws broken hearts from the guaranteed-to-run `renderStatusBarLayers` injection point.
- Fixed a phantom 10-pixel gap between the armor-toughness row and the thirst bar on the right side: Overflowing Bars unconditionally reserves a mount/vehicle health-bar row (`Math.max(1, getVisibleVehicleHeartRows(getVehicleMaxHearts(vehicle)) - 1) * 10`) even when the player isn't riding anything, because `getVisibleVehicleHeartRows(0)` returns `0` rather than feeding the `-1` adjustment as expected. Added `OverflowingBarsUtil.correctVehicleRowQuirk(Player)`, which subtracts the known 10-pixel quirk from the shared `overflowingbars:rightHeight` value when no vehicle health bar is actually shown, applied before any height-dependent HUD renderer reads it.
- All three fixes were user-verified in-game with Overflowing Bars installed and enabled: shield hearts, broken hearts, and the armor row render correctly, and the right-side stack (hunger, air, toughness, thirst) has no gap.

### 28. Forge event-subscriber final audit and first full release (released in `.15`)

- Re-audited all `@SubscribeEvent` handlers in the three excluded Forge-only files (`CommonForgeEvents`, `CommonModBusEvents`, `ClientForgeEvents`) against their Fabric mixin/callback equivalents, including cancellation semantics, side-only checks, and damage-pipeline timing.
- Confirmed `CommonModBusEvents`'s player attribute registration has full parity with `PlayerAttributeMixin`: all 15 LSO attributes (temperature, resistance, body-part resistance, and heart attributes) are registered identically.
- Confirmed the drink-reach distance is intentionally hardcoded to `3.0` in `FabricInteractionCallbacks` rather than reading Forge's dynamic `BLOCK_REACH` attribute, because vanilla Fabric has no equivalent reach attribute without Forge; this is an acceptable platform limitation, not a bug.
- Confirmed the Serene Seasons bonemeal `MAIN_HAND`-only restriction and the collapsed `LivingHurtEvent`/`LivingDamageEvent` → `actuallyHurt` mixin handling are correct adaptations to Fabric's different hook points, not missing functionality.
- Confirmed, by comparing against the original Forge `TooltipHandler`, that the armor coat tooltip only ever showed flavor text (e.g. "Warm Coat Applied") rather than a numeric resistance delta on Forge as well — this was not a port regression. The underlying `AttributeModifier` is recomputed every server tick in `FabricEquipmentAttributeHooks` and updates correctly when a coat is applied.
- No remaining code changes were required; this audit closes out the last tracked "remaining Forge event edge cases" item. The port is considered complete pending further in-game testing (particularly multiplayer/dedicated-server scenarios), and `.15` is published as the first full (non-prerelease) release.

### 29. Thin Air air-quality integration (released in `.16`; fixes in `.17`–`.25`)

- Adapts the MIT-licensed 1.20.1 Thin Air code and its assets into LSO's Fabric module. Asset reuse is separately All Rights Reserved and was expressly authorized; both notices are included under `src/main/resources/META-INF/licenses/`.
- Adds Green, Blue, Yellow, and Red air quality, configurable air-provider radii, data-driven dimension/height profiles, breathing-equipment protection, Drowned attack choking, and air-supply hooks in `LivingEntity.baseTick`.
- Default profiles make Overworld air Green from Y=0 through Y=255 and Yellow outside that range, Nether air Yellow, and End air Red. Unconfigured dimensions default to Green.
- Lava receives RED air within the configured RED provider radius (default 3 blocks). The `.25` update explicitly checks `FluidTags.LAVA` while scanning nearby blocks, covering both source and flowing fluid states rather than relying solely on a block tag.
- Nether ambient air is YELLOW. Its configured profile falls back explicitly to YELLOW if absent, and YELLOW drain occurs every two ticks in the Nether (every four ticks elsewhere) to make its gradual effect observable sooner. RED lava/fire providers near the player override the ambient dimension profile.
- The `.22` update captures air at `baseTick` entry, applies the quality adjustment once after vanilla's air update, and retains a pre-drowning check so air quality no longer competes with vanilla's separate air-supply branches. Dimension profile reload no longer drops resources based on whether the resource namespace is registered as a mod, allowing datapacks to define profiles in any namespace.
- The `.23` log showed that `minecraft:flowing_lava` is not a registered 1.20.1 block ID (source and flowing states both use the `minecraft:lava` block), invalidating the RED provider tag. The invalid entry is removed. On the client, air-supply prediction now preserves the last server-synced value instead of vanilla dry-air regeneration locally refilling it between server updates.
- `.24` temporarily added oak logs as GREEN providers; `.25` removes those entries as requested. Green-provider precedence remains in place for actual GREEN providers, such as portals.
- Adds Safety Lanterns with dye-locking and axe-unlocking, Signal Torches, Air Bladders (including durability/tag-preserving reinforcement), a Soulfire Bottle, and a Trinkets Respirator with a custom head render layer. Core air-provider/item/entity tags, crafting recipes, models, textures, and structure loot injections are included.
- Deliberately replaces Thin Air's acknowledged-unreliable per-chunk provider-position capability with an on-demand nearby-block scan cached briefly per entity. Curios, Create, Aether, and Dimensional Doors compatibility is out of scope.
- The `.16` prerelease introduced the integration; `.17` updates the per-entity cache to refresh on eye-block and dimension changes and rejects cache data from a later world tick. This prevents stale results across provider/air-profile boundaries and dimension transitions.
- The `.17` cache fix refreshes results when the entity changes eye block or dimension and rejects entries from a later world tick.
- The `.19` HUD fix leaves armor/health-tier placement alone when air bubbles appear and moves the thirst bar upward by one row while the air bar is visible. This corrects `.17`'s armor-row adjustment, which the user observed shifting armor rather than the thirst bar.
- These changes have not yet had in-game validation; verify RED air and oxygen loss at distances up to 3 blocks from source and flowing lava, confirm logs do not grant GREEN air, and separately retest height profiles, stable air-bar behavior, provider boundaries, armor/air/thirst-bar spacing, respirator behavior, air bladder recharge/refill, lantern locking, signal torch toggling, and chest loot. Nether ambient air remains a known issue deferred for a later fix.
- The integration is included in `.16`; follow-up fixes are included in `.17`, `.19`, `.20`, `.22`, `.23`, `.24`, and the upcoming test build. These are prereleases pending in-game testing.

## Release and edit notes

`v1.20.1-2.4.7-fabric` through `.14` were prereleases for testing. `.15` is the first full release. `.16` adds the Thin Air integration, with `.17`, `.19`, `.20`, `.22`–`.27` follow-up fixes; these remain prereleases pending in-game testing. Use Java 17 and install the required Fabric dependencies specified in `fabric.mod.json`, including Forge Config API Port 8.0.3 and Cardinal Components.

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
| `v1.20.1-2.4.7-fabric.9` | Restores item tooltip details for hydration/saturation, temperature/resistance modifiers, body healing, shade, and consumable effects. User verified hydration, Rotten Flesh and Refreshing Canteen tooltips, and the correct colors/values for Snow and Desert armor. |
| `v1.20.1-2.4.7-fabric.10` | Reworks HUD render anchors and health-bar rendering; places broken hearts in the visible health row at foreground depth, gives shield hearts separate alternating rows, and moves armor only while shield rows are present. |
| `v1.20.1-2.4.7-fabric.11` | Restores Fabric cutout rendering for the survival plants, corrects fern biome/substrate placement, increases plant feature frequency to once per 15 chunks, and enables water plants on farmland and grass. |
| `v1.20.1-2.4.7-fabric.12` | Removes unsupported Origins integration and restores the Serene Seasons out-of-season bonemeal warning, verified in-game. |
| `v1.20.1-2.4.7-fabric.13` | Fixes the sewing table not opening (`ExtendedScreenHandlerFactory` mismatch), restores vanilla heater/cooler fuel entries, fixes thermal fuel tick timing/persistence, and fixes sewing-table preview/advancement-timing bugs. User-verified: heater, cooler, sewing table opening, warm/cold string recipes, and coat application to armor. |
| `v1.20.1-2.4.7-fabric.14` | Fixes Overflowing Bars HUD compatibility: shield hearts no longer overlap the armor row, broken hearts render correctly instead of disappearing, and a phantom 10-pixel gap above the thirst bar is corrected. All three fixes are user-verified in-game with Overflowing Bars installed and enabled. |
| `v1.20.1-2.4.7-fabric.15` | First full (non-prerelease) release. Final audit of the excluded Forge event-subscriber files found no remaining behavioral gaps; the port is considered complete pending further in-game testing, particularly multiplayer/dedicated-server scenarios. No code changes were required. |
| `v1.20.1-2.4.7-fabric.16` | Adds Thin Air's air-quality mechanics and content for Fabric 1.20.1, including dimension/height profiles, air providers, Safety Lanterns, Signal Torches, Air Bladders, Soulfire Bottles, and a Trinkets Respirator. Prerelease pending in-game validation. |
| `v1.20.1-2.4.7-fabric.17` | Refreshes cached air quality when the entity changes eye block or dimension and rejects stale cache entries after a world-time reset; raises the armor row while the air bar is visible. Prerelease pending in-game validation. |
| `v1.20.1-2.4.7-fabric.18` | Follow-up air-quality HUD test build; its armor-row adjustment was superseded by `.19` after in-game feedback showed it moved armor rather than the thirst bar. |
| `v1.20.1-2.4.7-fabric.19` | Keeps armor-row movement exclusive to additional health rows and moves the thirst bar upward while air bubbles are displayed. Prerelease pending in-game validation. |
| `v1.20.1-2.4.7-fabric.20` | Adds flowing lava as a RED air provider; Nether ambient air remains YELLOW for gradual air loss. Prerelease pending in-game validation. |
| `v1.20.1-2.4.7-fabric.22` | Reapplies air-quality changes once after vanilla air handling, preserves the drowning check, and loads dimension-profile JSON from any datapack namespace. |
| `v1.20.1-2.4.7-fabric.23` | Removes the invalid `minecraft:flowing_lava` tag entry and prevents client-side vanilla air refill from fighting the server-synced air value. Prerelease pending in-game validation. |
| `v1.20.1-2.4.7-fabric.24` | Temporarily adds oak logs as GREEN providers and adjusts Nether YELLOW air drain; oak log support is removed in `.25`, while Nether behavior is deferred for further investigation. |
| `v1.20.1-2.4.7-fabric.25` | Removes oak logs as GREEN providers and recognizes source/flowing lava via `FluidTags.LAVA` for RED air within the configured provider radius (default 3 blocks). Prerelease pending in-game validation. |
| `v1.20.1-2.4.7-fabric.26` | Adds suffocation damage for air-quality-driven air loss (bad air pockets, lava fumes, Nether ambience) outside of water, since vanilla's own drowning damage only triggers while the entity's eyes are in water. Prerelease pending in-game validation. |
| `v1.20.1-2.4.7-fabric.27` | Makes the Nether's ambient YELLOW air quality apply directly in code instead of solely through the data-driven dimension profile JSON, so the Nether always behaves correctly even if that datapack-style profile ever fails to load; adds a dimension-key log for diagnosing the air-quality dimension loader. Prerelease pending in-game validation. |

### Latest released artifact

- File: `legendarysurvivaloverhaul-1.20.1-2.4.7-fabric.jar`
- Tag: `v1.20.1-2.4.7-fabric.27`
- SHA-256: `6AEBE9DA1DFD3A25779AAB1D24ACE76CED259C4F2483E0173C64EA53C287F861`
- Release page: <https://github.com/Atlasroar/LegendarySurvivalOverhaul/releases/tag/v1.20.1-2.4.7-fabric.27>

## Feature and compatibility notes

### Working in the current test slice

- Creative inventory registration and item icons.
- Core heater/chiller behavior and wearable armor, as visually confirmed by the user.
- Fabric player survival components and selected lifecycle/gameplay hooks.
- Server-data JSON reload listeners and 14-dataset client synchronization.
- Thirst, temperature, wetness, and body-damage HUD indicators.
- LSO shield/broken-heart HUD overlay (shield/armor separation and broken-heart foreground layering verified in-game).
- Cold-hunger food-bar overlay is visually confirmed in `.4`. Its active duration is managed by the temperature system, not by command duration overrides.
- Configured thirst exhaustion from jumping, successful block breaking, and attacking is user-validated in `.5`; attack food exhaustion also works, and Creative/Spectator do not lose hydration from those triggers.
- Localized body damage and healing items are user-validated in `.6`; hydration consumables work. The user confirmed melon juice applies Cold for 60 seconds and glistering melon juice applies Cold II for 3 minutes, with the stronger effect replacing the weaker one and temperature behavior responding accordingly.
- Death-respawn temperature immunity is user-validated in `.7` at the configured default 90-second duration and remains active across Nether dimension changes.
- The user confirmed the configured F3 debug filter both hides debug values when enabled and restores them when disabled. Verified on both states.
- Item tooltips for hydration, consumable effects, and equipment temperature/resistance are restored and user-verified, including the Snow and Desert armor tooltip colors and values.
- Optional Overflowing Bars shared-height integration, including shield-heart/armor-row separation, broken-heart rendering, and the corrected right-side vehicle-row quirk — all user-verified in-game in `.14`.
- The final Forge event-subscriber audit (`.15`) found all excluded Forge-only handlers either have Fabric equivalents, are intentional feature drops, or are justified platform adaptations; no code changes were required.

### Not yet restored or not fully validated

- Multiplayer/dedicated-server behavior beyond the specific networking paths already ported.

Forge datagen execution remains omitted as a developer workflow; generated resources used at runtime are checked into the repository. Origins and Meds and Herbs are intentionally unsupported, and Supplementaries requires no special adapter for its standard item finish-use path.

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
