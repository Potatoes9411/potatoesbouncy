package com.potatoes;

import com.potatoes.commands.TickrateCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;
import static com.mojang.brigadier.arguments.DoubleArgumentType.doubleArg;

public final class CommandRegistry {

        public static final void registerTickrateCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
                /*
                 * /tickrate get
                 */
                var get = literal("get").executes(TickrateCommand::get);
                /*
                 * /tickrate set [<percentage>]
                 */
                var set = literal("set").then(argument("percentage", doubleArg()).executes(TickrateCommand::set));
                var cmd = literal("tickrate").then(get).then(set);
                dispatcher.register(cmd);
        }

        public static final void registerCommands() {
                CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
                        registerTickrateCommand(dispatcher);
                });
        }

}
