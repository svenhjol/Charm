package svenhjol.charm.feature.firing;

import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import svenhjol.charm.foundation.Register;

public class CommonRegister extends Register<Firing> {
    public static final String RECIPE_ID = "firing";

    public CommonRegister(Firing feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        Firing.recipeType = feature.registry().recipeType(RECIPE_ID);
        Firing.recipeSerializer = feature.registry().recipeSerializer(RECIPE_ID,
            () -> new SimpleCookingSerializer<>(FiringRecipe::new, 100));
    }
}
