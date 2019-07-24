package svenhjol.meson;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import svenhjol.meson.handler.RegistrationHandler;
import svenhjol.meson.iface.IMesonBlock;

public abstract class MesonBlock extends Block implements IMesonBlock
{
    protected String baseName;

    public MesonBlock(String baseName, Block.Properties props)
    {
        super(props);
        this.baseName = baseName;
        RegistrationHandler.addBlock(this);
    }

    @Override
    public String getBaseName()
    {
        return baseName;
    }

    @Override
    public ItemGroup getItemGroup()
    {
        return ItemGroup.BUILDING_BLOCKS;
    }

    @Override
    public BlockItem getBlockItem()
    {
        final Item.Properties props = new Item.Properties().group(getItemGroup());
        return new BlockItem(this, props);
    }
}
