Legendary Survival Overhaul - Fabric
====================================

Minecraft version: 1.20.1
Java runtime: Java 17

The mod targets Java 17, as required for Minecraft 1.20.1. The current Gradle
wrapper uses Gradle 8.12 and Fabric Loom 1.10.5, so the build can run on JDK 17
as well.

Forge Config API Port (Fabric, Minecraft 1.20.1) is a required runtime mod for
the configuration screens and settings. The build uses version 8.0.3.

Build the mod
-------------

Run `gradlew build` on Windows or `./gradlew build` on macOS/Linux. The
resulting Fabric mod jar is created in `build/libs`.

Run a development client
------------------------

Run `gradlew runClient` on Windows or `./gradlew runClient` on macOS/Linux.

Configuration files are stored in `config/legendarysurvivaloverhaul`.

Port status
-----------

The current Fabric build is an early compatibility slice, not a feature-complete
release. The released HUD test build restores thirst, temperature, wetness, and
body-damage indicators. The health-bar replacement, cold-hunger food overlay,
some Forge event handlers, data generators, and selected optional integrations
are still excluded or awaiting Fabric replacements.

The client has been built and launched to an integrated world. Back up worlds
before testing prereleases. See the versioned port wiki for the migration history,
release notes, dependencies, test instructions, and remaining work:

https://github.com/Atlasroar/LegendarySurvivalOverhaul/blob/1.20.1/docs/Fabric-Port-Wiki.md
