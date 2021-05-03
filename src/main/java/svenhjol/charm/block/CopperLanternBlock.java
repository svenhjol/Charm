package svenhjol.charm.block;

import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import svenhjol.charm.base.CharmModule;

public class CopperLanternBlock extends BaseLanternBlock {
    private static final VoxelShape STANDING_SHAPE = Block.createCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 11.0D, 11.0D);
    private static final VoxelShape HANGING_SHAPE = Block.createCuboidShape(5.0D, 4.0D, 5.0D, 11.0D, 15.0D, 11.0D);

    public CopperLanternBlock(CharmModule module, String name) {
        super(module, name, AbstractBlock.Settings.copy(Blocks.LANTERN));
    }

    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(HANGING) ? HANGING_SHAPE : STANDING_SHAPE;
    }
}
