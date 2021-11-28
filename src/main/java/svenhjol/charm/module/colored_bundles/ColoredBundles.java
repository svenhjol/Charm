package svenhjol.charm.module.colored_bundles;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.registry.CommonRegistry;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.hover_sorting.HoverSorting;

import java.util.HashMap;
import java.util.Map;

@CommonModule(mod = Charm.MOD_ID, description = "Allows bundles to be dyed.")
public class ColoredBundles extends CharmModule {
    public static final ResourceLocation RECIPE_ID = new ResourceLocation(Charm.MOD_ID, "crafting_special_bundlecoloring");
    public static final Map<DyeColor, ColoredBundleItem> COLORED_BUNDLES = new HashMap<>();
    public static SimpleRecipeSerializer<BundleColoringRecipe> BUNDLE_COLORING_RECIPE;

    @Override
    public void register() {
        for (DyeColor color : DyeColor.values()) {
            COLORED_BUNDLES.put(color, new ColoredBundleItem(this, color));
            HoverSorting.SORTABLE.add(COLORED_BUNDLES.get(color));
        }

        BUNDLE_COLORING_RECIPE = CommonRegistry.recipeSerializer(RECIPE_ID.toString(), new SimpleRecipeSerializer<>(BundleColoringRecipe::new));
    }
}
