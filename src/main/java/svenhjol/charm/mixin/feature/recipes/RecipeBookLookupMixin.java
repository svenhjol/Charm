package svenhjol.charm.mixin.feature.recipes;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.foundation.helper.ClientRegistryHelper;

import java.util.List;

@Mixin(RecipeBookCategories.class)
public class RecipeBookLookupMixin {
    @Inject(
        method = "getCategories",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetCategories(RecipeBookType recipeBookType, CallbackInfoReturnable<List<RecipeBookCategories>> cir) {
        var byType = ClientRegistryHelper.getRecipeBookCategoryByType();

        if (byType.containsKey(recipeBookType)) {
            cir.setReturnValue(byType.get(recipeBookType));
        }
    }
}
