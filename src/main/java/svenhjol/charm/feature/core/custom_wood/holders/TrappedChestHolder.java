package svenhjol.charm.feature.core.custom_wood.holders;

import svenhjol.charm.feature.core.custom_wood.blocks.CustomTrappedChestBlock;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;

import java.util.List;
import java.util.function.Supplier;

public class TrappedChestHolder {
    public final Supplier<CustomTrappedChestBlock> block;
    public final Supplier<CustomTrappedChestBlock.BlockItem> item;

    public TrappedChestHolder(CustomWoodHolder holder) {
        var feature = holder.feature();
        var registry = holder.ownerRegistry();
        var material = holder.getMaterial();
        var id = "trapped_" + holder.getMaterialName() + "_chest";

        block = registry.block(id, () -> new CustomTrappedChestBlock(material));
        item = registry.item(id, () -> new CustomTrappedChestBlock.BlockItem(block));

        // Trapped chests can be used as furnace fuel.
        registry.fuel(item);

        // Associate the blocks with the block entity dynamically.
        registry.blockEntityBlocks(feature.registers.trappedChestBlockEntity, List.of(block));

        // Add to creative menu.
        holder.addItemToCreativeTab(item, CustomType.TRAPPED_CHEST);
    }
}
