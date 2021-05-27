package svenhjol.charm.block;

import net.minecraft.block.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.charm.module.CharmModule;

public class CharmLanternBlock extends LanternBlock implements ICharmBlock {
    protected CharmModule module;

    public CharmLanternBlock(CharmModule module, String name, AbstractBlock.Settings settings) {
        super(settings);
        this.module = module;
        register(module, name);
    }

    public CharmLanternBlock(CharmModule module, String name) {
        this(module, name, Settings.copy(Blocks.LANTERN));
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
