package svenhjol.charm.integration;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.TransferRecipeDisplay;
import me.shedaniel.rei.plugin.cooking.DefaultCookingDisplay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.util.Identifier;

import java.util.List;

@Environment(EnvType.CLIENT)
public class FiringDisplay extends DefaultCookingDisplay implements TransferRecipeDisplay {
    public FiringDisplay(AbstractCookingRecipe recipe) {
        super(recipe);
    }

    @Override
    public Identifier getRecipeCategory() {
        return CharmReiPlugin.FIRING;
    }

    @Override
    public List<List<EntryStack>> getRequiredEntries() {
        return getInputEntries();
    }
}