package svenhjol.charm.mixin.no_potion_glint;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.no_potion_glint.NoPotionGlint;

@Mixin(PotionItem.class)
public class CheckPotionHasGlintMixin {

    /**
     * Defer check to {@link NoPotionGlint#shouldRemoveGlint()}.
     * If check passes, return early from this method.
     */
    @Inject(
        method = "isFoil",
        at = @At("HEAD"),
        cancellable = true
    )
    public void hookHasGlint(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (NoPotionGlint.shouldRemoveGlint()) {
            cir.setReturnValue(false);
        }
    }
}
