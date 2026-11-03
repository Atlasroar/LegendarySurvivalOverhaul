import json
import struct
import sys
import zipfile


with zipfile.ZipFile(sys.argv[1]) as jar:
    metadata = json.loads(jar.read("fabric.mod.json"))
    assert not {"scorchful", "frostiful", "thermoo"} & metadata["depends"].keys()
    assert not any(name.startswith("META-INF/jars/") and
                   any(mod in name for mod in ("scorchful", "frostiful", "thermoo"))
                   for name in jar.namelist())
    for name in jar.namelist():
        if name.endswith(".json") and name.startswith(("assets/", "data/")):
            json.loads(jar.read(name))
    for part, resistance in {"helmet": 1.5, "chestplate": 3, "leggings": 2.5, "boots": 0.5}.items():
        path = f"data/legendarysurvivaloverhaul/legendarysurvivaloverhaul/temperature/items/snow_{part}.json"
        assert json.loads(jar.read(path))["cold_resistance"] == resistance
        image = jar.read(f"assets/legendarysurvivaloverhaul/textures/item/armor/snow_{part}.png")
        assert image.startswith(b"\x89PNG\r\n\x1a\n")
    for layer in (1, 2):
        image = jar.read(f"assets/legendarysurvivaloverhaul/textures/models/armor/snow_layer_{layer}.png")
        assert struct.unpack(">II", image[16:24]) == (64, 32)
    for lily in ("crimson_lily", "warped_lily"):
        assert jar.read(f"assets/legendarysurvivaloverhaul/textures/block/{lily}.png").startswith(b"\x89PNG")
        for path in (f"assets/legendarysurvivaloverhaul/blockstates/{lily}.json",
                     f"assets/legendarysurvivaloverhaul/models/item/{lily}.json",
                     f"data/legendarysurvivaloverhaul/loot_tables/blocks/{lily}.json",
                     f"data/legendarysurvivaloverhaul/worldgen/placed_feature/{lily}_patch.json"):
            json.loads(jar.read(path))
    assert json.loads(jar.read(
        "data/legendarysurvivaloverhaul/legendarysurvivaloverhaul/temperature/items/ice_skates.json"
    ))["cold_resistance"] == 0.5
    for license_name in ("Scorchful-LGPL-3.0.txt", "Frostiful-LGPL-3.0.txt", "nightconfig-GPL-3.0.txt"):
        assert len(jar.read("META-INF/licenses/" + license_name)) > 1000
    refmap = json.loads(jar.read("legendarysurvivaloverhaul-fabric-1.20.1-refmap.json"))
    skate = refmap["mappings"]["sfiomn/legendarysurvivaloverhaul/mixin/LivingEntityIceSkatingMixin"]
    assert "getFriction" not in skate["Lnet/minecraft/world/level/block/Block;getFriction()F"]
    assert "method_" in skate["travel"]
    assert "method_" in skate["aiStep"]
    assert "method_" in skate["push"]

print("WET_CONTENT_RESOURCES_CHECK_PASSED")
