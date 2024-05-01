package svenhjol.charm.mixin.event.smithing_table;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.api.event.SmithingTableEvents;

import java.util.List;

@Mixin(SmithingTransformRecipe.class)
public class SmithingTransformRecipeMixin {

    @Shadow @Final
    Ingredient template;

    @Inject(
        method = "isTemplateIngredient",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookIsTemplateIngredient(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (SmithingTableEvents.VALIDATE_TEMPLATE.invoke(stack)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(
        method = "isBaseIngredient",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookIsBaseIngredient(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (SmithingTableEvents.CAN_PLACE.invoke(getTemplate(), 1, stack)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(
        method = "isAdditionIngredient",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookIsAdditionIngredient(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (SmithingTableEvents.CAN_PLACE.invoke(getTemplate(), 2, stack)) {
            cir.setReturnValue(true);
        }
    }

    @Unique
    private ItemStack getTemplate() {
        var items = List.of(this.template.getItems());
        if (items.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return items.get(0);
    }
}
