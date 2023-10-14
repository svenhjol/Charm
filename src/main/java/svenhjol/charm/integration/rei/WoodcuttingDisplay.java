package svenhjol.charm.integration.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import svenhjol.charmony.feature.woodcutting.WoodcuttingRecipe;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * See me.shedaniel.rei.plugin.common.displays.DefaultStoneCuttingDisplay
 * for reference implementation.
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class WoodcuttingDisplay extends BasicDisplay {
    @SuppressWarnings("UnstableApiUsage")
    public WoodcuttingDisplay(RecipeHolder<WoodcuttingRecipe> recipe) {
        this(EntryIngredients.ofIngredients(recipe.value().getIngredients()),
            Collections.singletonList(EntryIngredients.of(recipe.value().getResultItem(BasicDisplay.registryAccess()))),
            Optional.of(recipe.id()));
    }

    public WoodcuttingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<ResourceLocation> location) {
        super(inputs, outputs, location);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return ICharmReiPlugin.WOODCUTTING;
    }

    public static BasicDisplay.Serializer<WoodcuttingDisplay> serializer() {
        return BasicDisplay.Serializer.ofSimple(WoodcuttingDisplay::new);
    }
}