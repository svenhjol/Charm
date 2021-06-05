package svenhjol.charm.module.woodcutters;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.annotation.Module;

@Module(mod = Charm.MOD_ID, client = WoodcuttersClient.class, description = "A functional block that adds more efficient recipes for crafting wooden stairs and slabs.")
public class Woodcutters extends CharmModule {
    public static Identifier RECIPE_ID = new Identifier(Charm.MOD_ID, "woodcutting");
    public static Identifier BLOCK_ID = new Identifier(Charm.MOD_ID, "woodcutter");
    public static WoodcutterBlock WOODCUTTER;
    public static ScreenHandlerType<WoodcutterScreenHandler> SCREEN_HANDLER;
    public static RecipeType<WoodcuttingRecipe> RECIPE_TYPE;
    public static RecipeSerializer<WoodcuttingRecipe> RECIPE_SERIALIZER;

    @Override
    public void register() {
        WOODCUTTER = new WoodcutterBlock(this);
        RECIPE_TYPE = RegistryHelper.recipeType(RECIPE_ID.toString());
        RECIPE_SERIALIZER = RegistryHelper.recipeSerializer(RECIPE_ID.toString(), new WoodcuttingRecipe.Serializer<>(WoodcuttingRecipe::new));
        SCREEN_HANDLER = RegistryHelper.screenHandler(BLOCK_ID, WoodcutterScreenHandler::new);
    }
}