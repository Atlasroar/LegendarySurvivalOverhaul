package sfiomn.legendarysurvivaloverhaul.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import sfiomn.legendarysurvivaloverhaul.api.data.json.JsonThirstBlock;
import sfiomn.legendarysurvivaloverhaul.api.thirst.ThirstUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.thirst.ThirstCapability;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

public class DrinkBlockFluidMessage
{
    // CLIENT to SERVER side message

    public DrinkBlockFluidMessage()
    {
    }

    public static void encode(DrinkBlockFluidMessage message, FriendlyByteBuf buffer)
    {
    }

    public static DrinkBlockFluidMessage decode(FriendlyByteBuf buffer)
    {
        return new DrinkBlockFluidMessage();
    }

    public static void DrinkWaterOnServer(ServerPlayer player) {
        ThirstCapability thirst = CapabilityUtil.getThirstCapability(player);
        if (player.isCreative() || player.isSpectator() || !Config.Baked.thirstEnabled
                || !ThirstUtil.isThirstActive(player) || thirst.isHydrationLevelAtMax())
            return;

        JsonThirstBlock jsonFluidThirst = ThirstUtil.getFluidThirstLookedAt(player, 3.0);

        if (jsonFluidThirst == null)
            return;

        ThirstUtil.takeDrink(player, jsonFluidThirst.hydration, jsonFluidThirst.saturation, jsonFluidThirst.effects);
    }
}
