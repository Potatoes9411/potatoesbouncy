package com.potatoes.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.potatoes.ProjectileCollision;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.util.hit.HitResult;

@Mixin(EggEntity.class)
public class EggEntityMixin {

    private ProjectileEntity self = (ProjectileEntity) (Object) this;

    @Inject(method = "onCollision", at = @At("HEAD"), cancellable = true)
    protected void onCollision(HitResult result, CallbackInfo c) {
        ProjectileCollision.onCollision(self, result, c);
    }

}
