package svenhjol.charm.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.RemovePotionGlint;

@Mixin(PotionItem.class)
public class PotionItemMixin {
    @Inject(
        method = "hasGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    public void hookHasGlint(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (RemovePotionGlint.shouldRemoveGlint())
            cir.setReturnValue(false);
    }
}
