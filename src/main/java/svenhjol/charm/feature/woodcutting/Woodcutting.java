package svenhjol.charm.feature.woodcutting;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import svenhjol.charm.Charm;
import svenhjol.charmony.base.Mods;
import svenhjol.charmony.common.CommonFeature;

import java.util.function.Supplier;

public class Woodcutting extends CommonFeature {
    public static final String RECIPE_ID = "woodcutting";
    public static Supplier<RecipeType<WoodcuttingRecipe>> recipeType;
    public static Supplier<RecipeSerializer<WoodcuttingRecipe>> recipeSerializer;

    @Override
    public String description() {
        return "Registers the woodcutting recipe.";
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
                () -> new WoodcuttingRecipe.Serializer<>(WoodcuttingRecipe::new));
        }
    }
}
