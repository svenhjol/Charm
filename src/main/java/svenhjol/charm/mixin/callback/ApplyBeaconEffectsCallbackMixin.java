package svenhjol.charm.mixin.callback;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.ApplyBeaconEffectsCallback;

@Mixin(BeaconBlockEntity.class)
public class ApplyBeaconEffectsCallbackMixin extends BlockEntity {
    public ApplyBeaconEffectsCallbackMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    /**
     * Fires the {@link ApplyBeaconEffectsCallback} event when adding vanilla beacon effects.
     * This allows Charm modules to add additional effects to entities in range.
     */
    @Inject(
        method = "applyEffects",
        at = @At("HEAD")
    )
    private static void hookAddEffects(Level world, BlockPos pos, int level, MobEffect primary, MobEffect secondary, CallbackInfo ci) {
        if (world != null)
            ApplyBeaconEffectsCallback.EVENT.invoker().interact(world, pos, level, primary, secondary);
    }
}
