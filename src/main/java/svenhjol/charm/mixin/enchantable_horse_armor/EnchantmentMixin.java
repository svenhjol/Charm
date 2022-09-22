package svenhjol.charm.mixin.enchantable_horse_armor;

import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.enchantable_horse_armor.EnchantableHorseArmor;

@Mixin(Enchantment.class)
public class EnchantmentMixin {
    @Inject(
        method = "canEnchant",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookCanEnchant(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        var enchantment = (Enchantment) (Object) this;

        if (stack.getItem() instanceof HorseArmorItem
            && EnchantableHorseArmor.VALID_ENCHANTMENTS.contains(enchantment)) {
            cir.setReturnValue(true);
        }
    }
}
