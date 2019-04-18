package svenhjol.meson;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;

public interface IMesonBlock
{
    String getModId();

    default void createItemBlock(String name)
    {
        Block self = (Block)this;
        ProxyRegistry.register( new ItemBlock(self)
            .setMaxStackSize(getMaxStackSize())
            .setRegistryName(new ResourceLocation(getModId() + ":" + name))
        );
    }

    default void register(String name)
    {
        Block self = (Block)this;

        // set the name of the block.
        self.setTranslationKey(getModId() + ":" + name);
        self.setRegistryName(getModId() + ":" + name);

        ProxyRegistry.blocks.add(self); // so we can register models

        // register the block
        ProxyRegistry.register(self);

        // register the item that the block becomes in your inventory
        this.createItemBlock(name);
    }

    default int getMaxStackSize()
    {
        return 64;
    }

    interface IBlockDropsInventory
    {

    }

    interface IHasCustomStateMapper
    {
        void setStateMapper();
    }

    interface IHasCustomInventoryItemModel
    {
        void setInventoryItemModel();
    }
}
