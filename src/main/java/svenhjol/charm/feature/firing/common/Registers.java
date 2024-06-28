package svenhjol.charm.feature.firing.common;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import svenhjol.charm.feature.firing.Firing;
import svenhjol.charm.charmony.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<Firing> {
    public static final String RECIPE_ID = "firing";

    public final Supplier<RecipeType<FiringRecipe>> recipeType;
    public final Supplier<SimpleCookingSerializer<FiringRecipe>> recipeSerializer;

    public Registers(Firing feature) {
        super(feature);

        recipeType = feature.registry().recipeType(RECIPE_ID);
        recipeSerializer = feature.registry().recipeSerializer(RECIPE_ID,
            () -> new SimpleCookingSerializer<>(FiringRecipe::new, 100));
    }
}
