package svenhjol.charm.module;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.WoodcutterBlock;
import svenhjol.charm.gui.WoodcutterScreen;
import svenhjol.charm.recipe.WoodcuttingRecipe;
import svenhjol.charm.screenhandler.WoodcutterScreenHandler;

@Module(mod = Charm.MOD_ID, description = "A functional block that adds more efficient recipes for crafting wooden stairs and slabs.")
public class Woodcutters extends CharmModule {
    public static Identifier RECIPE_ID = new Identifier("woodcutting");
    public static Identifier BLOCK_ID = new Identifier(Charm.MOD_ID, "woodcutter");
    public static WoodcutterBlock WOODCUTTER;
    public static ScreenHandlerType<WoodcutterScreenHandler> SCREEN_HANDLER;
    public static RecipeType<WoodcuttingRecipe> RECIPE_TYPE;
    public static RecipeSerializer<WoodcuttingRecipe> RECIPE_SERIALIZER;

    @Override
    public void register() {
        WOODCUTTER = new WoodcutterBlock(this);
        RECIPE_TYPE = RegistryHandler.recipeType(RECIPE_ID.toString());
        RECIPE_SERIALIZER = RegistryHandler.recipeSerializer(RECIPE_ID.toString(), new WoodcuttingRecipe.Serializer<>(WoodcuttingRecipe::new));
        SCREEN_HANDLER = RegistryHandler.screenHandler(BLOCK_ID, WoodcutterScreenHandler::new);
    }

    @Override
    public void clientInit() {
        ScreenRegistry.register(SCREEN_HANDLER, WoodcutterScreen::new);
        BlockRenderLayerMap.INSTANCE.putBlock(WOODCUTTER, RenderLayer.getCutout());
    }
}
