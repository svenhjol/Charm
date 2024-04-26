package svenhjol.charm.feature.firing;

import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import svenhjol.charm.foundation.Registration;

public class CommonRegistration extends Registration<Firing> {
    public static final String RECIPE_ID = "firing";

    public CommonRegistration(Firing feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        Firing.recipeType = feature.registry().recipeType(RECIPE_ID);
        Firing.recipeSerializer = feature.registry().recipeSerializer(RECIPE_ID,
            () -> new SimpleCookingSerializer<>(FiringRecipe::new, 100));
    }
}
