package svenhjol.charm.mixin.callback;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
        method = "applyPlayerEffects",
        at = @At("HEAD")
    )
    private static void hookAddEffects(World world, BlockPos pos, int level, StatusEffect primary, StatusEffect secondary, CallbackInfo ci) {
        if (world != null)
            ApplyBeaconEffectsCallback.EVENT.invoker().interact(world, pos, level, primary, secondary);
    }
}
