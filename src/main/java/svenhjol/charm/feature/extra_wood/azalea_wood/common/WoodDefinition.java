package svenhjol.charm.feature.extra_wood.azalea_wood.common;

import svenhjol.charm.api.iface.IVariantWoodMaterial;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodDefinition;

import java.util.List;

public class WoodDefinition implements CustomWoodDefinition {
    @Override
    public IVariantWoodMaterial material() {
        return Material.AZALEA;
    }

    @Override
    public List<CustomType> types() {
        return List.of(
            CustomType.BARREL, CustomType.BOAT, CustomType.BOOKSHELF,
            CustomType.BUTTON, CustomType.CHEST, CustomType.CHISELED_BOOKSHELF,
            CustomType.DOOR, CustomType.FENCE, CustomType.GATE,
            CustomType.HANGING_SIGN, CustomType.LADDER, CustomType.LOG,
            CustomType.PLANKS, CustomType.PRESSURE_PLATE, CustomType.SIGN,
            CustomType.SLAB, CustomType.STAIRS, CustomType.TRAPDOOR,
            CustomType.TRAPPED_CHEST, CustomType.WOOD
        );
    }
}
