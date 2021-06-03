package svenhjol.charm.block;

import net.minecraft.block.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import svenhjol.charm.module.CharmModule;

public class CharmLanternBlock extends LanternBlock implements ICharmBlock {
    protected CharmModule module;
    private static final VoxelShape STANDING_SHAPE = Block.createCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 11.0D, 11.0D);
    private static final VoxelShape HANGING_SHAPE = Block.createCuboidShape(5.0D, 4.0D, 5.0D, 11.0D, 15.0D, 11.0D);

    public CharmLanternBlock(CharmModule module, String name, AbstractBlock.Settings settings) {
        super(settings);
        this.module = module;
        register(module, name);
    }

    public CharmLanternBlock(CharmModule module, String name) {
        this(module, name, Settings.copy(Blocks.LANTERN));
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(HANGING) ? HANGING_SHAPE : STANDING_SHAPE;
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
