package svenhjol.charm.mixin.helper;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.kilns.Kilns;
import svenhjol.charm.module.woodcutters.Woodcutters;

@Mixin(ClientRecipeBook.class)
public class FixRecipeSpamMixin {
    /**
     * Prevents log spam from the recipe book when custom recipe types cannot be found.
     */
    @Inject(
        method = "getCategory",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetGroupForRecipe(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookCategories> cir) {
        if (recipe.getType() == Woodcutters.RECIPE_TYPE)
            cir.setReturnValue(RecipeBookCategories.STONECUTTER);

        if (recipe.getType() == Kilns.RECIPE_TYPE)
            cir.setReturnValue(RecipeBookCategories.FURNACE_MISC);
    }
}
