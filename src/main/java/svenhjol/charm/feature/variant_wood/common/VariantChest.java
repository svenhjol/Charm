package svenhjol.charm.feature.variant_wood.common;

import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.feature.variant_wood.block.ChestBlock;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.feature.FeatureResolver;

import java.util.List;
import java.util.function.Supplier;

public class VariantChest implements FeatureResolver<VariantWood> {
    public final Supplier<ChestBlock> block;
    public final Supplier<ChestBlock.BlockItem> item;
    public final String modId;

    public VariantChest(CommonFeature owner, IVariantMaterial material) {
        var registry = owner.registry();
        var id = material.getSerializedName() + "_chest";

        modId = registry.id();
        block = registry.block(id, () -> new ChestBlock(material));
        item = registry.item(id, () -> new ChestBlock.BlockItem(block));

        // Chests can be used as furnace fuel.
        registry.fuel(item);

        // Associate the blocks with the block entity dynamically.
        registry.blockEntityBlocks(feature().registers.chestBlockEntity, List.of(block));
    }

    @Override
    public Class<VariantWood> typeForFeature() {
        return VariantWood.class;
    }
}
