package svenhjol.charm.mixin.variant_ladders;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.VariantLadders;

@Mixin(LivingEntity.class)
public abstract class ClimbTrapdoorMixin extends Entity {

    public ClimbTrapdoorMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * Checks trapdoor ladder is a variant ladder when player is climbing.
     * {@link VariantLadders#canEnterTrapdoor(World, BlockPos, BlockState)}
     *
     * If this mixin is disabled then custom trapdoors won't be climbable
     * at the top of a ladder.
     */
    @Inject(
        method = "canEnterTrapdoor",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookCanEnterTrapdoor(BlockPos pos, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (VariantLadders.canEnterTrapdoor(this.world, pos, state))
            cir.setReturnValue(true);
    }
}
