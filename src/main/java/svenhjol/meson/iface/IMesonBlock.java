package svenhjol.meson.iface;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import svenhjol.meson.handler.RegistryHandler;

public interface IMesonBlock
{
    ItemGroup getItemGroup();

    default int getMaxStackSize()
    {
        return 64;
    }

    default void register(ResourceLocation res)
    {
        RegistryHandler.registerBlock((Block)this, res);
    }

    default BlockItem getBlockItem()
    {
        Item.Properties props = new Item.Properties();
        ItemGroup group = getItemGroup();
        if (group != null) props.group(group);
        props.maxStackSize(getMaxStackSize());

        Block block = (Block) this;
        return new BlockItem(block, props);
    }
}
