package svenhjol.charm.world.block;

import com.google.common.base.Predicates;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import svenhjol.charm.world.module.EndPortalRunes;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;
import svenhjol.meson.enums.ColorVariant;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class RunePortalFrameBlock extends MesonBlock {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final EnumProperty<ColorVariant> RUNE = EnumProperty.create("rune", ColorVariant.class);
    protected static final VoxelShape BASE_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 13.0D, 16.0D);
    protected static final VoxelShape RUNE_SHAPE = Block.makeCuboidShape(4.0D, 13.0D, 4.0D, 12.0D, 16.0D, 12.0D);
    protected static final VoxelShape BASE_WITH_RUNE_SHAPE = VoxelShapes.or(BASE_SHAPE, RUNE_SHAPE);

    private static BlockPattern portalShape;

    public RunePortalFrameBlock(MesonModule module) {
        super(module, "rune_portal_frame", Block.Properties
            .create(Material.ROCK, MaterialColor.GREEN)
            .sound(SoundType.GLASS)
            .hardnessAndResistance(-1.0F, 3600000.0F)
            .lightValue(1)
            .noDrops()
        );
        this.setDefaultState(this.getStateContainer().getBaseState()
            .with(FACING, Direction.NORTH)
            .with(RUNE, ColorVariant.WHITE)
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return BASE_WITH_RUNE_SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState()
            .with(FACING, context.getPlacementHorizontalFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, RUNE);
    }

    @Override
    public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
        return false;
    }

    // TODO unlikely to work, needs to get state somehow
    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return new ItemStack(this, 1);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);

        Direction facing;

        if (placer != null) {
            facing = placer.getHorizontalFacing().getOpposite();
            state = state.with(RunePortalFrameBlock.FACING, facing);
            world.setBlockState(pos, state, 2);
        }

        if (world instanceof ServerWorld) {
            EndPortalRunes.activate((ServerWorld) world, pos);
        }
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onReplaced(state, world, pos, newState, isMoving);

        if (world instanceof ServerWorld) {
            EndPortalRunes.deactivate((ServerWorld) world, pos);
        }
    }

    @SuppressWarnings("Guava")
    public static BlockPattern getOrCreatePortalShape() {
        if (portalShape == null) {
            portalShape = BlockPatternBuilder.start().aisle("?vvv?", ">???<", ">???<", ">???<", "?^^^?")
                .where('?', CachedBlockInfo.hasState(BlockStateMatcher.ANY))
                .where('^', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(EndPortalRunes.frame).where(FACING, Predicates.equalTo(Direction.SOUTH))))
                .where('>', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(EndPortalRunes.frame).where(FACING, Predicates.equalTo(Direction.WEST))))
                .where('v', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(EndPortalRunes.frame).where(FACING, Predicates.equalTo(Direction.NORTH))))
                .where('<', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(EndPortalRunes.frame).where(FACING, Predicates.equalTo(Direction.EAST))))
                .build();
        }

        return portalShape;
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.SEARCH;
    }
}
