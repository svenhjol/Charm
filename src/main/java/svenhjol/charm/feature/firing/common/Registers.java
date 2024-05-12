package svenhjol.charm.feature.firing.common;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import svenhjol.charm.feature.firing.Firing;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<Firing> {
    public static final String RECIPE_ID = "firing";

    public final Supplier<RecipeType<Recipe>> recipeType;
    public final Supplier<SimpleCookingSerializer<Recipe>> recipeSerializer;

    public Registers(Firing feature) {
        super(feature);

        recipeType = feature.registry().recipeType(RECIPE_ID);
        recipeSerializer = feature.registry().recipeSerializer(RECIPE_ID,
            () -> new SimpleCookingSerializer<>(Recipe::new, 100));
    }
}
