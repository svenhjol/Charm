package svenhjol.charm.base.block;

import net.minecraft.block.Blocks;
import net.minecraft.block.PaneBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.charm.base.CharmModule;

public abstract class CharmBarsBlock extends PaneBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmBarsBlock(CharmModule module, String name, Settings settings) {
        super(settings);

        this.module = module;
        this.register(module, name);
    }

    public CharmBarsBlock(CharmModule module, String name) {
        this(module, name, Settings.copy(Blocks.IRON_BARS));
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
