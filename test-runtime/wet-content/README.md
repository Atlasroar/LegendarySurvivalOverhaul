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

The fixture explicitly requires Scorchful/Frostiful/Thermoo absent to prove
the new content is standalone. This is not a coexistence test.

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
python test-runtime\wet-content\check_resources.py build\libs\legendarysurvivaloverhaul-fabric-1.20.1-3.2.1.jar
.\gradlew.bat --console=plain -PwithoutOptionalIntegrations -PwithWeathering -I test-runtime\weathering-storms\check.gradle jarWeatheringStormCheck runServer
```

The Weathering fixture additionally verifies wetness and snowstorm cooling
multiply to 3x the dry baseline. Config regressions cover all 264 optional
editor bindings. Render/gameplay feel and separate-client multiplayer
remain in-game validation follow-ups.
