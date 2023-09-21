package svenhjol.charm.feature.vanilla_wood_variants;

import svenhjol.charm.Charm;
import svenhjol.charmapi.Charmapi;
import svenhjol.charmapi.iface.*;
import svenhjol.charmony.annotation.Feature;
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
    IVariantLadderProvider
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
        Charmapi.registerProvider(this);
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
}
