package svenhjol.charm.feature.firing;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Register;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.List;
import java.util.function.Supplier;

public class Firing extends CommonFeature {
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
    public List<? extends Register<? extends Feature>> register() {
        return List.of(new CommonRegister(this));
    }
}
