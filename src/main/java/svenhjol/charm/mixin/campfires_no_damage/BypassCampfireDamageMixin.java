package svenhjol.charm.mixin.campfires_no_damage;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.campfires_no_damage.CampfiresNoDamage;

@Mixin(CampfireBlock.class)
public abstract class BypassCampfireDamageMixin {
    /**
     * Defer entity collision blockstate to the bypassDamage check.
     * If the check passes, return early from the vanilla method.
     */
    @Inject(
        method = "entityInside",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookOnEntityCollision(BlockState state, Level worldIn, BlockPos pos, Entity entityIn, CallbackInfo ci) {
        if (CampfiresNoDamage.bypassDamage(state))
            ci.cancel();
    }
}
