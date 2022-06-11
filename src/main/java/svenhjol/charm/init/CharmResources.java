package svenhjol.charm.init;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.crafting.RecipeManager;
import svenhjol.charm.Charm;
import svenhjol.charm.module.core.SortingRecipeManager;

public class CharmResources {
    public static final ResourceLocation INVENTORY_BUTTONS = new ResourceLocation(Charm.MOD_ID, "textures/gui/inventory_buttons.png");
    public static final SortingRecipeManager SORTING_RECIPE_MANAGER = new SortingRecipeManager();
    public static RecipeManager recipeManagerHolder;

    public static void init() {
        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(SORTING_RECIPE_MANAGER);
    }
}
