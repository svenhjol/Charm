package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChainBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.IMesonBlock;

public class GoldChainBlock extends ChainBlock implements IMesonBlock {
    private final MesonModule module;

    public GoldChainBlock(MesonModule module) {
        super(AbstractBlock.Settings.copy(Blocks.CHAIN));
        this.module = module;
        this.register(module, "gold_chain");
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.DECORATIONS;
    }

    @Override
    public void addStacksForDisplay(ItemGroup group, DefaultedList<ItemStack> items) {
        if (enabled())
            super.addStacksForDisplay(group, items);
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
