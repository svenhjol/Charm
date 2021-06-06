package svenhjol.charm.module.woodcutters;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.module.woodcutters.WoodcutterBlock;
import svenhjol.charm.module.woodcutters.WoodcutterScreenHandler;
import svenhjol.charm.module.woodcutters.WoodcuttersClient;
import svenhjol.charm.module.woodcutters.WoodcuttingRecipe;

@Module(mod = Charm.MOD_ID, client = WoodcuttersClient.class, description = "A functional block that adds more efficient recipes for crafting wooden stairs and slabs.")
public class Woodcutters extends CharmModule {
    public static ResourceLocation RECIPE_ID = new ResourceLocation(Charm.MOD_ID, "woodcutting");
    public static ResourceLocation BLOCK_ID = new ResourceLocation(Charm.MOD_ID, "woodcutter");
    public static svenhjol.charm.module.woodcutters.WoodcutterBlock WOODCUTTER;
    public static MenuType<svenhjol.charm.module.woodcutters.WoodcutterScreenHandler> SCREEN_HANDLER;
    public static RecipeType<svenhjol.charm.module.woodcutters.WoodcuttingRecipe> RECIPE_TYPE;
    public static RecipeSerializer<svenhjol.charm.module.woodcutters.WoodcuttingRecipe> RECIPE_SERIALIZER;

    @Override
    public void register() {
        WOODCUTTER = new WoodcutterBlock(this);
        RECIPE_TYPE = RegistryHelper.recipeType(RECIPE_ID.toString());
        RECIPE_SERIALIZER = RegistryHelper.recipeSerializer(RECIPE_ID.toString(), new svenhjol.charm.module.woodcutters.WoodcuttingRecipe.Serializer<>(WoodcuttingRecipe::new));
        SCREEN_HANDLER = RegistryHelper.screenHandler(BLOCK_ID, WoodcutterScreenHandler::new);
    }
}
