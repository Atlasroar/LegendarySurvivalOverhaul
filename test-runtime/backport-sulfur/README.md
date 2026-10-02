# Optional sulfur integration contract check

This **synthetic fixture is not VanillaBackport**. It uses that mod ID/class/method
shape solely in an isolated development server to exercise optional presence,
Mixin interception, equipped-mask checks, ambient biome lookup, provider priority,
Nether-rate drain and Air config toggles. It must never be installed alongside
the real mod or distributed to players.

The target contract was inspected in VanillaBackport's Minecraft 1.20.1
**1.2 development branch**, notably
`PotentSulfurBlockEntity.applyNauseaEffect(LivingEntity)` and biome
`minecraft:sulfur_caves`. It does not validate real geyser generation, gas
line-of-sight, upstream version conflicts, client visuals or remote sync.

With JDK 17:

```powershell
.\gradlew.bat -I test-runtime\backport-sulfur\check.gradle jarBackportSulfurCheck
```

Prepare `build\backport-sulfur-server` with accepted `eula.txt` and a
`server.properties` using a free port and a flat test world. Copy
`test-datapacks\backport-sulfur-contract-test` into `world\datapacks` before start.

```powershell
.\gradlew.bat -I test-runtime\backport-sulfur\check.gradle runServer
```

Verify `BACKPORT_SULFUR_CONTRACT_CHECK_PASSED` and no assertion/Mixin errors.
The check stops the server. Actual integration verification still requires a
sulfur-enabled VanillaBackport 1.2 development build and its matching Platform
dependency, on both client and server. Do not infer that older public releases
contain this content.
