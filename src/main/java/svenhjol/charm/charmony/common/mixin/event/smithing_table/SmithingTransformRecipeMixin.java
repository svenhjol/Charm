package svenhjol.charm.charmony.common.mixin.event.smithing_table;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charm.charmony.event.SmithingTableEvents;

import java.util.List;

@Mixin(SmithingTransformRecipe.class)
public class SmithingTransformRecipeMixin {

    @Shadow @Final
    Ingredient template;

    @ModifyReturnValue(
            method = "isTemplateIngredient",
            at = @At("RETURN")
    )
    private boolean hookIsTemplateIngredient(boolean original, @Local(argsOnly = true) ItemStack stack) {
        if (SmithingTableEvents.VALIDATE_TEMPLATE.invoke(stack)) {
            return true;
        }
        return original;
    }

    @ModifyReturnValue(
            method = "isBaseIngredient",
            at = @At("RETURN")
    )
    private boolean hookIsBaseIngredient(boolean original, @Local(argsOnly = true) ItemStack stack) {
        if (SmithingTableEvents.CAN_PLACE.invoke(getTemplate(), 1, stack)) {
            return true;
        }
        return original;
    }

    @ModifyReturnValue(
            method = "isAdditionIngredient",
            at = @At("RETURN")
    )
    private boolean hookIsAdditionIngredient(boolean original, @Local(argsOnly = true) ItemStack stack) {
        if (SmithingTableEvents.CAN_PLACE.invoke(getTemplate(), 2, stack)) {
            return true;
        }
        return original;
    }

    @Unique
    private ItemStack getTemplate() {
        var items = List.of(this.template.getItems());
        return items.isEmpty() ? ItemStack.EMPTY : items.get(0);
    }
}
