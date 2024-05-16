package svenhjol.charm.feature.core.custom_wood.types;

import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;
import svenhjol.charm.foundation.block.CharmTrappedChestBlock;

import java.util.List;
import java.util.function.Supplier;

public class CustomTrappedChest {
    public final Supplier<CharmTrappedChestBlock> block;
    public final Supplier<CharmTrappedChestBlock.BlockItem> item;

    public CustomTrappedChest(CustomWoodHolder holder) {
        var feature = holder.feature();
        var registry = holder.ownerRegistry();
        var material = holder.getMaterial();
        var id = "trapped_" + holder.getMaterialName() + "_chest";

        block = registry.block(id, () -> new CharmTrappedChestBlock(material));
        item = registry.item(id, () -> new CharmTrappedChestBlock.BlockItem(block));

        // Trapped chests can be used as furnace fuel.
        registry.fuel(item);

        // Associate the blocks with the block entity dynamically.
        registry.blockEntityBlocks(feature.registers.trappedChestBlockEntity, List.of(block));

        // Add to creative menu.
        holder.addCreativeTabItem(CustomType.TRAPPED_CHEST, item);
    }
}
