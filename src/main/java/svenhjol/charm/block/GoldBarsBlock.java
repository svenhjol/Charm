package svenhjol.charm.block;

import net.minecraft.block.Blocks;
import net.minecraft.block.PaneBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.ICharmBlock;

public class GoldBarsBlock extends PaneBlock implements ICharmBlock {
    private CharmModule module;

    public GoldBarsBlock(CharmModule module) {
        super(Settings.copy(Blocks.IRON_BARS));
        this.module = module;
        this.register(module, "gold_bars");
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
