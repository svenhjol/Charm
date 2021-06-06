package svenhjol.charm.block;

import net.minecraft.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import svenhjol.charm.block.ICharmBlock;
import svenhjol.charm.module.CharmModule;

public class CharmLanternBlock extends LanternBlock implements ICharmBlock {
    protected CharmModule module;
    private static final VoxelShape AABB = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 11.0D, 11.0D);
    private static final VoxelShape HANGING_AABB = Block.box(5.0D, 4.0D, 5.0D, 11.0D, 15.0D, 11.0D);

    public CharmLanternBlock(CharmModule module, String name, BlockBehaviour.Properties settings) {
        super(settings);
        this.module = module;
        register(module, name);
    }

    public CharmLanternBlock(CharmModule module, String name) {
        this(module, name, Properties.copy(Blocks.LANTERN));
    }

    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return state.getValue(HANGING) ? HANGING_AABB : AABB;
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_DECORATIONS;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> list) {
        if (enabled())
            super.fillItemCategory(group, list);
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
