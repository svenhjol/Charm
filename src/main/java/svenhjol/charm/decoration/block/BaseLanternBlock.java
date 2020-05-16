package svenhjol.charm.decoration.block;

import net.minecraft.block.Block;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import svenhjol.charm.tweaks.block.ImprovedLanternBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.IMesonBlock;

public abstract class BaseLanternBlock extends ImprovedLanternBlock implements IMesonBlock, IWaterLoggable {
    protected MesonModule module;

    public BaseLanternBlock(MesonModule module, String name, Block.Properties props) {
        super(props);
        this.module = module;
        register(module, name);
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.DECORATIONS;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isEnabled() && group == ItemGroup.SEARCH) {
            super.fillItemGroup(group, items);
        }
    }

    @Override
    public boolean isEnabled() {
        return module.enabled;
    }
}
