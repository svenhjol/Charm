package svenhjol.charm.tweaks.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.PushReaction;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class ImprovedLanternBlock extends FallingBlock implements IWaterLoggable {
    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape NORMAL_SHAPE = VoxelShapes.or(Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 7.0D, 11.0D), Block.makeCuboidShape(6.0D, 7.0D, 6.0D, 10.0D, 9.0D, 10.0D));
    protected static final VoxelShape HANGING_SHAPE = VoxelShapes.or(Block.makeCuboidShape(5.0D, 1.0D, 5.0D, 11.0D, 8.0D, 11.0D), Block.makeCuboidShape(6.0D, 8.0D, 6.0D, 10.0D, 10.0D, 10.0D));

    public ImprovedLanternBlock(Block.Properties props) {
        super(props);
        this.setDefaultState(this.stateContainer.getBaseState()
            .with(HANGING, false)
            .with(WATERLOGGED, false)
        );
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        for(Direction direction : context.getNearestLookingDirections()) {
            if (direction.getAxis() == Direction.Axis.Y) {
                BlockState blockstate = this.getDefaultState().with(HANGING, direction == Direction.UP);
                if (blockstate.isValidPosition(context.getWorld(), context.getPos())) {
                    return blockstate;
                }
            }
        }
        return null;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return state.get(HANGING) ? HANGING_SHAPE : NORMAL_SHAPE;
    }

    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        Direction direction = getHangingDirection(state).getOpposite();
        return Block.hasEnoughSolidSide(worldIn, pos.offset(direction), direction.getOpposite());
    }

    protected static Direction getHangingDirection(BlockState p_220277_0_) {
        return p_220277_0_.get(HANGING) ? Direction.DOWN : Direction.UP;
    }

    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.DESTROY;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(HANGING, WATERLOGGED);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }
        if (!stateIn.isValidPosition(worldIn, currentPos)) {
            return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        }
        return stateIn;
    }

    @Override
    public IFluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (state.get(HANGING) && worldIn.isAirBlock(pos.up())) {
            worldIn.setBlockState(pos, state.with(HANGING, false));
            super.tick(state, worldIn, pos, rand);
        } else if (!state.get(HANGING)) {
            super.tick(state, worldIn, pos, rand);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        // don't
    }
}
