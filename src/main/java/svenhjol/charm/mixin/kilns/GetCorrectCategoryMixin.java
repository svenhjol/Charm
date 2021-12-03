package svenhjol.charm.mixin.kilns;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.crafting.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.kilns.Kilns;
import svenhjol.charm.module.kilns.KilnsClient;

@Mixin(ClientRecipeBook.class)
public class GetCorrectCategoryMixin {
    @Inject(
        method = "getCategory",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetCategory(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookCategories> cir) {
        if (recipe.getType() == Kilns.RECIPE_TYPE) {
            cir.setReturnValue(KilnsClient.MAIN_CATEGORY);
        }
    }
}
