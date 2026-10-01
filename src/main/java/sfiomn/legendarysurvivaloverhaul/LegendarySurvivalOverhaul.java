package sfiomn.legendarysurvivaloverhaul;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyDamageUtil;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.BodyDamageDataManager;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.TemperatureDataManager;
import sfiomn.legendarysurvivaloverhaul.api.data.manager.ThirstDataManager;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;
import sfiomn.legendarysurvivaloverhaul.api.temperature.TemperatureUtil;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.api.wetness.WetnessUtil;
import sfiomn.legendarysurvivaloverhaul.common.integration.jsonConfig.JsonIntegrationConfigRegistration;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.ModCapabilities;
import sfiomn.legendarysurvivaloverhaul.common.listeners.*;
import sfiomn.legendarysurvivaloverhaul.common.events.CanteenInteractionHandler;
import sfiomn.legendarysurvivaloverhaul.common.events.FabricInteractionCallbacks;
import sfiomn.legendarysurvivaloverhaul.common.events.FabricSurvivalCallbacks;
import sfiomn.legendarysurvivaloverhaul.common.data.FabricReloadListener;
import sfiomn.legendarysurvivaloverhaul.network.FabricServerNetworkHandler;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.registry.*;
import sfiomn.legendarysurvivaloverhaul.util.internal.*;

import java.nio.file.Path;
import java.nio.file.Paths;

public class LegendarySurvivalOverhaul implements ModInitializer
{
	public static final String MOD_ID = "legendarysurvivaloverhaul";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static boolean betterWeatherLoaded;
	public static boolean betterDaysLoaded;
	public static boolean sereneSeasonsLoaded;
	public static boolean eclipticSeasonsLoaded;
	public static boolean terraFirmaCraftLoaded;
	public static boolean surviveLoaded;
	public static boolean curiosLoaded;
	public static boolean vampirismLoaded;
	public static boolean originsLoaded;
	public static boolean mutantMonstersLoaded;
	public static boolean supplementariesLoaded;
	public static boolean artifactsLoaded;
	public static boolean beachpartyLoaded;
	public static boolean meadowLoaded;
	public static boolean overflowingbarsLoaded;
	public static boolean weather2Loaded;
	public static boolean medsandherbsLoaded;
	public static boolean crayfishFurnitureLoaded;

	public static Path configPath;
	public static Path modConfigPath;
	public static Path modConfigJsons;
	public static Path modIntegrationConfigJsons;

	@Override
	public void onInitialize()
	{
		configPath = FabricLoader.getInstance().getConfigDir();
		modConfigPath = configPath.resolve(MOD_ID);
		modConfigJsons = modConfigPath.resolve("json");
		modIntegrationConfigJsons = modConfigJsons.resolve("integration");
		sereneSeasonsLoaded = FabricLoader.getInstance().isModLoaded("sereneseasons");
		betterDaysLoaded = FabricLoader.getInstance().isModLoaded("betterdays");
		overflowingbarsLoaded = FabricLoader.getInstance().isModLoaded("overflowingbars");

		Config.register();
		FabricServerNetworkHandler.register();
		registerContent();
		initializeRuntimeLogic();
		ModCapabilities.registerServerEvents();
		CanteenInteractionHandler.register();
		FabricInteractionCallbacks.register();
		FabricSurvivalCallbacks.register();
		registerIntegrations();

		BodyDamageUtilInternal.initMalusConfig();
		BodyDamageUtilInternal.initLimbEffects();
		if (Config.Baked.temperatureEnabled)
			MobEffectRegistry.registerBrewingRecipes();
	}

	private static void registerContent()
	{
		AttributeRegistry.register();
		BlockRegistry.register();
		ItemRegistry.register();
		MobEffectRegistry.register();
		BlockEntityRegistry.register();
		ContainerRegistry.register();
		ParticleTypeRegistry.register();
		RecipeRegistry.register();
		SoundRegistry.register();
		TemperatureModifierRegistry.register();
		FeatureRegistry.register();
		CreativeTabRegistry.register();
		EnchantmentRegistry.register();
		CommandRegistry.register();
	}

	private static void initializeRuntimeLogic()
	{
		TemperatureUtil.internal = new TemperatureUtilInternal();
		ThirstUtil.internal = new ThirstUtilInternal();
		BodyDamageUtil.internal = new BodyDamageUtilInternal();
		WetnessUtil.internal = new WetnessUtilInternal();
		HealthUtil.internal = new HealthUtilInternal();

		TemperatureConsumableListener temperatureConsumables = new TemperatureConsumableListener();
		TemperatureConsumableBlockListener temperatureConsumableBlocks = new TemperatureConsumableBlockListener();
		TemperatureBlockListener temperatureBlocks = new TemperatureBlockListener();
		TemperatureItemListener temperatureItems = new TemperatureItemListener();
		TemperatureBiomeListener temperatureBiomes = new TemperatureBiomeListener();
		TemperatureFuelItemListener temperatureFuelItems = new TemperatureFuelItemListener();
		TemperatureDimensionListener temperatureDimensions = new TemperatureDimensionListener();
		TemperatureMountListener temperatureMounts = new TemperatureMountListener();
		TemperatureOriginListener temperatureOrigins = new TemperatureOriginListener();
		ThirstConsumableListener thirstConsumables = new ThirstConsumableListener();
		ThirstBlockListener thirstBlocks = new ThirstBlockListener();
		BodyPartsDamageSourceListener bodyPartsDamageSources = new BodyPartsDamageSourceListener();
		BodyDamageHealingConsumableListener healingConsumables = new BodyDamageHealingConsumableListener();
		BodyPartResistanceItemListener bodyPartResistanceItems = new BodyPartResistanceItemListener();

		TemperatureDataManager.internalConsumable = temperatureConsumables;
		TemperatureDataManager.internalConsumableBlock = temperatureConsumableBlocks;
		TemperatureDataManager.internalBlock = temperatureBlocks;
		TemperatureDataManager.internalItem = temperatureItems;
		TemperatureDataManager.internalBiome = temperatureBiomes;
		TemperatureDataManager.internalFuelItem = temperatureFuelItems;
		TemperatureDataManager.internalDimension = temperatureDimensions;
		TemperatureDataManager.internalMount = temperatureMounts;
		TemperatureDataManager.internalOrigin = temperatureOrigins;
		ThirstDataManager.internalConsumable = thirstConsumables;
		ThirstDataManager.internalBlock = thirstBlocks;
		BodyDamageDataManager.internalBodyPartsDamageSource = bodyPartsDamageSources;
		BodyDamageDataManager.internalHealingConsumable = healingConsumables;
		BodyDamageDataManager.internalBodyResistanceItem = bodyPartResistanceItems;

		FabricReloadListener.register(id("temperature/consumables"), temperatureConsumables);
		FabricReloadListener.register(id("temperature/consumable_blocks"), temperatureConsumableBlocks);
		FabricReloadListener.register(id("temperature/blocks"), temperatureBlocks);
		FabricReloadListener.register(id("temperature/items"), temperatureItems);
		FabricReloadListener.register(id("temperature/biomes"), temperatureBiomes);
		FabricReloadListener.register(id("temperature/fuel_items"), temperatureFuelItems);
		FabricReloadListener.register(id("temperature/dimensions"), temperatureDimensions);
		FabricReloadListener.register(id("temperature/mounts"), temperatureMounts);
		FabricReloadListener.register(id("temperature/origins"), temperatureOrigins);
		FabricReloadListener.register(id("thirst/consumables"), thirstConsumables);
		FabricReloadListener.register(id("thirst/blocks"), thirstBlocks);
		FabricReloadListener.register(id("body_damage/damage_sources"), bodyPartsDamageSources);
		FabricReloadListener.register(id("body_damage/consumables"), healingConsumables);
		FabricReloadListener.register(id("body_damage/items"), bodyPartResistanceItems);
	}

	private static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}

	private static void registerIntegrations()
	{
		JsonIntegrationConfigRegistration.init(modIntegrationConfigJsons.toFile());
	}
}
