package svenhjol.meson.iface;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;

public interface IMesonBlock
{
    String getModId();

    String getBaseName();

    ItemGroup getItemGroup();

    BlockItem getBlockItem();
}
