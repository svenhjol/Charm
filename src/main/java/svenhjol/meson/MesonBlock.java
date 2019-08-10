package svenhjol.meson;

import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import svenhjol.meson.iface.IMesonBlock;

public abstract class MesonBlock extends Block implements IMesonBlock
{
    public MesonBlock(Block.Properties props)
    {
        super(props);
    }

    @Override
    public ItemGroup getItemGroup()
    {
        return ItemGroup.BUILDING_BLOCKS;
    }
}
