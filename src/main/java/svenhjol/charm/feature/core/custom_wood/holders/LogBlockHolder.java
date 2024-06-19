package svenhjol.charm.feature.core.custom_wood.holders;

import svenhjol.charm.feature.core.custom_wood.blocks.CustomLogBlock;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;

import java.util.function.Supplier;

public class LogBlockHolder {
    public final Supplier<CustomLogBlock> block;
    public final Supplier<CustomLogBlock.BlockItem> item;
    public final Supplier<CustomLogBlock> strippedBlock;
    public final Supplier<CustomLogBlock.BlockItem> strippedItem;

    public LogBlockHolder(CustomWoodHolder holder) {
        var material = holder.getMaterial();
        var registry = holder.ownerRegistry();

        var id = holder.getMaterialName() + "_log";
        var strippedId = "stripped_" + holder.getMaterialName() + "_log";

        block = registry.block(id, () -> new CustomLogBlock(material));
        item = registry.item(id, () -> new CustomLogBlock.BlockItem(block));
        strippedBlock = registry.block(strippedId, () -> new CustomLogBlock(material));
        strippedItem = registry.item(strippedId, () -> new CustomLogBlock.BlockItem(strippedBlock));

        // Logs can set on fire.
        registry.ignite(block);
        registry.ignite(strippedBlock);

        // Logs can be stripped.
        registry.strippable(block, strippedBlock);

        holder.addItemToCreativeTab(item, CustomType.LOG);
        holder.addItemToCreativeTab(strippedItem, CustomType.STRIPPED_LOG);
    }
}
