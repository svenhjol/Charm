package svenhjol.charm.block;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.FallingBlock;
import svenhjol.charm.loader.CommonModule;

@SuppressWarnings({"NullableProblems", "unused"})
public abstract class CharmFallingBlock extends FallingBlock implements ICharmFallingBlock {
    protected CommonModule module;

    public CharmFallingBlock(CommonModule module, String name, Properties props) {
        super(props);
        this.module = module;
        register(module, name);
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_BUILDING_BLOCKS;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (enabled())
            super.fillItemCategory(group, items);
    }

    @Override
    public boolean enabled() {
        return module.isEnabled();
    }
}
