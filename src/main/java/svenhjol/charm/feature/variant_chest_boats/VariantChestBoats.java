package svenhjol.charm.feature.variant_chest_boats;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.ItemLike;
import svenhjol.charm.Charm;
import svenhjol.charmony.api.iface.IVariantWoodMaterial;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, canBeDisabled = false, description = "Registers variant chest boats.")
public class VariantChestBoats extends CharmFeature {
    static final String BOAT_RECIPE_ID = "crafting_special_variantchestboats";
    static final Map<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>> CHEST_BOAT_MAP_SUPPLIER = new HashMap<>();
    static Map<ItemLike, ItemLike> CHEST_BOAT_MAP = new HashMap<>();
    static Map<String, Integer> CHEST_LAYER_COLORS = new HashMap<>();
    static Supplier<SimpleCraftingRecipeSerializer<VariantChestBoatRecipe>> BOAT_RECIPE;

    public void register() {
        BOAT_RECIPE = Charm.instance().registry().recipeSerializer(BOAT_RECIPE_ID,
            () -> new SimpleCraftingRecipeSerializer<>(VariantChestBoatRecipe::new));
    }

    public static void registerChestBoat(Supplier<? extends ItemLike> boatItem, Supplier<? extends ItemLike> chestBoatItem) {
        VariantChestBoats.CHEST_BOAT_MAP_SUPPLIER.put(boatItem, chestBoatItem);
    }

    public static void registerChestLayerColor(IVariantWoodMaterial material) {
        VariantChestBoats.CHEST_LAYER_COLORS.put(material.getSerializedName(), material.chestBoatColor());
    }

    public static int getLayerColor(ItemStack stack) {
        int color = 0xdf9f43; // this is the default color when there's no variant chest tag

        var tag = stack.getTag();
        if (tag != null && tag.contains(VariantChestBoatRecipe.CHEST_TYPE_TAG)) {
            var type = tag.getString(VariantChestBoatRecipe.CHEST_TYPE_TAG);
            color = CHEST_LAYER_COLORS.getOrDefault(type, color);
        }

        return color;
    }
}
