package svenhjol.charm.mixin.variant_ladders;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.variant_ladders.VariantLadders;

@Mixin(LivingEntity.class)
public abstract class ClimbTrapdoorMixin extends Entity {
    public ClimbTrapdoorMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    /**
     * Checks trapdoor ladder is a variant ladder when player is climbing.
     * {@link VariantLadders#canEnterTrapdoor(Level, BlockPos, BlockState)}
     *
     * If this mixin is disabled then custom trapdoors won't be climbable
     * at the top of a ladder.
     */
    @Inject(
        method = "trapdoorUsableAsLadder",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookCanEnterTrapdoor(BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (VariantLadders.canEnterTrapdoor(this.level, pos, state))
            cir.setReturnValue(true);
    }
}
