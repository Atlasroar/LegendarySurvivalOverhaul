package sfiomn.legendarysurvivaloverhaul.common.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import sfiomn.legendarysurvivaloverhaul.LegendarySurvivalOverhaul;
import sfiomn.legendarysurvivaloverhaul.api.bodydamage.BodyPartEnum;
import sfiomn.legendarysurvivaloverhaul.common.capabilities.bodydamage.BodyDamageCapability;
import sfiomn.legendarysurvivaloverhaul.util.CapabilityUtil;

import java.util.Collection;

public class BodyDamageCommand extends CommandBase
{
	public BodyDamageCommand() {
		super(Commands.literal("bodydamage")
				.requires((p_198521_0_) -> p_198521_0_.hasPermission(2))
				.then(Commands.argument("target", EntityArgument.entities())
				.then(Commands.literal("set")
					.then(Commands.argument("BodyPart", StringArgumentType.word())
						.then(Commands.argument("Health", FloatArgumentType.floatArg(0))
							.executes(src -> new BodyDamageCommand().set(src.getSource(), EntityArgument.getEntities(src, "target"), getBodyPart(src), FloatArgumentType.getFloat(src, "Health")))))
					.then(Commands.literal("ALL")
							.then(Commands.argument("Health", FloatArgumentType.floatArg(0))
							.executes(src -> new BodyDamageCommand().setAll(src.getSource(), EntityArgument.getEntities(src, "target"), FloatArgumentType.getFloat(src, "Health"))))))
				.then(Commands.literal("heal")
						.then(Commands.argument("BodyPart", StringArgumentType.word())
								.then(Commands.argument("Health", FloatArgumentType.floatArg(0))
										.executes(src -> new BodyDamageCommand().heal(src.getSource(), EntityArgument.getEntities(src, "target"), getBodyPart(src), FloatArgumentType.getFloat(src, "Health")))))
						.then(Commands.literal("ALL")
								.then(Commands.argument("Health", FloatArgumentType.floatArg(0))
										.executes(src -> new BodyDamageCommand().healAll(src.getSource(), EntityArgument.getEntities(src, "target"), FloatArgumentType.getFloat(src, "Health"))))))
				.then(Commands.literal("get")
						.then(Commands.argument("BodyPart", StringArgumentType.word())
								.executes(src -> new BodyDamageCommand().get(src.getSource(), EntityArgument.getEntities(src, "target"), getBodyPart(src))))
						.then(Commands.literal("ALL")
								.executes(src -> new BodyDamageCommand().getAll(src.getSource(), EntityArgument.getEntities(src, "target")))))
				));
	}

	private static BodyPartEnum getBodyPart(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		String name = StringArgumentType.getString(context, "BodyPart");
		try {
			return BodyPartEnum.valueOf(name.toUpperCase(java.util.Locale.ROOT));
		} catch (IllegalArgumentException exception) {
			throw new SimpleCommandExceptionType(Component.literal("Unknown body part: " + name)).create();
		}
	}

	public int get(CommandSourceStack source, Collection<? extends Entity> entities, BodyPartEnum bodyPart) {
		try {
			StringBuilder reply = new StringBuilder();
			for (Entity entity: entities) {
				if (entity instanceof Player player && source.getEntity() instanceof Player) {
					BodyDamageCapability cap = CapabilityUtil.getBodyDamageCapability(player);
					float bodyPartMaxHealth = cap.getBodyPartMaxHealth(bodyPart);
					float bodyPartHealth = bodyPartMaxHealth - cap.getBodyPartDamage(bodyPart);

					reply.append("Player ").append(player.getName().getString()).append("\n")
							.append("Body Limb ").append(bodyPart.name())
							.append(" Health : ").append(bodyPartHealth)
							.append("/").append(bodyPartMaxHealth)
							.append("\n");
					source.sendSuccess(() -> Component.literal(reply.toString()), false);
				}
			}
		}
		catch(Exception e) 
		{
			LegendarySurvivalOverhaul.LOGGER.error(e.getMessage());
		}
		return Command.SINGLE_SUCCESS;
	}

	public int getAll(CommandSourceStack source, Collection<? extends Entity> entities) {
		try {
			StringBuilder reply = new StringBuilder();
			for (Entity entity: entities) {
				if (entity instanceof Player player && source.getEntity() instanceof Player) {
					BodyDamageCapability cap = CapabilityUtil.getBodyDamageCapability(player);
					reply.append("Player ").append(player.getName().getString()).append("\n");
					for (BodyPartEnum bodyPart : BodyPartEnum.values()) {
						float bodyPartMaxHealth = cap.getBodyPartMaxHealth(bodyPart);
						float bodyPartHealth = bodyPartMaxHealth - cap.getBodyPartDamage(bodyPart);

						reply
								.append("Body Limb ").append(bodyPart.name())
								.append(" Health : ").append(bodyPartHealth)
								.append("/").append(bodyPartMaxHealth)
								.append("\n");
					}

					source.sendSuccess(() -> Component.literal(reply.toString()), false);
				}
			}
		}
		catch(Exception e)
		{
			LegendarySurvivalOverhaul.LOGGER.error(e.getMessage());
		}
		return Command.SINGLE_SUCCESS;
	}

	private int set(CommandSourceStack src, Collection<? extends Entity> entities, BodyPartEnum bodyPart, float healthValue) throws CommandSyntaxException {

		for (Entity entity: entities) {
			if (entity instanceof Player player) {
				BodyDamageCapability cap = CapabilityUtil.getBodyDamageCapability(player);
				cap.setBodyPartDamage(bodyPart, cap.getBodyPartMaxHealth(bodyPart) - healthValue);
			}
		}
		return Command.SINGLE_SUCCESS;
	}

	private int setAll(CommandSourceStack src, Collection<? extends Entity> entities, float healthValue) throws CommandSyntaxException {

		for (Entity entity: entities) {
			if (entity instanceof Player player) {
				BodyDamageCapability cap = CapabilityUtil.getBodyDamageCapability(player);
				for (BodyPartEnum bodyPart : BodyPartEnum.values())
					cap.setBodyPartDamage(bodyPart, cap.getBodyPartMaxHealth(bodyPart) - healthValue);
			}
		}
		return Command.SINGLE_SUCCESS;
	}

	private int heal(CommandSourceStack src, Collection<? extends Entity> entities, BodyPartEnum bodyPart, float healthValue) throws CommandSyntaxException {

		for (Entity entity: entities) {
			if (entity instanceof Player player) {
				BodyDamageCapability cap = CapabilityUtil.getBodyDamageCapability(player);
				cap.setBodyPartDamage(bodyPart, cap.getBodyPartDamage(bodyPart) - healthValue);
			}
		}
		return Command.SINGLE_SUCCESS;
	}

	private int healAll(CommandSourceStack src, Collection<? extends Entity> entities, float healthValue) throws CommandSyntaxException {

		for (Entity entity: entities) {
			if (entity instanceof Player player) {
				BodyDamageCapability cap = CapabilityUtil.getBodyDamageCapability(player);
				for (BodyPartEnum bodyPart : BodyPartEnum.values())
					cap.setBodyPartDamage(bodyPart, cap.getBodyPartDamage(bodyPart) - healthValue);
			}
		}
		return Command.SINGLE_SUCCESS;
	}
}
