package svenhjol.charm.base.integration;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.module.Woodcutters;

@Environment(EnvType.CLIENT)
public class CharmReiPlugin implements REIPluginV0 {
    public static final Identifier WOODCUTTING = new Identifier(Charm.MOD_ID, "woodcutting");
    public static final Identifier FIRING = new Identifier(Charm.MOD_ID, "firing");

    @Override
    public Identifier getPluginIdentifier() {
        return new Identifier(Charm.MOD_ID, "rei_plugin");
    }

    @Override
    public void registerOthers(RecipeHelper recipeHelper) {
        recipeHelper.registerWorkingStations(WOODCUTTING, EntryStack.create(Woodcutters.WOODCUTTER));
//        recipeHelper.registerWorkingStations(FIRING, EntryStack.create(Kilns.KILN));

        recipeHelper.removeAutoCraftButton(WOODCUTTING);
//        recipeHelper.removeAutoCraftButton(FIRING);

//        BuiltinPlugin.getInstance().registerInformation(EntryStack.create(EnergonRelics.Items.CIRCUIT_BOARD), new TranslatableText("category.rei." + Charm.MOD_ID + ".information.structure_generation.title"), texts -> {
//            List<Text> newTexts = new ArrayList<>(texts);
//            newTexts.add(new TranslatableText("category.rei." + Charm.MOD_ID + ".information.structure_generation.research_complex"));
//            return newTexts;
//        });
    }

    @Override
    public void registerRecipeDisplays(RecipeHelper recipeHelper) {
        WoodcuttingCategory.register(recipeHelper);
//        ReactorFuelCategory.register(recipeHelper);
    }

    @Override
    public void registerPluginCategories(RecipeHelper recipeHelper) {
        recipeHelper.registerCategories(new WoodcuttingCategory());
    }

}
