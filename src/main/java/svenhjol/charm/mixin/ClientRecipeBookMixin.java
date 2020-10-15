package svenhjol.charm.mixin;

import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.Kilns;
import svenhjol.charm.module.Woodcutters;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {

    /**
     * Prevents log spam from the recipe book when the woodcutter recipe type cannot be found.
     */
    @Inject(
        method = "getGroupForRecipe",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetGroupForRecipe(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cir) {
        if (recipe.getType() == Woodcutters.RECIPE_TYPE)
            cir.setReturnValue(RecipeBookGroup.STONECUTTER);

        if (recipe.getType() == Kilns.RECIPE_TYPE)
            cir.setReturnValue(RecipeBookGroup.FURNACE_MISC);
    }
}
