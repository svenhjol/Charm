package svenhjol.charm.module.kilns;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.registry.ClientRegistry;

@ClientModule(module = Kilns.class)
public class KilnsClient extends CharmModule {
    public static RecipeBookCategories SEARCH_CATEGORY;
    public static RecipeBookCategories MAIN_CATEGORY;

    @Override
    public void register() {
        ClientRegistry.menuScreen(Kilns.SCREEN_HANDLER, KilnScreen::new);
    }

    @Override
    public void runWhenEnabled() {
        Pair<RecipeBookCategories, RecipeBookCategories> pair = ClientRegistry.recipeBookCategory("kiln", new ItemStack(Kilns.KILN));
        SEARCH_CATEGORY = pair.getFirst();
        MAIN_CATEGORY = pair.getSecond();
    }
}
