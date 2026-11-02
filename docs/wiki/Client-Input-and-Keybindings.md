# Client Input and Keybindings

Primary source: `registry/KeyMappingRegistry.java`, wired from `client/LegendarySurvivalOverhaulClient.onInitializeClient()`.

This page is a worked example of diagnosing and fixing a platform-level keybind conflict, and of the project's config/save migration convention. It documents [#42 "Release v2.6.3: fix Left Shift keybind hijack"](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/42) and [#43 "Release v2.6.4: migrate existing Left-Shift added_desc keybinds"](https://github.com/Atlasroar/LegendarySurvivalOverhaul/pull/43).

## Registered keybinds

```java
public static KeyMapping showAddedDesc;
public static KeyMapping showBodyHealth;

public static void register() {
    showAddedDesc = KeyBindingHelper.registerKeyBinding(
            create("added_desc", InputConstants.UNKNOWN.getValue()));
    showBodyHealth = KeyBindingHelper.registerKeyBinding(
            create("body_health", GLFW.GLFW_KEY_H));
}
```

`showAddedDesc` toggles the tooltip "added description" text on items such as the Thermometer and Coat (read-only `isDown()` checks — it does not consume or re-route the key event to anything else). `showBodyHealth` opens the body-health breakdown screen, bound to <kbd>H</kbd> by default.

## Root cause: vanilla `KeyMapping` only tracks one binding per physical key

Prior to v2.6.3, `added_desc` defaulted to `GLFW_KEY_LEFT_SHIFT`. This silently broke vanilla sneak and any other mod's keybind that was also bound to Shift. The cause is a genuine vanilla Minecraft limitation (not a Fabric or LSO bug) that is worth understanding before adding any new default keybind to this mod:

```java
// net.minecraft.client.KeyMapping (vanilla, unpatched — no Forge)
private static final Map<InputConstants.Key, KeyMapping> MAP = Maps.newHashMap();

public static void resetMapping() {
    MAP.clear();
    for (KeyMapping keymapping : ALL.values()) {
        MAP.put(keymapping.key, keymapping);
    }
}
```

`MAP` is keyed by physical key and holds a **single** `KeyMapping` value — not a list. `resetMapping()` iterates `ALL` and overwrites the slot for each physical key, so whichever binding is registered last for a given key is the only one that receives `click()`/`set()` notifications; every earlier binding sharing that key is silently starved of events. Forge patches this exact class with `KeyMappingLookup`, which supports a `List<KeyMapping>` per key via `IForgeKeyMapping` — but this project is a pure Fabric port with no Forge runtime, so it is fully exposed to vanilla's one-binding-per-key behavior. Any new keybind this mod registers with a non-`UNKNOWN` default risks silently hijacking that same physical key from vanilla or another mod.

**Takeaway for future keybinds:** default new `KeyMapping`s to `InputConstants.UNKNOWN.getValue()` (unbound) unless the key is truly exclusive to this mod (e.g. <kbd>H</kbd> for `body_health`, which nothing else vanilla uses by default). Let players opt in via Controls rather than picking a default that collides with an existing binding.

## Why changing the default alone wasn't enough

Minecraft persists keybinds by name string in `options.txt`, not by reading the mod's compiled-in default at every launch. A player who had already launched the game once with the old `added_desc` default had `key.legendarysurvivaloverhaul.added_desc:key.keyboard.left.shift` written into their `options.txt`. Shipping v2.6.3's default change did nothing for that save — their saved binding stayed on Left Shift, and the hijack continued. This is why the user's "bug is still not fixed" report after v2.6.3 was accurate, and why a second, runtime migration step (v2.6.4) was required.

## The migration pattern

```java
public static void migrateLegacyShiftBinding(Minecraft client) {
    Path marker = LegendarySurvivalOverhaul.modConfigPath
            .resolve(".added_desc_shift_unbind_migrated");
    if (Files.exists(marker))
        return;

    try {
        Files.createDirectories(marker.getParent());

        if (showAddedDesc.matches(GLFW.GLFW_KEY_LEFT_SHIFT, 0)) {
            showAddedDesc.setKey(InputConstants.UNKNOWN);
            KeyMapping.resetMapping();
            client.options.save();
            LegendarySurvivalOverhaul.LOGGER.info(
                    "Unbound the '{}' tooltip keybind from Left Shift " +
                    "(it was hijacking vanilla sneak and other keybinds " +
                    "sharing that key); rebind it in Controls if you want it.",
                    "added_desc");
        }

        Files.createFile(marker);
    } catch (IOException e) {
        LegendarySurvivalOverhaul.LOGGER.warn(
                "Could not write the added_desc keybind migration marker", e);
    }
}
```

Key points, reusable for any future one-time save/config fixup in this codebase:

- **Marker file**, not a config version number: a plain empty file at `LegendarySurvivalOverhaul.modConfigPath.resolve(".added_desc_shift_unbind_migrated")`. If it exists, the migration has already run (or deliberately doesn't need to) and the method returns immediately. This mirrors the project's existing migration conventions (e.g. the `.toml.forge-backup` pattern used by the legacy Forge Config API Port migration).
- **Only touch the binding if it still matches the old default.** `showAddedDesc.matches(GLFW.GLFW_KEY_LEFT_SHIFT, 0)` checks the *current* saved binding, not a version number — so a player who deliberately rebinds `added_desc` to Shift *after* upgrading is left alone; the marker file is written either way so the check only runs once.
- **`setKey(InputConstants.Key)` + `KeyMapping.resetMapping()`**: `setKey` updates the `KeyMapping` instance itself; `resetMapping()` is required to immediately refresh the static `MAP` lookup table described above so the unbind takes effect without waiting for the next full key-remap pass.
- **`client.options.save()`** persists the change back to `options.txt` immediately, so the fix survives the current session even if the player doesn't open Controls.
- No Forge-only `KeyMapping.getKey()` is used — only vanilla-available methods (`matches`, `setKey`, `isUnbound`, `saveString`, `getDefaultKey`) are safe to call on a pure Fabric/vanilla `KeyMapping`.

## Hook timing: why `ClientLifecycleEvents.CLIENT_STARTED`, not `onInitializeClient()`

```java
// client/LegendarySurvivalOverhaulClient.java
KeyMappingRegistry.register();
ClientLifecycleEvents.CLIENT_STARTED.register(KeyMappingRegistry::migrateLegacyShiftBinding);
```

`ClientModInitializer.onInitializeClient()` runs during mod bootstrap, **before** Minecraft has loaded `Options` from `options.txt`. Reading or comparing saved key bindings at that point would see only just-registered defaults, not the player's actual saved state. `ClientLifecycleEvents.CLIENT_STARTED` (from `net.fabricmc.fabric.api.client.event.lifecycle.v1`, already available through the full `fabric-api` bundle this project depends on) fires once `Minecraft` has finished its startup sequence, including options load — so this is the earliest safe point to inspect/correct a saved binding.

## Extension checklist

When adding a new keybind to this mod:

1. Default to `InputConstants.UNKNOWN.getValue()` unless you are certain the key is exclusive to LSO.
2. If you must ship a fix that changes an *existing* keybind's default, remember it will not retroactively apply to existing `options.txt` saves — pair it with a `CLIENT_STARTED`-gated, marker-file-guarded migration like the one above if correctness for existing installs matters.
3. Never call Forge-only `KeyMapping` methods (`getKey()`, etc.) — this is a pure Fabric port.

[Architecture and APIs](Architecture-and-APIs) | [Developer Hub](Developer-Hub) | [Releases and Versioning](Releases-and-Versioning)
