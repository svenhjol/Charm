package svenhjol.charm.feature.variant_wood.registry;

import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.ItemLike;
import svenhjol.charm.api.IVariantChestBoatDefinition;
import svenhjol.charm.feature.variant_wood.recipe.VariantChestBoatRecipe;
import svenhjol.charmony.api.iface.IVariantWoodMaterial;
import svenhjol.charmony.iface.ICommonRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CustomChestBoat {
    public final IVariantWoodMaterial material;
    public static final String BOAT_RECIPE_ID = "crafting_special_variantchestboats";
    public static Map<Supplier<? extends ItemLike>, Supplier<? extends ItemLike>> boatPairs = new HashMap<>();
    public static Map<String, Integer> layerColors = new HashMap<>();
    public static Supplier<SimpleCraftingRecipeSerializer<VariantChestBoatRecipe>> boatRecipe;

    public CustomChestBoat(ICommonRegistry registry, IVariantChestBoatDefinition definition) {
        // Register the boat recipe if it hasn't already been initialized.
        if (boatRecipe == null) {
            boatRecipe = registry.recipeSerializer(BOAT_RECIPE_ID,
                () -> new SimpleCraftingRecipeSerializer<>(VariantChestBoatRecipe::new));
        }

        material = definition.getMaterial();
        var boatPair = definition.getBoatPair();

        boatPairs.put(boatPair.getFirst(), boatPair.getSecond());
        layerColors.put(material.getSerializedName(), material.chestBoatColor());
    }
}
