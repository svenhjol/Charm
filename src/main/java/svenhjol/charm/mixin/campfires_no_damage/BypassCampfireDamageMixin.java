package svenhjol.charm.mixin.campfires_no_damage;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
        method = "onEntityCollision",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookOnEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn, CallbackInfo ci) {
        if (CampfiresNoDamage.bypassDamage(state))
            ci.cancel();
    }
}
