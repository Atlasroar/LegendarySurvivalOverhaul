package sfiomn.legendarysurvivaloverhaul.common.capabilities;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;

public final class PlayerSurvivalComponents implements EntityComponentInitializer
{
	public static final ComponentKey<PlayerSurvivalComponent> PLAYER_SURVIVAL = ComponentRegistry.getOrCreate(
			new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "player_survival"),
			PlayerSurvivalComponent.class
	);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry)
	{
		registry.registerFor(Player.class, PLAYER_SURVIVAL, player -> new PlayerSurvivalComponent());
	}
}
