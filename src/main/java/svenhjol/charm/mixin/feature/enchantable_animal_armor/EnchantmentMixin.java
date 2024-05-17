package svenhjol.charm.mixin.feature.enchantable_animal_armor;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.enchantable_animal_armor.EnchantableAnimalArmor;
import svenhjol.charm.foundation.Resolve;

@SuppressWarnings({"ConstantConditions", "UnreachableCode"})
@Mixin(Enchantment.class)
public class EnchantmentMixin {
    @Inject(
        method = "canEnchant",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookCanEnchant(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        var enchantment = (Enchantment) (Object) this;
        var handlers = Resolve.feature(EnchantableAnimalArmor.class).handlers;

        if (handlers.isValidHorseEnchantment(stack, enchantment) || handlers.isValidWolfEnchantment(stack, enchantment)) {
            cir.setReturnValue(true);
        }
    }
}