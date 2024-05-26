package svenhjol.charm.feature.core.custom_wood.holders;

import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.feature.core.custom_wood.blocks.CustomChiseledBookshelfBlock;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;

import java.util.List;
import java.util.function.Supplier;

public class ChiseledBookshelfHolder {
    public final Supplier<CustomChiseledBookshelfBlock> block;
    public final Supplier<CustomChiseledBookshelfBlock.BlockItem> item;

    public ChiseledBookshelfHolder(CustomWoodHolder holder) {
        var registry = holder.ownerRegistry();
        var material = holder.getMaterial();
        var id = "chiseled_" + holder.getMaterialName() + "_bookshelf";

        block = registry.block(id, () -> new CustomChiseledBookshelfBlock(material));
        item = registry.item(id, () -> new CustomChiseledBookshelfBlock.BlockItem(block));

        // Can be used as furnace fuel.
        registry.fuel(item);

        // Associate blocks with block entity dynamically.
        registry.blockEntityBlocks(() -> BlockEntityType.CHISELED_BOOKSHELF, List.of(block));

        // Add to creative menu.
        holder.addCreativeTabItem(CustomType.CHISELED_BOOKSHELF, item);
    }
}
