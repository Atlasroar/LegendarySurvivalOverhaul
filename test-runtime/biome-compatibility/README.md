# Biome compatibility regression check

This opt-in Fabric development-server check does not ship in LSO. It preserves
native biome climates and exercises datapack-only namespaces, reload replacement,
invalid-entry rejection, dry/wet overrides and biome packet encode/decode/application.
It also checks equipped Respirator protection through both vanilla Nausea
application overloads without VanillaBackport, the config toggle, and tooltip
line visibility. Protection prevents new applications; it does not cure an
already active Nausea effect. The beta.1 `respiratorBlocksSulfurNausea` file key
is retained for existing saved preferences; its menu label is now
**Respirator Blocks Nausea**.
The Terralith-namespaced fixture is deliberately tested **without** Terralith.
It is a loader/sync regression check, not a real Terralith/Tectonic worldgen test.

With JDK 17, from the repository root in PowerShell:

```powershell
.\gradlew.bat -I test-runtime\biome-compatibility\check.gradle jarBiomeCompatibilityCheck
```

Prepare `build\biome-compatibility-server` with an accepted `eula.txt` and
`server.properties` using a free port and a flat test world. Copy
`test-datapacks\biome-compatibility-test` into that server's `world\datapacks`
directory before its first start. Do not add Terralith to this isolated test.

```powershell
.\gradlew.bat -I test-runtime\biome-compatibility\check.gradle runServer
```

Success prints `BIOME_COMPATIBILITY_CHECK_PASSED` and stops the server. One
intentional invalid-codec error should include `datapack_only:invalid`. A normal
Gradle exit alone is not a passing assertion: verify the marker and absence of
`AssertionError`.

The isolated server lives under `build`; this does not alter `run` or a player's
world. Never distribute the assertion mod with a release.
