package svenhjol.meson.block;

import net.minecraft.block.BlockWithEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.meson.MesonModule;

public abstract class MesonBlockEntity extends BlockWithEntity implements IMesonBlock {
    public MesonModule module;

    protected MesonBlockEntity(MesonModule module, String name, Settings props) {
        super(props);
        this.module = module;
        register(module, name);
    }

    @Override
    public void addStacksForDisplay(ItemGroup group, DefaultedList<ItemStack> list) {
        if (enabled())
            super.addStacksForDisplay(group, list);
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
