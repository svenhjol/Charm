package svenhjol.charm.module.storage_crates;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import svenhjol.charm.block.CharmBlockWithEntity;
import svenhjol.charm.enums.IVariantMaterial;
import svenhjol.charm.loader.CharmModule;

import javax.annotation.Nullable;

public class StorageCrateBlock extends CharmBlockWithEntity {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    private static final VoxelShape INSIDE_UP = box(1.0D, 1.0D, 1.0D, 15.0D, 16.0D, 15.0D);
    private static final VoxelShape INSIDE_DOWN = box(1.0D, 0.0D, 1.0D, 15.0D, 15.0D, 15.0D);
    private static final VoxelShape INSIDE_NORTH = box(0.0D, 1.0D, 1.0D, 15.0D, 15.0D, 15.0D);
    private static final VoxelShape INSIDE_SOUTH = box(1.0D, 1.0D, 1.0D, 16.0D, 15.0D, 15.0D);
    private static final VoxelShape INSIDE_EAST = box(1.0D, 1.0D, 0.0D, 15.0D, 15.0D, 15.0D);
    private static final VoxelShape INSIDE_WEST = box(1.0D, 1.0D, 1.0D, 15.0D, 15.0D, 16.0D);

    private static final VoxelShape SHAPE_UP = Shapes.join(Shapes.block(), Shapes.or(INSIDE_UP), BooleanOp.ONLY_FIRST);
    private static final VoxelShape SHAPE_DOWN = Shapes.join(Shapes.block(), Shapes.or(INSIDE_DOWN), BooleanOp.ONLY_FIRST);
    private static final VoxelShape SHAPE_NORTH = Shapes.join(Shapes.block(), Shapes.or(INSIDE_NORTH), BooleanOp.ONLY_FIRST);
    private static final VoxelShape SHAPE_SOUTH = Shapes.join(Shapes.block(), Shapes.or(INSIDE_SOUTH), BooleanOp.ONLY_FIRST);
    private static final VoxelShape SHAPE_EAST = Shapes.join(Shapes.block(), Shapes.or(INSIDE_EAST), BooleanOp.ONLY_FIRST);
    private static final VoxelShape SHAPE_WEST = Shapes.join(Shapes.block(), Shapes.or(INSIDE_WEST), BooleanOp.ONLY_FIRST);

    public StorageCrateBlock(CharmModule module, IVariantMaterial material) {
        super(module, material.getSerializedName() + "_storage_crate",
            Properties.copy(Blocks.COMPOSTER)
        );

        if (material.isFlammable()) {
            this.setBurnTime(300);
        } else {
            this.setFireproof();
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return this.getShape(state);
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter world, BlockPos pos) {
        return this.getShape(state);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        StorageCrateBlockEntity crate = getBlockEntity(world, pos);

        if (!world.isClientSide && crate != null && !crate.isEmpty()) {
            Containers.dropContents(world, pos, crate.getItems());
        }

        super.onRemove(state, world, pos, newState, moved);
    }

    @Nullable
    public StorageCrateBlockEntity getBlockEntity(Level world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof StorageCrateBlockEntity)
            return (StorageCrateBlockEntity) blockEntity;

        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StorageCrateBlockEntity(pos, state);
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_DECORATIONS;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getNearestLookingDirection().getOpposite());
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    private VoxelShape getShape(BlockState state) {
        switch (state.getValue(FACING)) {
            case DOWN:
                return SHAPE_DOWN;

            case NORTH:
                return SHAPE_NORTH;

            case SOUTH:
                return SHAPE_SOUTH;

            case EAST:
                return SHAPE_EAST;

            case WEST:
                return SHAPE_WEST;

            default:
            case UP:
                return SHAPE_UP;
        }
    }
}
