package svenhjol.charm.module;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.Charm;
import svenhjol.charm.block.KilnBlock;
import svenhjol.charm.blockentity.KilnBlockEntity;
import svenhjol.charm.gui.KilnScreen;
import svenhjol.charm.recipe.FiringRecipe;
import svenhjol.charm.screenhandler.KilnScreenHandler;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;

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
        RECIPE_TYPE = RecipeType.register(RECIPE_ID.toString());
        RECIPE_SERIALIZER = RecipeSerializer.register(RECIPE_ID.toString(), new CookingRecipeSerializer(FiringRecipe::new, 100));
        BLOCK_ENTITY = BlockEntityType.Builder.create(KilnBlockEntity::new, KILN).build(null);
        SCREEN_HANDLER = Registry.register(Registry.SCREEN_HANDLER, BLOCK_ID, new ScreenHandlerType<>(KilnScreenHandler::new));

        Registry.register(Registry.BLOCK_ENTITY_TYPE, BLOCK_ID, BLOCK_ENTITY);
    }

    @Override
    public void clientInit() {
        ScreenRegistry.register(SCREEN_HANDLER, KilnScreen::new);
    }
}
