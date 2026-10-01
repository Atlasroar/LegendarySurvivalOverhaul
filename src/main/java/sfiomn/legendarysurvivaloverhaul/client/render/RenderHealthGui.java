package sfiomn.legendarysurvivaloverhaul.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.health.HealthUtil;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.health.HealthCapability;
import sfiomn.legendarysurvivaloverhaul.common.integration.overflowingbars.OverflowingBarsUtil;
import sfiomn.legendarysurvivaloverhaul.config.Config;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.Random;

public class RenderHealthGui
{
	private static HealthCapability HEALTH_CAP = null;

	public static final ResourceLocation ICONS = new ResourceLocation(LegendarySurvivalOverhaul.MOD_ID, "textures/gui/overlay.png");
	protected static final ResourceLocation MINECRAFT_GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");
	private static final ResourceLocation OVERFLOWING_ICONS = new ResourceLocation(
			LegendarySurvivalOverhaul.MOD_ID, "textures/gui/overflowingbars_icons.png");

	public static void render(GuiGraphics guiGraphics, Player player, int width, int height) {
		if (Config.Baked.healthOverhaulEnabled
				&& !Minecraft.getInstance().options.hideGui
				&& !player.isCreative() && !player.isSpectator()) {
			Minecraft.getInstance().getProfiler().push("health");
			int vanillaHealthRows = Mth.ceil(player.getMaxHealth() / 20.0F);
			drawHealthBar(guiGraphics, player, width, height,
					OverflowingBarsUtil.leftHeight(39 + vanillaHealthRows * 10));
			Minecraft.getInstance().getProfiler().pop();

			RenderSystem.depthMask(true);
			RenderSystem.enableDepthTest();
		}
	}
	
	public static void drawHealthBar(GuiGraphics gui, Player player, int width, int height, int leftHeight) {
		if (HEALTH_CAP == null || player.tickCount % 20 == 0)
			HEALTH_CAP = CapabilityUtil.getHealthCapability(player);

		float shieldHealth = HEALTH_CAP.getShieldHealth();

		if (shieldHealth <= 0)
			return;

		int left = width / 2 - 91; // Same x offset as the health bar
		int top = height - leftHeight;
		drawShieldHearts(gui, left, top, shieldHealth);
	}

	public static int additionalHeartRows(Player player) {
		if (!Config.Baked.healthOverhaulEnabled
				|| Minecraft.getInstance().options.hideGui
				|| player.isCreative() || player.isSpectator()
				|| LegendarySurvivalOverhaul.overflowingbarsLoaded)
			return 0;

		if (HEALTH_CAP == null || player.tickCount % 20 == 0)
			HEALTH_CAP = CapabilityUtil.getHealthCapability(player);

		int shieldHearts = Mth.ceil(HEALTH_CAP.getShieldHealth() / 2.0F);
		return shieldHearts > 0 ? Mth.ceil(shieldHearts / 10.0F) : 0;
	}

	public static void renderBrokenHearts(GuiGraphics gui, Player player, int left, int top, int rowHeight,
										  float maxHealth) {
		if (!Config.Baked.appendBrokenShieldHeartsToHealthBar)
			return;

		int brokenHearts = HealthUtil.getEffectiveBrokenHearts(player);
		int firstBrokenHeart = Mth.ceil(maxHealth / 2.0F);
		for (int heart = firstBrokenHeart; heart < firstBrokenHeart + brokenHearts; heart++) {
			int x = left + heart % 10 * 8;
			int y = top - heart / 10 * rowHeight;
			renderHeart(gui, HeartType.BROKEN, x, y, 0, false);
		}
	}

	private static void drawShieldHearts(GuiGraphics gui, int left, int top, float shieldHealth) {
		int shieldHearts = Mth.ceil(shieldHealth / 2.0F);
		for (int heart = 0; heart < shieldHearts; heart++) {
			int x = left + heart % 10 * 8;
			int y = top - heart / 10 * 10;
			boolean halfHeart = heart == shieldHearts - 1 && shieldHealth < shieldHearts * 2;
			boolean orangeLayer = heart / 10 % 2 == 1;
			renderHeart(gui, HeartType.CONTAINER, x, y, 0, false);
			renderHeart(gui, orangeLayer ? HeartType.ORANGE_SHIELD : HeartType.SHIELD, x, y,
					orangeLayer ? 27 : 0, halfHeart);
		}
	}

	public static void renderHeart(GuiGraphics gui, HeartType heartType, int x, int y, int yTexture, boolean halfIcon) {
		gui.blit(heartType.location, x, y, heartType.getX(halfIcon), yTexture, 9, 9);
	}

	private enum HeartType {
		CONTAINER(MINECRAFT_GUI_ICONS_LOCATION, 0),
		SHIELD(MINECRAFT_GUI_ICONS_LOCATION, 8),
		BROKEN(ICONS, 0),
		ORANGE_SHIELD(OVERFLOWING_ICONS, 0);

		private final ResourceLocation location;
		private final int index;

		HeartType(ResourceLocation location, int index) {
			this.location = location;
			this.index = index;
		}

		private int getX(boolean halfIcon) {
			if (this == BROKEN)
				return 144;
			if (this == ORANGE_SHIELD)
				return halfIcon ? 9 : 0;

			int halfHeartOffset = this == SHIELD && halfIcon ? 1 : 0;
			return 16 + (this.index * 2 + halfHeartOffset) * 9;
		}
	}

}
