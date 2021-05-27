package svenhjol.charm.module.storage_crates;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.block.CharmBlockWithEntity;
import svenhjol.charm.enums.IVariantMaterial;

import javax.annotation.Nullable;

public class StorageCrateBlock extends CharmBlockWithEntity {
    public static final DirectionProperty FACING = Properties.FACING;
    private static final VoxelShape SHAPE = createCuboidShape(1.0D, 1.0D, 1.0D, 15.0D, 16.0D, 15.0D);

    public StorageCrateBlock(CharmModule module, IVariantMaterial material) {
        super(module, material.asString() + "_storage_crate",
            Settings.copy(Blocks.COMPOSTER)
        );
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
        return SHAPE;
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        StorageCrateBlockEntity crate = getBlockEntity(world, pos);

        if (!world.isClient && crate != null && !crate.isEmpty()) {
            ItemScatterer.spawn(world, pos, crate.getInvStackList());
        }

        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Nullable
    public StorageCrateBlockEntity getBlockEntity(World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof StorageCrateBlockEntity)
            return (StorageCrateBlockEntity) blockEntity;

        return null;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StorageCrateBlockEntity(pos, state);
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.DECORATIONS;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }
}
