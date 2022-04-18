package svenhjol.charm.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SlabBlock;
import svenhjol.charm.loader.CharmModule;

public abstract class CharmSlabBlock extends SlabBlock implements ICharmBlock {
    private final CharmModule module;

    public CharmSlabBlock(CharmModule module, String name, Properties settings) {
        super(settings);
        this.register(module, name);
        this.module = module;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (enabled()) {
            super.fillItemCategory(group, items);
        }
    }

    @Override
    public boolean enabled() {
        return module.isEnabled();
    }
}
