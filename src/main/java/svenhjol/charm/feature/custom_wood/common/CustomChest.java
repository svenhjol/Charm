package svenhjol.charm.feature.custom_wood.common;

import svenhjol.charm.foundation.block.CharmChestBlock;

import java.util.List;
import java.util.function.Supplier;

public class CustomChest {
    public final Supplier<CharmChestBlock> block;
    public final Supplier<CharmChestBlock.BlockItem> item;

    public CustomChest(CustomWoodHolder holder) {
        var feature = holder.feature();
        var registry = holder.ownerRegistry();
        var material = holder.getMaterial();
        var id = holder.getMaterialName() + "_chest";

        block = registry.block(id, () -> new CharmChestBlock(material));
        item = registry.item(id, () -> new CharmChestBlock.BlockItem(block));

        // Chests can be used as furnace fuel.
        registry.fuel(item);

        // Associate the blocks with the block entity dynamically.
        registry.blockEntityBlocks(feature.registers.chestBlockEntity, List.of(block));

        // Add to creative menu.
        holder.addCreativeTabItem(CustomWoodHelper.CHESTS, item);
    }
}
