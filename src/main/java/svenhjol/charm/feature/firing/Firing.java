package svenhjol.charm.feature.firing;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import svenhjol.charm.Charm;
import svenhjol.charmony.base.Mods;
import svenhjol.charmony.common.CommonFeature;

import java.util.function.Supplier;

public class Firing extends CommonFeature {
    public static final String RECIPE_ID = "firing";
    public static Supplier<RecipeType<FiringRecipe>> recipeType;
    public static Supplier<SimpleCookingSerializer<FiringRecipe>> recipeSerializer;

    @Override
    public String description() {
        return "Registers the firing recipe.";
    }

    @Override
    public boolean canBeDisabled() {
        return false;
    }

    @Override
    public void register() {
        var registry = Mods.common(Charm.ID).registry();

        if (recipeType == null) {
            recipeType = registry.recipeType(RECIPE_ID);
        }

        if (recipeSerializer == null) {
            recipeSerializer = registry.recipeSerializer(RECIPE_ID,
                () -> new SimpleCookingSerializer<>(FiringRecipe::new, 100));
        }
    }
}
