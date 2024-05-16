package svenhjol.charm.feature.extra_wood.vanilla_wood_variants.common;

import svenhjol.charm.api.iface.IVariantWoodMaterial;
import svenhjol.charm.feature.custom_wood.common.CustomType;
import svenhjol.charm.feature.custom_wood.common.CustomWoodDefinition;

import java.util.List;

public record WoodDefinition(IVariantWoodMaterial material) implements CustomWoodDefinition {
    @Override
    public List<CustomType> types() {
        return List.of(
            CustomType.BARREL, CustomType.BOOKSHELF, CustomType.CHEST,
            CustomType.CHISELED_BOOKSHELF, CustomType.LADDER, CustomType.TRAPPED_CHEST
        );
    }
}
