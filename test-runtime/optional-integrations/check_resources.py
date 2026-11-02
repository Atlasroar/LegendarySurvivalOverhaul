import json
import sys
import zipfile


REMOVED = {
    "alexsmobs", "atmospheric", "betterendforge", "born_in_chaos_v1", "byg",
    "call_of_yucutan", "cataclysm", "crabbersdelight", "create",
    "create_confectionery", "crockpot", "curios", "decorative_blocks",
    "endergetic", "farmersrespite", "forge", "hearthandharvest", "seasonals",
    "survival_instinct", "tfc", "upgrade_aquatic", "valhelsia_structures",
    "wardrobe", "quark",
}
DRINKS = {
    "apple_cider", "chicken_soup", "fruit_salad", "hot_cocoa", "melon_juice",
    "melon_popsicle", "milk_bottle", "pumpkin_soup", "tomato", "tomato_sauce",
    "vegetable_soup",
}


with zipfile.ZipFile(sys.argv[1]) as jar:
    names = set(jar.namelist())
    for name in names:
        parts = name.split("/")
        if parts[0] == "data":
            assert parts[1] not in REMOVED, f"Obsolete namespace still packaged: {name}"
            assert "/forge/" not in name and "/loot_modifiers/" not in name, name
        assert not name.startswith("assets/quark/"), name
        if name.startswith("data/") and name.endswith(".json"):
            content = jar.read(name).decode("utf-8")
            json.loads(content)
            assert "forge:" not in content, f"Forge-only reference retained: {name}"
    for drink in DRINKS:
        path = f"data/farmersdelight/legendarysurvivaloverhaul/thirst/consumables/{drink}.json"
        data = json.loads(jar.read(path))
        assert isinstance(data, list) and data, path
        assert all(isinstance(entry["hydration"], int) for entry in data), path
    stove = json.loads(jar.read(
        "data/farmersdelight/legendarysurvivaloverhaul/temperature/blocks/stove.json"))
    assert {entry["properties"]["lit"] for entry in stove} == {"true", "false"}
    assert any(name.startswith("data/betterend/") for name in names)
    metadata = json.loads(jar.read("fabric.mod.json"))
    for optional in ("trinkets", "fzzy_config", "fabric-language-kotlin"):
        assert optional not in metadata["depends"], optional
        assert optional in metadata["suggests"], optional
    assert not any(optional in name for name in names if name.startswith("META-INF/jars/")
                   for optional in ("trinkets", "fzzy", "kotlin"))

print("RESOURCE_CLEANUP_CHECK_PASSED")
