package svenhjol.charm.module;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.Charm;
import svenhjol.charm.block.WoodcutterBlock;
import svenhjol.charm.gui.WoodcutterScreen;
import svenhjol.meson.mixin.accessor.RenderLayersAccessor;
import svenhjol.charm.recipe.WoodcuttingRecipe;
import svenhjol.charm.screenhandler.WoodcutterScreenHandler;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "A functional block that adds more efficient recipes for crafting wooden stairs and slabs.")
public class Woodcutters extends MesonModule {
    public static Identifier RECIPE_ID = new Identifier("woodcutting");
    public static Identifier BLOCK_ID = new Identifier(Charm.MOD_ID, "woodcutter");
    public static WoodcutterBlock WOODCUTTER;
    public static ScreenHandlerType<WoodcutterScreenHandler> SCREEN_HANDLER;
    public static RecipeType<WoodcuttingRecipe> RECIPE_TYPE;
    public static RecipeSerializer<WoodcuttingRecipe> RECIPE_SERIALIZER;

    @Override
    public void init() {
        WOODCUTTER = new WoodcutterBlock(this);
        RECIPE_TYPE = RecipeType.register(RECIPE_ID.toString());
        RECIPE_SERIALIZER = RecipeSerializer.register(RECIPE_ID.toString(), new WoodcuttingRecipe.Serializer(WoodcuttingRecipe::new));
        SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, BLOCK_ID, new ScreenHandlerType<>(WoodcutterScreenHandler::new));
    }

    @Override
    public void initClient() {
        RenderLayersAccessor.getBlocks().put(WOODCUTTER, RenderLayer.getCutout());
    }

    @Override
    public void afterInitClient() {
        ScreenRegistry.register(SCREEN_HANDLER, WoodcutterScreen::new);
    }
}
