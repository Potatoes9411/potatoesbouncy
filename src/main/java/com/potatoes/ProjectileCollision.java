package com.potatoes;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import static com.potatoes.BouncyMod.trace;

public final class ProjectileCollision {

    // On projectile collision
    public static final void onCollision(ProjectileEntity self, HitResult result, CallbackInfo c) {
        trace("[onCollision]: init");
        trace("[onCollision]: projectile = " + self.getName());

        boolean hasMinimumSpeed = hasMinimumSpeed(self);
        trace("[onCollision]: hasMinimumSpeed = " + hasMinimumSpeed);

        if (!hasMinimumSpeed) {
            return;
        }

        if (result instanceof BlockHitResult blockHitResult) {
            trace("[onCollision]: result instanceof BlockHitResult");

            boolean isInsideBlock = blockHitResult.isInsideBlock();
            trace("[onCollision]: isInsideBlock = " + isInsideBlock);

            if (isInsideBlock) {
                return;
            }

            BlockState blockState = self.getWorld().getBlockState(blockHitResult.getBlockPos());
            trace("[onCollision]: block = " + blockState.getBlock().getName());

            @SuppressWarnings("deprecated")
            boolean blocksMovement = blockState.blocksMovement();
            trace("[onCollision]: blocksMovement = " + blocksMovement);

            if (!blocksMovement) {
                return;
            }

            Direction side = blockHitResult.getSide();
            trace("[onCollision]: side = " + side);

            changeProjectileMotion(self, side);
        } else if (result instanceof EntityHitResult entityHitResult) {
            trace("[onCollision]: result instanceof EntityHitResult");

            Entity entity = entityHitResult.getEntity();
            boolean isAttackable = entity.isAttackable();
            trace("[onCollision]: isAttackable" + isAttackable);

            if (isAttackable) {
                return;
            }

            changeProjectileMotion(self);
        }

        playBounceSound(self);
        c.cancel();
    }

    private static boolean hasMinimumSpeed(ProjectileEntity projectile) {
        double minimumSpeed = 0.25;
        return projectile.getVelocity().length() > minimumSpeed;
    }

    private static void playBounceSound(ProjectileEntity projectile) {
        if (projectile instanceof ArrowEntity) {
            projectile.playSoundIfNotSilent(SoundEvents.ENTITY_ARROW_HIT);
        } else if (projectile instanceof SnowballEntity) {
            projectile.playSoundIfNotSilent(SoundEvents.BLOCK_SNOW_STEP);
        } else if (projectile instanceof TridentEntity) {
            projectile.playSoundIfNotSilent(SoundEvents.ITEM_TRIDENT_HIT_GROUND);
        }
    }

    // in case it hit an unatackable entity
    private static void changeProjectileMotion(ProjectileEntity projectile) {
        Vec3d velocity = projectile.getVelocity();

        double x = velocity.x;
        double y = velocity.y;
        double z = velocity.z;

        double multiplier = 0.75; // projectile arbitrary weight
        x *= -multiplier;
        y *= -multiplier;
        z *= -multiplier;

        velocity = new Vec3d(x, y, z);
        projectile.setVelocity(velocity);
    }

    // in case it hit a block
    private static void changeProjectileMotion(ProjectileEntity projectile, Direction direction) {
        Vec3d velocity = projectile.getVelocity();

        double x = velocity.x;
        double y = velocity.y;
        double z = velocity.z;

        if (direction.getOffsetX() != 0) {
            x *= -1;
        } else if (direction.getOffsetY() != 0) {
            y *= -1;
        } else if (direction.getOffsetZ() != 0) {
            z *= -1;
        }

        velocity = new Vec3d(x, y, z);

        double multiplier = 0.75; // projectile arbitrary weight
        velocity = velocity.multiply(multiplier, multiplier, multiplier);

        projectile.setVelocity(velocity);
    }

}
