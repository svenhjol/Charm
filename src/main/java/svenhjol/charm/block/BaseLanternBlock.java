package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.IMesonBlock;

public abstract class BaseLanternBlock extends ImprovedLanternBlock implements IMesonBlock {
    protected MesonModule module;

    public BaseLanternBlock(MesonModule module, String name, AbstractBlock.Settings settings) {
        super(settings);
        this.module = module;
        register(module, name);
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.DECORATIONS;
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
