package svenhjol.charm.integration;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.NotNull;
import svenhjol.charm.recipe.WoodcuttingRecipe;

import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class WoodcuttingDisplay implements RecipeDisplay {
    private List<List<EntryStack>> inputs;
    private List<EntryStack> output;
    private WoodcuttingRecipe display;

    public WoodcuttingDisplay(WoodcuttingRecipe recipe) {
        this(recipe.getPreviewInputs(), recipe.getOutput());
        this.display = recipe;
    }

    public WoodcuttingDisplay(DefaultedList<Ingredient> ingredients, ItemStack output) {
        this.inputs = EntryStack.ofIngredients(ingredients);
        this.output = Collections.singletonList(EntryStack.create(output));
    }

    @Override
    public @NotNull List<List<EntryStack>> getInputEntries() {
        return inputs;
    }

    @Override
    public @NotNull List<List<EntryStack>> getResultingEntries() {
        return Collections.singletonList(output);
    }

    @Override
    public @NotNull Identifier getRecipeCategory() {
        return CharmReiPlugin.WOODCUTTING;
    }

    @Override
    public List<List<EntryStack>> getRequiredEntries() {
        return getInputEntries();
    }
}