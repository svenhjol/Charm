package svenhjol.meson.iface;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;

public interface IMesonBlock
{
    ItemGroup getItemGroup();

    default int getMaxStackSize()
    {
        return 64;
    }

    default BlockItem getBlockItem()
    {
        Item.Properties props = new Item.Properties();
        ItemGroup group = getItemGroup();
        if (group != null) props.group(group);
        props.maxStackSize(getMaxStackSize());

        Block block = (Block) this;
        BlockItem blockItem = new BlockItem(block, props);

        // set the blockitem name to the same as the block, if possible
        ResourceLocation blockName = block.getRegistryName();
        if (blockName != null) {
            blockItem.setRegistryName(blockName);
        }

        return blockItem;
    }
}
