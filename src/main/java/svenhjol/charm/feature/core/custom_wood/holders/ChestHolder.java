package svenhjol.charm.feature.core.custom_wood.holders;

import svenhjol.charm.feature.core.custom_wood.blocks.CustomChestBlock;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;

import java.util.List;
import java.util.function.Supplier;

public class ChestHolder {
    public final Supplier<CustomChestBlock> block;
    public final Supplier<CustomChestBlock.BlockItem> item;

    public ChestHolder(CustomWoodHolder holder) {
        var feature = holder.feature();
        var registry = holder.ownerRegistry();
        var material = holder.getMaterial();
        var id = holder.getMaterialName() + "_chest";

        block = registry.block(id, () -> new CustomChestBlock(material));
        item = registry.item(id, () -> new CustomChestBlock.BlockItem(block));

        // Chests can be used as furnace fuel.
        registry.fuel(item);

        // Associate the blocks with the block entity dynamically.
        registry.blockEntityBlocks(feature.registers.chestBlockEntity, List.of(block));

        // Add to creative menu.
        holder.addItemToCreativeTab(item, CustomType.CHEST);
    }
}
