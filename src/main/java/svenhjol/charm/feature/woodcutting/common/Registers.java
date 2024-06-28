package svenhjol.charm.feature.woodcutting.common;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import svenhjol.charm.feature.woodcutting.Woodcutting;
import svenhjol.charm.charmony.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<Woodcutting> {
    public static final String RECIPE_ID = "woodcutting";

    public final Supplier<RecipeType<WoodcuttingRecipe>> recipeType;
    public final Supplier<RecipeSerializer<WoodcuttingRecipe>> recipeSerializer;

    public Registers(Woodcutting feature) {
        super(feature);
        var registry = feature.registry();

        recipeType = registry.recipeType(RECIPE_ID);
        recipeSerializer = registry.recipeSerializer(RECIPE_ID,
            () -> new WoodcuttingRecipe.Serializer<>(WoodcuttingRecipe::new));
    }
}
