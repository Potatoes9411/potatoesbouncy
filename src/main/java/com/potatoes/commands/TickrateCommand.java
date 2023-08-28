package com.potatoes.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.potatoes.BouncyMod;

import net.minecraft.server.command.ServerCommandSource;
import static net.minecraft.text.Text.literal;

public final class TickrateCommand {

    public static final int get(CommandContext<ServerCommandSource> context) {
        double percentage = BouncyMod.TICKRATE_MULTIPLIER.get() * 100;
        context.getSource().sendMessage(literal("Tickrate is " + percentage + "%"));
        return Command.SINGLE_SUCCESS;
    }

    public static final int set(CommandContext<ServerCommandSource> context) {
        double percentage = DoubleArgumentType.getDouble(context, "percentage");
        BouncyMod.TICKRATE_MULTIPLIER.set(percentage / 100);
        context.getSource()
                .sendMessage(literal("Tickrate set to " + percentage + "%"));
        return Command.SINGLE_SUCCESS;
    }

}
