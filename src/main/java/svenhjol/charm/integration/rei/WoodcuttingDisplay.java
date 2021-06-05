package svenhjol.charm.integration.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.minecraft.util.Identifier;
import svenhjol.charm.module.woodcutters.WoodcuttingRecipe;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class WoodcuttingDisplay extends BasicDisplay {
    public WoodcuttingDisplay(WoodcuttingRecipe recipe) {
        this(EntryIngredients.ofIngredients(recipe.getIngredients()), Collections.singletonList(EntryIngredients.of(recipe.getOutput())),
            Optional.ofNullable(recipe.getId()));
    }

    public WoodcuttingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<Identifier> location) {
        super(inputs, outputs, location);
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CharmReiCategories.WOODCUTTING;
    }

    public static BasicDisplay.Serializer<WoodcuttingDisplay> serializer() {
        return BasicDisplay.Serializer.ofSimple(WoodcuttingDisplay::new);
    }
}