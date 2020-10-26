package svenhjol.charm.module;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ClientRegistryHandler;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.KilnBlock;
import svenhjol.charm.blockentity.KilnBlockEntity;
import svenhjol.charm.gui.KilnScreen;
import svenhjol.charm.recipe.FiringRecipe;
import svenhjol.charm.screenhandler.KilnScreenHandler;

@Module(mod = Charm.MOD_ID, description = "A functional block that speeds up cooking of clay, bricks and terracotta.")
public class Kilns extends CharmModule {
    public static Identifier RECIPE_ID = new Identifier("firing");
    public static Identifier BLOCK_ID = new Identifier(Charm.MOD_ID, "kiln");
    public static KilnBlock KILN;
    public static BlockEntityType<KilnBlockEntity> BLOCK_ENTITY;
    public static RecipeType<FiringRecipe> RECIPE_TYPE;
    public static CookingRecipeSerializer<FiringRecipe> RECIPE_SERIALIZER;
    public static ScreenHandlerType<KilnScreenHandler> SCREEN_HANDLER;

    @Override
    public void register() {
        KILN = new KilnBlock(this);
        RECIPE_TYPE = RegistryHandler.recipeType(RECIPE_ID.toString());
        RECIPE_SERIALIZER = RegistryHandler.recipeSerializer(RECIPE_ID.toString(), new CookingRecipeSerializer<>(FiringRecipe::new, 100));
        BLOCK_ENTITY = RegistryHandler.blockEntity(BLOCK_ID, KilnBlockEntity::new, KILN);
        SCREEN_HANDLER = RegistryHandler.screenHandler(BLOCK_ID, KilnScreenHandler::new);
    }

    @Override
    public void clientInit() {
        ClientRegistryHandler.screenHandler(SCREEN_HANDLER, KilnScreen::new);
    }
}