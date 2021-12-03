package svenhjol.charm.mixin.kilns;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.kilns.Kilns;
import svenhjol.charm.module.kilns.KilnsClient;

import java.util.List;

@Mixin(RecipeBookCategories.class)
public class GetCorrectCategoriesMixin {
    @Inject(
        method = "getCategories",
        at = @At("RETURN"),
        cancellable = true
    )
    private static void hookGetCategories(RecipeBookType recipeBookType, CallbackInfoReturnable<List<RecipeBookCategories>> cir) {
        if (recipeBookType == Kilns.RECIPE_BOOK_TYPE) {
            cir.setReturnValue(ImmutableList.of(KilnsClient.SEARCH_CATEGORY, KilnsClient.MAIN_CATEGORY));
        }
    }
}
