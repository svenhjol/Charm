package svenhjol.meson.block;

import net.minecraft.block.Block;
import net.minecraft.block.FallingBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.IMesonFallingBlock;

public abstract class MesonFallingBlock extends FallingBlock implements IMesonFallingBlock {
    protected MesonModule module;

    public MesonFallingBlock(MesonModule module, String name, Block.Properties props) {
        super(props);
        this.module = module;
        register(module, name);
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.BUILDING_BLOCKS;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isEnabled())
            super.fillItemGroup(group, items);
    }

    @Override
    public boolean isEnabled() {
        return module.enabled;
    }
}
