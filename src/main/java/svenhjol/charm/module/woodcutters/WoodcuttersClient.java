package svenhjol.charm.module.woodcutters;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.registry.ClientRegistry;

@ClientModule(module = Woodcutters.class)
public class WoodcuttersClient extends CharmModule {
    public static RecipeBookCategories SEARCH_CATEGORY;
    public static RecipeBookCategories MAIN_CATEGORY;

    @Override
    public void register() {
        ClientRegistry.menuScreen(Woodcutters.MENU, WoodcutterScreen::new);
        BlockRenderLayerMap.INSTANCE.putBlock(Woodcutters.WOODCUTTER, RenderType.cutout());
    }

    @Override
    public void runWhenEnabled() {
        Pair<RecipeBookCategories, RecipeBookCategories> pair = ClientRegistry.recipeBookCategory("woodcutter", new ItemStack(Woodcutters.WOODCUTTER));
        SEARCH_CATEGORY = pair.getFirst();
        MAIN_CATEGORY = pair.getSecond();
    }
}
