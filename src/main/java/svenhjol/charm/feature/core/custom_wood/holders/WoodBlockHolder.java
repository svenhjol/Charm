package svenhjol.charm.feature.core.custom_wood.holders;

import svenhjol.charm.feature.core.custom_wood.blocks.CustomLogBlock;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;

import java.util.function.Supplier;

public class WoodBlockHolder {
    public final Supplier<CustomLogBlock> block;
    public final Supplier<CustomLogBlock.BlockItem> item;
    public final Supplier<CustomLogBlock> strippedBlock;
    public final Supplier<CustomLogBlock.BlockItem> strippedItem;

    public WoodBlockHolder(CustomWoodHolder holder) {
        var material = holder.getMaterial();
        var registry = holder.ownerRegistry();

        var woodId = holder.getMaterialName() + "_wood";
        var strippedWoodId = "stripped_" + holder.getMaterialName() + "_wood";

        block = registry.block(woodId, () -> new CustomLogBlock(material));
        item = registry.item(woodId, () -> new CustomLogBlock.BlockItem(block));
        strippedBlock = registry.block(strippedWoodId, () -> new CustomLogBlock(material));
        strippedItem = registry.item(strippedWoodId, () -> new CustomLogBlock.BlockItem(strippedBlock));

        // Wood can set on fire.
        registry.ignite(block);
        registry.ignite(strippedBlock);

        // Wood can be stripped.
        registry.strippable(block, strippedBlock);

        holder.addItemToCreativeTab(item, CustomType.WOOD);
        holder.addItemToCreativeTab(strippedItem, CustomType.STRIPPED_WOOD);
    }
}
