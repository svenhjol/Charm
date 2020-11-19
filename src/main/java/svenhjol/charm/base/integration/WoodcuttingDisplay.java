package svenhjol.charm.base.integration;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeDisplay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import svenhjol.charm.module.Woodcutters;

import java.util.Collections;
import java.util.List;

@Environment(EnvType.CLIENT)
public class WoodcuttingDisplay implements RecipeDisplay {
    private final EntryStack input;
    public final EntryStack output;

    public WoodcuttingDisplay(ItemStack input, ItemStack output) {
        this.input = EntryStack.create(input);
        this.output = EntryStack.create(output);
    }

    @Override
    public List<List<EntryStack>> getInputEntries() {
        return Collections.singletonList(Collections.singletonList(input));
    }

    @Override
    public List<List<EntryStack>> getResultingEntries() {
        return Collections.singletonList(Collections.singletonList(output));
    }

    @Override
    public Identifier getRecipeCategory() {
        return Woodcutters.RECIPE_ID;
    }

    @Override
    public List<List<EntryStack>> getRequiredEntries() {
        return getInputEntries();
    }
}