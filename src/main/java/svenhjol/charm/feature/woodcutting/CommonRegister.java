package svenhjol.charm.feature.woodcutting;

import svenhjol.charm.foundation.Register;

public class CommonRegister extends Register<Woodcutting> {
    public static final String RECIPE_ID = "woodcutting";

    public CommonRegister(Woodcutting feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        Woodcutting.recipeType = feature.registry().recipeType(RECIPE_ID);
        Woodcutting.recipeSerializer = feature.registry().recipeSerializer(RECIPE_ID,
            () -> new WoodcuttingRecipe.Serializer<>(WoodcuttingRecipe::new));
    }
}
