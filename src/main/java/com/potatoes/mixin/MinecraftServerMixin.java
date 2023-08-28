package com.potatoes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.potatoes.BouncyMod;

import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    // Represents the amount of milliseconds per tick
    @ModifyConstant(method = "runServer", constant = @Constant(longValue = 50L))
    private long ticks(long x) {
        return (long) (1000 / 20 / BouncyMod.TICKRATE_MULTIPLIER.get());
    }

}
