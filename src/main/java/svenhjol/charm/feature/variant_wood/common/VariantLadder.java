package svenhjol.charm.feature.variant_wood.common;

import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.feature.variant_wood.block.LadderBlock;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.feature.FeatureResolver;

import java.util.function.Supplier;

public class VariantLadder implements FeatureResolver<VariantWood> {
    public final Supplier<LadderBlock> block;
    public final Supplier<LadderBlock.BlockItem> item;

    public VariantLadder(CommonFeature owner, IVariantMaterial material) {
        var registry = owner.registry();
        var id = material.getSerializedName() + "_ladder";

        block = registry.block(id, () -> new LadderBlock(material));
        item = registry.item(id, () -> new LadderBlock.BlockItem(block));

        // Ladders can be used as furnace fuel.
        registry.fuel(item);
    }

    @Override
    public Class<VariantWood> featureType() {
        return VariantWood.class;
    }
}
