package svenhjol.charm.mixin.grindable_horse_armor;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.annotation.CharmMixin;
import svenhjol.charm.module.grindable_horse_armor.GrindableHorseArmor;

/**
 * GrindEnchantments mod replaces the grindstone slots so doesn't allow this mixin to function.
 */
@CharmMixin(disableIfModsPresent = {"grindenchantments"})
@Mixin(targets = {"net/minecraft/world/inventory/GrindstoneMenu$2", "net/minecraft/world/inventory/GrindstoneMenu$3"})
public class AllowHorseArmorMixin {
    /**
     * Adds an injection point in the mayPlace method of the grindstone slot constructor.
     * Checks the placed item against the grindable horse armor recipes and returns early if matches.
     */
    @Inject(
        method = "mayPlace(Lnet/minecraft/world/item/ItemStack;)Z",
        at = @At("RETURN"),
        cancellable = true
    )
    private void hookMayPlace(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (GrindableHorseArmor.enabled() && GrindableHorseArmor.HORSE_ARMOR_RECIPES.containsKey(stack.getItem())) {
            cir.setReturnValue(true);
        }
    }
}
