package svenhjol.charm.feature.vanilla_wood_variants;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.api.*;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.api.CharmonyApi;
import svenhjol.charmony.api.iface.IRecipeRemoveProvider;
import svenhjol.charmony.api.iface.IVariantBarrelProvider;
import svenhjol.charmony.api.iface.IVariantChestProvider;
import svenhjol.charmony.api.iface.IVariantMaterial;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.enums.VanillaWood;

import java.util.ArrayList;
import java.util.List;

@Feature(mod = Charm.MOD_ID, description = "Barrels, bookcases, chests and ladders in all vanilla wood types.")
public class VanillaWoodVariants extends CharmFeature implements
    IVariantBarrelProvider,
    IVariantBookshelfProvider,
    IVariantChestProvider,
    IVariantChestBoatProvider,
    IVariantChiseledBookshelfProvider,
    IVariantLadderProvider,
    IRecipeRemoveProvider
{
    private static final List<IVariantMaterial> TYPES = new ArrayList<>(VanillaWood.getTypes());

    private static final List<IVariantChestBoatDefinition> CHEST_BOAT_DEFINITIONS = List.of(
        new ChestBoatDefinitions.Acacia(),
        new ChestBoatDefinitions.Bamboo(),
        new ChestBoatDefinitions.Birch(),
        new ChestBoatDefinitions.Cherry(),
        new ChestBoatDefinitions.DarkOak(),
        new ChestBoatDefinitions.Jungle(),
        new ChestBoatDefinitions.Mangrove(),
        new ChestBoatDefinitions.Oak(),
        new ChestBoatDefinitions.Spruce()
    );

    @Override
    public void register() {
        CharmonyApi.registerProvider(this);
    }

    @Override
    public List<IVariantMaterial> getVariantBarrels() {
        return TYPES;
    }

    @Override
    public List<IVariantMaterial> getVariantBookshelves() {
        return TYPES;
    }

    @Override
    public List<IVariantMaterial> getVariantChests() {
        return TYPES;
    }

    @Override
    public List<IVariantChestBoatDefinition> getVariantChestBoatDefinitions() {
        return CHEST_BOAT_DEFINITIONS;
    }

    @Override
    public List<IVariantMaterial> getVariantChiseledBookshelves() {
        return TYPES;
    }

    @Override
    public List<IVariantMaterial> getVariantLadders() {
        return TYPES;
    }

    @Override
    public List<ResourceLocation> getRecipesToRemove() {
        List<ResourceLocation> recipes = new ArrayList<>();
        var charm = Charm.instance();
        var woodcuttersEnabled = charm.loader().isEnabled("Woodcutters");

        if (!woodcuttersEnabled) {
            recipes.add(charm.makeId("vanilla_wood_variants/woodcutting"));
        }

        return recipes;
    }
}
