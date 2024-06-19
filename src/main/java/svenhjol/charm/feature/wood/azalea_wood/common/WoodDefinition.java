package svenhjol.charm.feature.wood.azalea_wood.common;

import svenhjol.charm.charmony.iface.CustomWoodMaterial;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodDefinition;

import java.util.List;

public class WoodDefinition implements CustomWoodDefinition {
    @Override
    public CustomWoodMaterial material() {
        return Material.AZALEA;
    }

    @Override
    public List<CustomType> types() {
        return List.of(
            CustomType.BARREL,
            CustomType.CHEST_BOAT,
            CustomType.BOAT,
            CustomType.BOOKSHELF,
            CustomType.BUTTON,
            CustomType.CHEST,
            CustomType.CHISELED_BOOKSHELF,
            CustomType.DOOR,
            CustomType.FENCE,
            CustomType.GATE,
            CustomType.HANGING_SIGN,
            CustomType.LADDER,
            CustomType.LOG,
            CustomType.PLANKS,
            CustomType.PRESSURE_PLATE,
            CustomType.SIGN,
            CustomType.SLAB,
            CustomType.STAIRS,
            CustomType.STRIPPED_LOG,
            CustomType.STRIPPED_WOOD,
            CustomType.TRAPDOOR,
            CustomType.TRAPPED_CHEST,
            CustomType.WOOD
        );
    }
}
