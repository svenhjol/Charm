package svenhjol.charm.mixin.feature.animal_armor_enchanting;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charm.feature.animal_armor_enchanting.AnimalArmorEnchanting;
import svenhjol.charm.charmony.Resolve;

@SuppressWarnings({"ConstantConditions", "UnreachableCode"})
@Mixin(Enchantment.class)
public class EnchantmentMixin {

    @ModifyReturnValue(
            method = "canEnchant",
            at = @At("RETURN")
    )
    private boolean hookCanEnchant(boolean original, @Local(argsOnly = true) ItemStack stack) {
        var enchantment = (Enchantment) (Object) this;
        var handlers = Resolve.feature(AnimalArmorEnchanting.class).handlers;

        if (handlers.isValidHorseEnchantment(stack, enchantment) || handlers.isValidWolfEnchantment(stack, enchantment)) {
            return true;
        }
        return original;
    }
}