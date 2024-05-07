package svenhjol.charm.feature.firing;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.feature.Register;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;
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
    public Optional<Register<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }
}
