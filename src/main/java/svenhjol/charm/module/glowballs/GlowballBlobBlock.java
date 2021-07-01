package svenhjol.charm.module.glowballs;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import svenhjol.charm.loader.CommonModule;
import svenhjol.charm.block.CharmBlock;

import java.util.HashMap;
import java.util.Map;

public class GlowballBlobBlock extends CharmBlock implements SimpleWaterloggedBlock {
    public static final Map<Direction, VoxelShape> SHAPE = new HashMap<>();
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public GlowballBlobBlock(CommonModule module) {
        super(module, "glowball_blob", Properties.of(Material.REPLACEABLE_PLANT)
            .noCollission()
            .instabreak()
            .lightLevel(l -> 12));

        this.registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.defaultBlockState();
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();

        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        boolean isWaterlogged = fluidstate.getType() == Fluids.WATER;

        Direction[] directions = context.getNearestLookingDirections();
        for (Direction direction : directions) {
            Direction opposite = direction.getOpposite();
            state = state.setValue(FACING, opposite);

            if (state.canSurvive(world, pos)) {
                return state.setValue(WATERLOGGED, isWaterlogged);
            }
        }

        return null;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
        if (state.getValue(WATERLOGGED))
            world.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));

        return direction.getOpposite() == state.getValue(FACING) && !state.canSurvive(world, pos) ? Blocks.AIR.defaultBlockState() : state;
    }

    @Override
    public boolean placeLiquid(LevelAccessor worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        if (!state.getValue(BlockStateProperties.WATERLOGGED) && fluidStateIn.getType() == Fluids.WATER) {
            worldIn.setBlock(pos, state.setValue(WATERLOGGED, true), 3);
            worldIn.getLiquidTicks().scheduleTick(pos, fluidStateIn.getType(), fluidStateIn.getType().getTickDelay(worldIn));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockPos = pos.relative(direction.getOpposite());
        BlockState blockState = world.getBlockState(blockPos);
        return blockState.isFaceSturdy(world, blockPos, direction);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    public void createBlockItem(ResourceLocation id) {
        // don't
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        // don't
    }

    static {
        SHAPE.put(Direction.UP, Block.box(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D));
        SHAPE.put(Direction.DOWN, Block.box(3.0D, 15.0D, 3.0D, 13.0D, 16.0D, 13.0D));
        SHAPE.put(Direction.EAST, Block.box(0.0D, 3.0D, 3.0D, 1.0D, 13.0D, 13.0D));
        SHAPE.put(Direction.SOUTH, Block.box(3.0D, 3.0D, 0.0D, 13.0D, 13.0D, 1.0D));
        SHAPE.put(Direction.WEST, Block.box(15.0D, 3.0D, 3.0D, 16.0D, 13.0D, 13.0D));
        SHAPE.put(Direction.NORTH, Block.box(3.0D, 3.0D, 15.0D, 13.0D, 13.0D, 16.0D));
    }
}
