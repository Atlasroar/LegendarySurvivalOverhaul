# Optional integration and compatibility checks

These opt-in checks are not included in the published mod. The assertion mod
provides a synthetic `farmersdelight` loader alias to verify that the retained
Refabricated hydration and stove data load through the real resource listeners.
It does not replace testing with a real Farmer's Delight Refabricated installation.

With JDK 17, compile the fixture:

```powershell
.\gradlew.bat -I test-runtime\optional-integrations\check.gradle jarOptionalIntegrationsCheck
```

Use the isolated `build\optional-integrations-server` directory. Prepare
`eula.txt` according to the Minecraft EULA and `server.properties` with a flat
world, a free local-only port, and no existing player data. Run both cases:

```powershell
.\gradlew.bat -I test-runtime\optional-integrations\check.gradle runServer
.\gradlew.bat -PwithoutOptionalIntegrations -I test-runtime\optional-integrations\check.gradle runServer
```

Each run stops automatically after assertions. Verify
`OPTIONAL_INTEGRATIONS_CHECK_PASSED` with the expected `trinkets`/`fzzy` flags,
and no `AssertionError`. Assertions cover canteen-only anvil application,
loot-book eligibility, enchanting-table/trade exclusion, non-canteen loot
selection, accessory adapter registration or absence-safe held/helmet behavior,
retained recipes and potion NBT checks, and Farmer's Delight data loading.

After building the actual distributable, check its packaged resources:

```powershell
python test-runtime\optional-integrations\check_resources.py build\libs\legendarysurvivaloverhaul-fabric-1.20.1-3.0.0.jar
```

This rejects removed namespaces, Quark overrides, Forge resource references,
required optional libraries, or accidentally bundled integration JARs, and
verifies retained Refabricated/BetterEnd data.
