package svenhjol.charm.feature.variant_wood;

import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.foundation.common.CommonRegistry;

import java.util.List;
import java.util.function.Supplier;

public class CustomChiseledBookshelf {
    public final Supplier<VariantChiseledBookshelfBlock> block;
    public final Supplier<VariantChiseledBookshelfBlock.BlockItem> item;

    public CustomChiseledBookshelf(CommonRegistry registry, IVariantMaterial material) {
        var id = "chiseled_" + material.getSerializedName() + "_bookshelf";

        block = registry.block(id, () -> new VariantChiseledBookshelfBlock(material));
        item = registry.item(id, () -> new VariantChiseledBookshelfBlock.BlockItem(block));

        // Can be used as furnace fuel.
        registry.fuel(item);

        // Associate blocks with block entity dynamically.
        registry.blockEntityBlocks(() -> BlockEntityType.CHISELED_BOOKSHELF, List.of(block));
    }
}
