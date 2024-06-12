package svenhjol.charm.mixin.feature.casks;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.casks.Casks;

@Mixin(PotionBrewing.class)
public class PotionBrewingMixin {
    @Inject(
        method = "mix",
        at = @At("RETURN"),
        cancellable = true
    )
    private void hookMix(ItemStack itemStack, ItemStack itemStack2, CallbackInfoReturnable<ItemStack> cir) {
        var opt = Resolve.feature(Casks.class).handlers.restoreCustomPotionEffects(itemStack2, cir.getReturnValue());
        opt.ifPresent(cir::setReturnValue);
    }
}
