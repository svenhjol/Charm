package svenhjol.meson.iface;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonItemBlock;
import svenhjol.meson.registry.ProxyRegistry;

public interface IMesonBlock
{
    default void register()
    {
        register(getName());
    }

    default void register(String name)
    {
        Block self = (Block)this;

        // set the name of the block.
        ResourceLocation res = new ResourceLocation(getModId() + ":" + name);
        self.setTranslationKey(res.toString());
        self.setRegistryName(res);

        // register the block
        ProxyRegistry.register(self);

        ProxyRegistry.blocks.add(self);

        // create an ItemBlock for this block
        registerItemBlock(self, name);
    }

    default void registerItemBlock(Block block, String name)
    {
        MesonItemBlock itemBlock = null;

        // create a new itemblock instance for this block
        try {
            itemBlock = getItemBlockClass()
                .getConstructor(Block.class, String.class)
                .newInstance(block, name);

        } catch (Exception e) {
            Meson.runtimeException("Error creating item block");
        }

        itemBlock.setMaxStackSize(getMaxStackSize());

        // register the itemblock
        ProxyRegistry.register(itemBlock);

        // add to "items" for model handler
        ProxyRegistry.items.add(itemBlock);
    }

    default Class<? extends MesonItemBlock> getItemBlockClass()
    {
       return MesonItemBlock.class;
    }

    default String[] getVariants()
    {
        return new String[] { getName() };
    }

    default int getMaxStackSize()
    {
        return 64;
    }

    String getModId();

    String getName();

    interface IHasCustomStateMapper
    {
        void setStateMapper();
    }

    interface IHasCustomItemBlockModel
    {
        void setInventoryItemModel();
    }
}
