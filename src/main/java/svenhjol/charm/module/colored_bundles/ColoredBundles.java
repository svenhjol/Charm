package svenhjol.charm.module.colored_bundles;

import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.annotation.Module;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import svenhjol.charm.module.colored_bundles.BundleColoringRecipe;
import svenhjol.charm.module.colored_bundles.ColoredBundleItem;
import svenhjol.charm.module.colored_bundles.ColoredBundlesClient;

@Module(mod = Charm.MOD_ID, client = ColoredBundlesClient.class, description = "Allows bundles to be dyed.")
public class ColoredBundles extends CharmModule {
    public static final ResourceLocation RECIPE_ID = new ResourceLocation(Charm.MOD_ID, "crafting_special_bundlecoloring");
    public static final Map<DyeColor, svenhjol.charm.module.colored_bundles.ColoredBundleItem> COLORED_BUNDLES = new HashMap<>();
    public static SimpleRecipeSerializer<svenhjol.charm.module.colored_bundles.BundleColoringRecipe> BUNDLE_COLORING_RECIPE;

    @Override
    public void register() {
        for (DyeColor color : DyeColor.values()) {
            COLORED_BUNDLES.put(color, new ColoredBundleItem(this, color));
        }

        BUNDLE_COLORING_RECIPE = RegistryHelper.recipeSerializer(RECIPE_ID.toString(), new SimpleRecipeSerializer<>(BundleColoringRecipe::new));
    }
}
