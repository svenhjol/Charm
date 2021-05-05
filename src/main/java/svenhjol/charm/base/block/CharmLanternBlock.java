package svenhjol.charm.base.block;

import net.minecraft.block.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.ICharmBlock;

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
