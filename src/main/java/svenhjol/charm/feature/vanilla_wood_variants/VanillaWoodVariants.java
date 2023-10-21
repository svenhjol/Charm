package svenhjol.charm.feature.vanilla_wood_variants;

import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.enums.VanillaWood;
import svenhjol.charmony_api.CharmonyApi;
import svenhjol.charmony_api.iface.*;

import java.util.LinkedList;
import java.util.List;

public class VanillaWoodVariants extends CommonFeature implements
    IVariantBarrelProvider,
    IVariantBookshelfProvider,
    IVariantChestProvider,
    IVariantChiseledBookshelfProvider,
    IVariantLadderProvider
{
    private static final List<IVariantMaterial> TYPES = new LinkedList<>(VanillaWood.getTypes());

    @Override
    public String description() {
        return "Barrels, bookcases, chests and ladders in all vanilla wood types.";
    }

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
    public List<IVariantMaterial> getVariantChiseledBookshelves() {
        return TYPES;
    }

    @Override
    public List<IVariantMaterial> getVariantLadders() {
        return TYPES;
    }
}
