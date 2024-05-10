package svenhjol.charm.feature.variant_wood.common;

import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.feature.variant_wood.block.ChiseledBookShelfBlock;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.feature.FeatureResolver;

import java.util.List;
import java.util.function.Supplier;

public class VariantChiseledBookshelf implements FeatureResolver<VariantWood> {
    public final Supplier<ChiseledBookShelfBlock> block;
    public final Supplier<ChiseledBookShelfBlock.BlockItem> item;

    public VariantChiseledBookshelf(CommonFeature owner, IVariantMaterial material) {
        var registry = owner.registry();
        var id = "chiseled_" + material.getSerializedName() + "_bookshelf";

        block = registry.block(id, () -> new ChiseledBookShelfBlock(material));
        item = registry.item(id, () -> new ChiseledBookShelfBlock.BlockItem(block));

        // Can be used as furnace fuel.
        registry.fuel(item);

        // Associate blocks with block entity dynamically.
        registry.blockEntityBlocks(() -> BlockEntityType.CHISELED_BOOKSHELF, List.of(block));
    }

    @Override
    public Class<VariantWood> featureType() {
        return VariantWood.class;
    }
}
