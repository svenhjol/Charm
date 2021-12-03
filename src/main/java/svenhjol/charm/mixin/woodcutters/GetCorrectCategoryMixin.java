package svenhjol.charm.mixin.woodcutters;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.woodcutters.Woodcutters;
import svenhjol.charm.module.woodcutters.WoodcuttersClient;

@Mixin(ClientRecipeBook.class)
public class GetCorrectCategoryMixin {
    @Inject(
        method = "getCategory",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetCategory(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookCategories> cir) {
        if (recipe.getType() == Woodcutters.RECIPE_TYPE) {
            cir.setReturnValue(WoodcuttersClient.MAIN_CATEGORY);
        }
    }
}
