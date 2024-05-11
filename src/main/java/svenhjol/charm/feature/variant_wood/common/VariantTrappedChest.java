package svenhjol.charm.feature.variant_wood.common;

import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.feature.variant_wood.block.TrappedChestBlock;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.feature.FeatureResolver;

import java.util.List;
import java.util.function.Supplier;

public class VariantTrappedChest implements FeatureResolver<VariantWood> {
    public final Supplier<TrappedChestBlock> block;
    public final Supplier<TrappedChestBlock.BlockItem> item;

    public VariantTrappedChest(CommonFeature owner, IVariantMaterial material) {
        var registry = owner.registry();
        var id = "trapped_" + material.getSerializedName() + "_chest";

        block = registry.block(id, () -> new TrappedChestBlock(material));
        item = registry.item(id, () -> new TrappedChestBlock.BlockItem(block));

        // Trapped chests can be used as furnace fuel.
        registry.fuel(item);

        // Associate the blocks with the block entity dynamically.
        registry.blockEntityBlocks(feature().registers.trappedChestBlockEntity, List.of(block));
    }

    @Override
    public Class<VariantWood> typeForFeature() {
        return VariantWood.class;
    }
}
