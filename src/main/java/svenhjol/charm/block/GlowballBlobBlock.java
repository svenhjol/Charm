package svenhjol.charm.block;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.CharmBlock;

import java.util.HashMap;
import java.util.Map;

public class GlowballBlobBlock extends CharmBlock implements Waterloggable {
    public static final Map<Direction, VoxelShape> SHAPE = new HashMap<>();
    public static final DirectionProperty FACING = FacingBlock.FACING;
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public GlowballBlobBlock(CharmModule module) {
        super(module, "glowball_blob", Settings.copy(Blocks.REDSTONE_WIRE)
            .luminance(l -> 8));

        this.setDefaultState(getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE.get(state.get(FACING));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState state = this.getDefaultState();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();

        FluidState fluidstate = context.getWorld().getFluidState(context.getBlockPos());
        boolean isWaterlogged = fluidstate.getFluid() == Fluids.WATER;

        Direction[] directions = context.getPlacementDirections();
        for (Direction direction : directions) {
            Direction opposite = direction.getOpposite();
            state = state.with(FACING, opposite);

            if (state.canPlaceAt(world, pos)) {
                return state.with(WATERLOGGED, isWaterlogged);
            }
        }

        return null;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if (state.get(WATERLOGGED))
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));

        return direction.getOpposite() == state.get(FACING) && !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : state;
    }

    @Override
    public boolean tryFillWithFluid(WorldAccess worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        if (!state.get(Properties.WATERLOGGED) && fluidStateIn.getFluid() == Fluids.WATER) {
            worldIn.setBlockState(pos, state.with(WATERLOGGED, true), 3);
            worldIn.getFluidTickScheduler().schedule(pos, fluidStateIn.getFluid(), fluidStateIn.getFluid().getTickRate(worldIn));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction direction = state.get(FACING);
        BlockPos blockPos = pos.offset(direction.getOpposite());
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isSideSolidFullSquare(world, blockPos, direction);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public void createBlockItem(Identifier id) {
        // don't
    }

    @Override
    public void addStacksForDisplay(ItemGroup group, DefaultedList<ItemStack> items) {
        // don't
    }

    static {
        SHAPE.put(Direction.UP, Block.createCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D));
        SHAPE.put(Direction.DOWN, Block.createCuboidShape(3.0D, 15.0D, 3.0D, 13.0D, 16.0D, 13.0D));
        SHAPE.put(Direction.EAST, Block.createCuboidShape(0.0D, 3.0D, 3.0D, 1.0D, 13.0D, 13.0D));
        SHAPE.put(Direction.SOUTH, Block.createCuboidShape(3.0D, 3.0D, 0.0D, 13.0D, 13.0D, 1.0D));
        SHAPE.put(Direction.WEST, Block.createCuboidShape(15.0D, 3.0D, 3.0D, 16.0D, 13.0D, 13.0D));
        SHAPE.put(Direction.NORTH, Block.createCuboidShape(3.0D, 3.0D, 15.0D, 13.0D, 13.0D, 16.0D));
    }
}
