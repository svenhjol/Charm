package svenhjol.meson.iface;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public interface IMesonBlock
{
    String getModId();

    String getBaseName();

    ItemGroup getItemGroup();

    default BlockItem getBlockItem()
    {
        Item.Properties props = new Item.Properties();
        ItemGroup group = getItemGroup();
        if (group != null) props.group(group);

        return new BlockItem((Block)this, props);
    }
}
