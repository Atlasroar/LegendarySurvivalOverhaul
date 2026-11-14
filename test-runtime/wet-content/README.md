# Wetness, lilies, fur and skates regression fixture

This opt-in Fabric assertion mod is never packaged in LSO. Use JDK 17.
Do not install it in a real world: server assertions replace blocks/biomes
in the test chunk, create a fake player, and stop the server on completion.
The client assertions load models/textures and stop the client automatically.

## Server

Create `build/wet-content-server/eula.txt` containing `eula=true` only after
accepting Minecraft's EULA. Use isolated `server.properties`, bound to
`127.0.0.1`, `server-port=0`, low view/simulation distance and a disposable
flat world. Do not copy real saves or expose this assertion server publicly.

```powershell
.\gradlew.bat --console=plain -PwithoutOptionalIntegrations -I test-runtime\wet-content\check.gradle jarWetContentCheck runServer
```

Require `WET_CONTENT_CHECK_PASSED`, not just Gradle exit zero. The game can
catch an assertion and shut down while Gradle still reports success.

Checks include cooling-only 1.5x in six biome types, custom multiplier,
wetness-disable behavior, preserved saturation-scaled target offset, no
overshoot, lily water bottles/canteens and non-water rejection, bursts,
Nether substrate refill, survival loot/support, real configured-feature
placement, preserved clothing armor, recipe registration, actual entity
travel friction and off-ice penalty application/removal.
The packaged-resource check verifies unchanged winter cold values, valid
JSON, required textures/licenses, remapped skating hooks and no new upstream
mod dependency.

The default fixture requires Scorchful/Frostiful/Thermoo absent to prove
the new content is standalone. It also installs a synthetic higher-priority
friction redirect and verifies composition both with and without LSO skates.
Damage assertions cover player/mob attribution, disabled server PvP, team
friendly-fire and Fabric damage cancellation. Wetness assertions cover held
Sponge in either hand, deactivation, global disable, protected mounts and
Creative/Spectator; with Trinkets installed they also equip a real Sponge.
Supporting-block lookup instrumentation verifies non-wearers skip LSO's
extra movement/tick lookups while unequipping still removes the walking
penalty. Vanilla and competing-mod friction remain unchanged without skates.

Equipment assertions require `EQUIPMENT_CHECK_PASSED`. Test-only method
instrumentation verifies unchanged six-slot equipment invokes neither built-in
temperature modifier after warmup (240 calls become 0 across 20 updates).
Parity checks cover all temperature/body slot attributes, copied/replaced and
in-place changed stacks, enchantments, damage, legacy coats, config baking,
dataset replacement/removal and in-place mutation, external modifier repair,
disabled systems, unequip and dynamic replacement temperature providers.
Body data and attribute reconciliation intentionally remain live each update.
An interleaved, warmed timing comparison runs 40,000 complete updates per
implementation with plain gear and again with coated/enchanted gear. The
reference recomputes resistance values and reconciles the same slot attributes
without caching. Timings are diagnostics, not pass thresholds or server-MSPT
predictions; method instrumentation is present in both paths.

To additionally register a real dynamic third-party attribute modifier:

```powershell
.\gradlew.bat --console=plain -PsimulateDynamicEquipment -I test-runtime\wet-content\check.gradle jarWetContentCheck runServer
```

Require `EQUIPMENT_DYNAMIC_CHECK_PASSED` as well. Unchanged equipment must keep
its original per-tick calculations in this run (240 calls remain 240), and
changing the custom modifier's external state must update attributes without
changing the stack. Rebuild without this property to restore the cache fixture.
These synthetic aliases and modifiers never ship in LSO.

Config-cache assertions require `CONFIG_CACHE_CHECK_PASSED`. They invoke the
same join/tick/stop handlers used by the Fabric callbacks and count real
snapshot construction: 20 unchanged polls perform 0 JSON rebuilds. Changes
keep the 20-tick cadence; joining players get fresh values without consuming
the pending broadcast for existing players. Coverage includes equal edits,
client-only changes, field/list replacement, rejected updates, restoration
and restart reset. With optional libraries installed, require
`CONFIG_FZZY_CACHE_CHECK_PASSED` for a real registered editor's update callback.
Send attempts are intercepted in the fixture, so these checks do not establish
remote packet delivery, UI interaction or operator permissions.

A warmed interleaved comparison prints timings for 2,000 unchanged polls per
implementation against the original JSON-building path. These are diagnostics,
not pass thresholds or server-wide performance predictions. The standalone
config regressions additionally cover TOML loads, wire-format parity and
custom mutable objects/list elements.

Wetness assertions require `WETNESS_LOOKUP_CHECK_PASSED`. They count actual
world lookups and compare particle decisions with the original lookup
sequence: partially submerged wet updates use 2 fluid reads instead of 4,
and retain their single block read. Each selected position is read exactly
once, with no cross-update cache. Checks cover same-position water/lava/air
changes, full saturation, fractional flowing water, cauldron fill/surface
boundaries, fire/lava drying, rain/snow, floating versus impulsed boats,
living/protected mounts and immediate START/END immunity. The optional-library
run additionally checks a real equipped Sponge.

The fixture registers a synthetic `meadow:wooden_water_cauldron` when absent
and temporarily enables LSO's Meadow detection flag for its boundary checks.
This validates the existing block-ID/level-property integration, not a real
Meadow installation. These test blocks and lookup/particle mixins never ship
in LSO.

To check the coexistence warning path with real optional libraries:

```powershell
.\gradlew.bat --console=plain -PsimulateThermalCoexistence -I test-runtime\wet-content\check.gradle jarWetContentCheck runServer
```

This adds synthetic `scorchful`/`frostiful` loader aliases, not their actual
mods. Require both the warning and `WET_CONTENT_CHECK_PASSED`; settings must
remain enabled. This does not establish real upstream runtime compatibility.
Rebuild without the property before the default/client checks to remove
the aliases. Fixture mixins and aliases never ship in LSO.

The fixture's `c:ice` tag deliberately includes vanilla glass to exercise a
tagged surface that is not an `IceBlock`. It checks skating and walking
penalty removal on that surface and all four vanilla ice variants. This
test-only tag never ships in LSO. Real IW Thin Ice movement/state coverage
lives in the weathering-storms fixture.

The server fixture also requires `AIR_SCAN_CHECK_PASSED`. Air checks compare
the optimized scan with both the original and v3.2.3 algorithms at fractional eye positions
and provider radii, including negative coordinates, overlapping lava/portals,
lit/unlit providers, flowing lava, eye fluids, maximum radii, unloaded chunks,
build-height boundaries, and entity-cache expiry/invalidation. Test-only mixins count actual provider
classifications and requires the empty-air scan to reduce thousands of calls
to a handful. They also count chunk-availability checks/fetches, require at
most one check/fetch pair per covered chunk, and confirm scans never request
creation of neighboring chunks. Missing in-range chunks remain absent.
A warmed uncached timing comparison against v3.2.3 is printed for diagnostics;
it is not a server-MSPT prediction or a timing-sensitive pass threshold.

## Client

```powershell
.\gradlew.bat --console=plain -PwithoutOptionalIntegrations -I test-runtime\wet-content\check.gradle copyWetContentClientCheck runClient
```

Require `WET_CONTENT_CLIENT_CHECK_PASSED`. Runs in
`build/wet-content-client`, checks baked item/lily textures, both skate
model layers and the English replacement name, then exits. It is not a
world/equipped-armor visual or multiplayer movement test.

## Related checks

```powershell
.\gradlew.bat --console=plain -PwithoutOptionalIntegrations -I src\test\resources\config-tests.gradle build configRegression configAdapterRegression
python test-runtime\wet-content\check_resources.py build\libs\legendarysurvivaloverhaul-fabric-1.20.1-3.2.4.jar
.\gradlew.bat --console=plain -PwithoutOptionalIntegrations -PwithWeathering -I test-runtime\weathering-storms\check.gradle jarWeatheringStormCheck runServer
```

The Weathering fixture additionally verifies wetness and snowstorm cooling
multiply to 3x the dry baseline. Config regressions cover all 264 optional
editor bindings. Render/gameplay feel and separate-client multiplayer
remain in-game validation follow-ups.
