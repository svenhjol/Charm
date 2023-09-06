package svenhjol.charm.feature.variant_wood.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.feature.variant_wood.block.VariantChiseledBookshelfBlock;
import svenhjol.charmony.api.iface.IVariantMaterial;
import svenhjol.charmony.iface.ICommonRegistry;

import java.util.List;
import java.util.function.Supplier;

public class CustomChiseledBookshelf {
    public final Supplier<VariantChiseledBookshelfBlock> block;
    public final Supplier<VariantChiseledBookshelfBlock.BlockItem> item;

    public CustomChiseledBookshelf(ICommonRegistry registry, IVariantMaterial material) {
        var id = "chiseled_" + material.getSerializedName() + "_bookshelf";

        block = registry.block(id, () -> new VariantChiseledBookshelfBlock(material));
        item = registry.item(id, () -> new VariantChiseledBookshelfBlock.BlockItem(block));

        // Can be used as furnace fuel.
        registry.fuel(item);

        // Associate blocks with block entity dynamically.
        registry.blockEntityBlocks(() -> BlockEntityType.CHISELED_BOOKSHELF, List.of(block));
    }
}
