Legendary Survival Overhaul - Fabric
====================================

Minecraft version: 1.20.1
Java runtime: Java 17

The mod targets Java 17, as required for Minecraft 1.20.1. The current Gradle
build uses Fabric Loom 1.14.10, which itself requires JDK 21 to run Gradle.
This build-tool requirement does not change the Java version required to run
the mod.

Build the mod
-------------

Run `gradlew build` on Windows or `./gradlew build` on macOS/Linux. The
resulting Fabric mod jar is created in `build/libs`.

Run a development client
------------------------

Run `gradlew runClient` on Windows or `./gradlew runClient` on macOS/Linux.

Configuration files are stored in `config/legendarysurvivaloverhaul`.
