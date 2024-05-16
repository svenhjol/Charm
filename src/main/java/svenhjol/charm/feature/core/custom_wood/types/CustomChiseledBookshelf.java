package svenhjol.charm.feature.core.custom_wood.types;

import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;
import svenhjol.charm.foundation.block.CharmChiseledBookshelfBlock;

import java.util.List;
import java.util.function.Supplier;

public class CustomChiseledBookshelf {
    public final Supplier<CharmChiseledBookshelfBlock> block;
    public final Supplier<CharmChiseledBookshelfBlock.BlockItem> item;

    public CustomChiseledBookshelf(CustomWoodHolder holder) {
        var registry = holder.ownerRegistry();
        var material = holder.getMaterial();
        var id = "chiseled_" + holder.getMaterialName() + "_bookshelf";

        block = registry.block(id, () -> new CharmChiseledBookshelfBlock(material));
        item = registry.item(id, () -> new CharmChiseledBookshelfBlock.BlockItem(block));

        // Can be used as furnace fuel.
        registry.fuel(item);

        // Associate blocks with block entity dynamically.
        registry.blockEntityBlocks(() -> BlockEntityType.CHISELED_BOOKSHELF, List.of(block));

        // Add to creative menu.
        holder.addCreativeTabItem(CustomType.CHISELED_BOOKSHELF, item);
    }
}
