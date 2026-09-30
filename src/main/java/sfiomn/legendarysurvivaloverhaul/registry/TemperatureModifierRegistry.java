package sfiomn.legendarysurvivaloverhaul.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryKey;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.temperature.AttributeModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.DynamicModifierBase;
import sfiomn.legendarysurvivaloverhaul.api.temperature.ModifierBase;
import sfiomn.legendarysurvivaloverhaul.common.temperature.*;
import sfiomn.legendarysurvivaloverhaul.common.temperature.attribute.CoatAttributeModifier;
import sfiomn.legendarysurvivaloverhaul.common.temperature.attribute.ItemAttributeModifier;
import sfiomn.legendarysurvivaloverhaul.common.temperature.dynamic.MountDynamicModifier;
import sfiomn.legendarysurvivaloverhaul.common.temperature.dynamic.TemperatureResistanceModifier;

public class TemperatureModifierRegistry
{
	public static final ResourceLocation MODIFIERS_RESOURCE = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "temperature_modifiers");
	public static final ResourceLocation DYNAMIC_MODIFIERS_RESOURCE = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "dynamic_temperature_modifiers");
	public static final ResourceLocation ITEM_ATTRIBUTE_MODIFIERS_RESOURCE = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "item_attribute_temperature_modifiers");

	public static final Registry<ModifierBase> MODIFIERS_REGISTRY = createRegistry(MODIFIERS_RESOURCE);
	public static final Registry<DynamicModifierBase> DYNAMIC_MODIFIERS_REGISTRY = createRegistry(DYNAMIC_MODIFIERS_RESOURCE);
	public static final Registry<AttributeModifierBase> ITEM_ATTRIBUTE_MODIFIERS_REGISTRY = createRegistry(ITEM_ATTRIBUTE_MODIFIERS_RESOURCE);
	public static final FabricDeferredRegister<ModifierBase> MODIFIERS = FabricDeferredRegister.create(MODIFIERS_REGISTRY);
	public static final FabricDeferredRegister<DynamicModifierBase> DYNAMIC_MODIFIERS = FabricDeferredRegister.create(DYNAMIC_MODIFIERS_REGISTRY);
	public static final FabricDeferredRegister<AttributeModifierBase> ITEM_ATTRIBUTE_MODIFIERS = FabricDeferredRegister.create(ITEM_ATTRIBUTE_MODIFIERS_REGISTRY);

	// Base Modifiers
	public static final RegistryObject<ModifierBase> ALTITUDE = MODIFIERS.register("altitude", AltitudeModifier::new);
	public static final RegistryObject<ModifierBase> ATTRIBUTE = MODIFIERS.register("attribute", AttributeModifier::new);
	public static final RegistryObject<ModifierBase> BIOME = MODIFIERS.register("biome", BiomeModifier::new);
	public static final RegistryObject<ModifierBase> BLOCKS = MODIFIERS.register("blocks", BlockModifier::new);
	public static final RegistryObject<ModifierBase> DIMENSION = MODIFIERS.register("dimension", DimensionModifier::new);
	public static final RegistryObject<ModifierBase> MOUNT = MODIFIERS.register("mount", MountModifier::new);
	public static final RegistryObject<ModifierBase> FREEZE = MODIFIERS.register("freeze", FreezeModifier::new);
	public static final RegistryObject<ModifierBase> ON_FIRE = MODIFIERS.register("on_fire", OnFireModifier::new);
	public static final RegistryObject<ModifierBase> PLAYER_HUDDLING = MODIFIERS.register("player_huddling", PlayerHuddlingModifier::new);
	public static final RegistryObject<ModifierBase> SPRINT = MODIFIERS.register("sprint", SprintModifier::new);
	public static final RegistryObject<ModifierBase> TIME = MODIFIERS.register("time", TimeModifier::new);
	public static final RegistryObject<ModifierBase> WEATHER = MODIFIERS.register("weather", WeatherModifier::new);
	public static final RegistryObject<ModifierBase> WETNESS = MODIFIERS.register("wetness", WetModifier::new);

	public static final RegistryObject<AttributeModifierBase> ITEM_ATTRIBUTE = ITEM_ATTRIBUTE_MODIFIERS.register("item_attribute", ItemAttributeModifier::new);
	public static final RegistryObject<AttributeModifierBase> COAT_ATTRIBUTE = ITEM_ATTRIBUTE_MODIFIERS.register("coat_attribute", CoatAttributeModifier::new);

	public static final RegistryObject<DynamicModifierBase> TEMPERATURE_RESISTANCE = DYNAMIC_MODIFIERS.register("temperature_resistance", TemperatureResistanceModifier::new);
	public static final RegistryObject<DynamicModifierBase> MOUNT_DYNAMIC = DYNAMIC_MODIFIERS.register("mount_dynamic", MountDynamicModifier::new);

	public static void register(){
		MODIFIERS.registerAll();
		DYNAMIC_MODIFIERS.registerAll();
		ITEM_ATTRIBUTE_MODIFIERS.registerAll();
	}

	private static <T> Registry<T> createRegistry(ResourceLocation identifier) {
		RegistryKey<Registry<T>> key = RegistryKey.createRegistryKey(identifier);
		return FabricRegistryBuilder.<T>createSimple(key).buildAndRegister();
	}
}
