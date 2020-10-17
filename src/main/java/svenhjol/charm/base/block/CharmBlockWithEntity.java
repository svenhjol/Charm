package svenhjol.charm.base.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.charm.base.CharmModule;

public abstract class CharmBlockWithEntity extends BlockWithEntity implements ICharmBlock {
    public CharmModule module;

    protected CharmBlockWithEntity(CharmModule module, String name, Settings props) {
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

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
}
