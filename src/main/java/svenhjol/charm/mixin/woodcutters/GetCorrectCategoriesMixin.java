package svenhjol.charm.mixin.woodcutters;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.woodcutters.Woodcutters;
import svenhjol.charm.module.woodcutters.WoodcuttersClient;

import java.util.List;

@Mixin(RecipeBookCategories.class)
public class GetCorrectCategoriesMixin {
    @Inject(
        method = "getCategories",
        at = @At("RETURN"),
        cancellable = true
    )
    private static void hookGetCategories(RecipeBookType recipeBookType, CallbackInfoReturnable<List<RecipeBookCategories>> cir) {
        if (recipeBookType == Woodcutters.RECIPE_BOOK_TYPE) {
            cir.setReturnValue(ImmutableList.of(WoodcuttersClient.SEARCH_CATEGORY, WoodcuttersClient.MAIN_CATEGORY));
        }
    }
}
