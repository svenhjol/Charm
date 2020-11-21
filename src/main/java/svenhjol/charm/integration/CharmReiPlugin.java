package svenhjol.charm.integration;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.gui.KilnScreen;
import svenhjol.charm.module.Kilns;
import svenhjol.charm.module.Woodcutters;
import svenhjol.charm.recipe.FiringRecipe;
import svenhjol.charm.recipe.WoodcuttingRecipe;

@Environment(EnvType.CLIENT)
public class CharmReiPlugin implements REIPluginV0 {
    public static final Identifier PLUGIN = new Identifier(Charm.MOD_ID, "rei_plugin");
    public static final Identifier WOODCUTTING = new Identifier(Charm.MOD_ID, "plugins/woodcutting");
    public static final Identifier FIRING = new Identifier(Charm.MOD_ID, "plugins/firing");

    @Override
    public Identifier getPluginIdentifier() {
        return PLUGIN;
    }

    @Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
        recipeHelper.registerRecipes(WOODCUTTING, WoodcuttingRecipe.class, WoodcuttingDisplay::new);
        recipeHelper.registerRecipes(FIRING, FiringRecipe.class, FiringDisplay::new);
    }

    @Override
    public void registerPluginCategories(RecipeHelper recipeHelper) {
        recipeHelper.registerCategories(new WoodcuttingCategory());
        recipeHelper.registerCategories(new FiringCategory());
    }

    @Override
    public void registerOthers(RecipeHelper recipeHelper) {
        recipeHelper.registerWorkingStations(WOODCUTTING, EntryStack.create(Woodcutters.WOODCUTTER));
        recipeHelper.registerWorkingStations(FIRING, EntryStack.create(Kilns.KILN));
        recipeHelper.registerContainerClickArea(new Rectangle(78, 32, 28, 23), KilnScreen.class, FIRING);
    }
}
