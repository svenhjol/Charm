package svenhjol.charm.mixin.no_treasure_enchantment_trading;

import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.no_treasure_enchantment_trading.NoTreasureEnchantmentTrading;

@Mixin(Enchantment.class)
public class CheckIfTradeableMixin {
    @Inject(
        method = "isTradeable",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookIsTradeable(CallbackInfoReturnable<Boolean> cir) {
        Enchantment enchantment = (Enchantment)(Object)this;
        if (!NoTreasureEnchantmentTrading.isTradeable(enchantment)) {
            cir.setReturnValue(false);
        }
    }
}
