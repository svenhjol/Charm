package svenhjol.charm.feature.wood.vanilla_wood_variants.common;

import svenhjol.charm.api.iface.CustomWoodMaterial;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodDefinition;

import java.util.List;

public record WoodDefinition(CustomWoodMaterial material) implements CustomWoodDefinition {
    @Override
    public List<CustomType> types() {
        return List.of(
            CustomType.BARREL,
            CustomType.BOOKSHELF,
            CustomType.CHEST,
            CustomType.CHISELED_BOOKSHELF,
            CustomType.LADDER,
            CustomType.TRAPPED_CHEST
        );
    }
}
