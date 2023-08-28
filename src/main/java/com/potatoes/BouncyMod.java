package com.potatoes;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.AtomicDouble;
import com.mojang.brigadier.arguments.DoubleArgumentType;

public final class BouncyMod implements ModInitializer {

	public static final String MOD_ID = "bouncy";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final AtomicDouble TICKRATE_MULTIPLIER = new AtomicDouble(1D);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing");
		CommandRegistrationCallback.EVENT
				.register((dispatcher, registryAccess, environment) -> dispatcher

						/*
						 * /tickrate
						 */
						.register(CommandManager.literal("tickrate")

								/*
								 * /tickrate get
								 */
								.then(CommandManager.literal("get").executes(context -> {
									double percentage = TICKRATE_MULTIPLIER.get() * 100;
									context.getSource().sendMessage(Text.literal("Tickrate is " + percentage + "%"));

									return 1;
								}))

								/*
								 * /tickrate set [<percentage>]
								 */
								.then(CommandManager.literal("set")
										.then(CommandManager.argument("percentage", DoubleArgumentType.doubleArg())
												.executes(context -> {
													double percentage = DoubleArgumentType.getDouble(context,
															"percentage");
													TICKRATE_MULTIPLIER.set(percentage / 100);

													context.getSource()
															.sendMessage(Text
																	.literal("Tickrate set to " + percentage + "%"));

													return 1;
												})))

						));
	}

	public static void trace(String s) {
		LOGGER.info(s);
	}
}