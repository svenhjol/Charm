package svenhjol.charm.mixin.snow_storms;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.snow_storms.SnowStorms;

@Mixin(LivingEntity.class)
public abstract class SetEntityFreezingMixin extends Entity {

    public SetEntityFreezingMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * After removing frozen ticks, check if environmental conditions are correct for adding frozen ticks.
     * This is handled by the Snowstorms module.
     */
    @Inject(
        method = "tickMovement",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;setFrozenTicks(I)V",
            shift = At.Shift.AFTER,
            ordinal = 1
        )
    )
    private void hookTickMovementSetFrozenTicks(CallbackInfo ci) {
        if (SnowStorms.shouldFreezeEntity((LivingEntity)(Object)this))
            this.setFrozenTicks(Math.min(this.getMinFreezeDamageTicks(), this.getFrozenTicks() + 3));
    }
}
