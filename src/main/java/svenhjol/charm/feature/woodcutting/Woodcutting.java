package svenhjol.charm.feature.woodcutting;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;
import java.util.function.Supplier;

public class Woodcutting extends CommonFeature {
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
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }
}
