package svenhjol.charm.mixin.feature.recipes;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.recipes.ClientCallbacks;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {
    /**
     * Get custom recipe category early.
     */
    @Inject(
        method = "getCategory",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetCategory(RecipeHolder<?> recipeHolder, CallbackInfoReturnable<RecipeBookCategories> cir) {
        var opt = ClientCallbacks.getCustomRecipeCategory(recipeHolder);
        opt.ifPresent(cir::setReturnValue);
    }
}
