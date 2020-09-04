package svenhjol.charm.module;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import svenhjol.charm.block.WoodcutterBlock;
import svenhjol.charm.mixin.accessor.RenderLayersAccessor;
import svenhjol.charm.recipe.WoodcuttingRecipe;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "")
public class Lumberjacks extends MesonModule {
    public static Identifier RECIPE_ID = new Identifier("woodcutter");
    public static WoodcutterBlock WOODCUTTER;
    public static RecipeType<WoodcuttingRecipe> WOODCUTTING_RECIPE_TYPE;
    public static RecipeSerializer<WoodcuttingRecipe> WOODCUTTING_RECIPE_SERIALIZER;

    @Override
    public void init() {
        WOODCUTTER = new WoodcutterBlock(this);
        WOODCUTTING_RECIPE_TYPE = RecipeType.register("woodcutting");
        WOODCUTTING_RECIPE_SERIALIZER = RecipeSerializer.register("woodcutting", new WoodcuttingRecipe.Serializer(WoodcuttingRecipe::new));
    }

    @Override
    public void initClient() {
        RenderLayersAccessor.getBlocks().put(WOODCUTTER, RenderLayer.getCutout());
    }
}
