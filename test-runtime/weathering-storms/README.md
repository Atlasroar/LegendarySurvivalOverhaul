# Immersive Weathering storm checks

These opt-in assertions use Atlasroar's actual Immersive Weathering **2.5.0**
release and its Moonlight dependency, not a synthetic storm implementation.
They are not packaged in LSO.

With JDK 17, compile the fixture:

```powershell
.\gradlew.bat -PwithoutOptionalIntegrations -I test-runtime\weathering-storms\check.gradle jarWeatheringStormCheck
```

Prepare `build\weathering-storm-server\eula.txt` according to the Minecraft EULA
and a fresh, isolated `server.properties`: bind `127.0.0.1`, use port `0`, a
small flat world, offline mode, and no existing player data. Do not use a real
world: the assertions modify weather, a chunk's biomes, and test roof blocks.

Run without IW, then with IW/Moonlight:

```powershell
.\gradlew.bat -PwithoutOptionalIntegrations -I test-runtime\weathering-storms\check.gradle runServer
.\gradlew.bat -PwithoutOptionalIntegrations -PwithWeathering -I test-runtime\weathering-storms\check.gradle runServer
```

Both runs stop automatically. Require `WEATHERING_STORM_CHECK_PASSED absent`
or `WEATHERING_STORM_CHECK_PASSED present`, respectively, and no assertion
failures.

Coverage includes real storm predicates, exact 2x temperature change, forced
cold/heat targets, roof holes and height-10/height-11 boundaries, transparent
glass, drink protection, ordinary snowfall, integration/immunity switches,
real wind impact interception, a full 200-tick hunger countdown, overlapping
wind/storm duration, and preservation of ordinary frostbite hunger.

The optional-library configuration regressions remain separate:

```powershell
.\gradlew.bat -PwithoutOptionalIntegrations -I src\test\resources\config-tests.gradle configRegression configAdapterRegression
```

Visual client behavior and separate-client multiplayer still need in-game
checks. These assertions run in Loom's development namespace; release mixin
refmaps must also retain the mapped wind `tick` and `LivingEntity.push` targets.
