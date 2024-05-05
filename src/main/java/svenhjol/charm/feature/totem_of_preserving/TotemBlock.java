package svenhjol.charm.feature.totem_of_preserving;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class TotemBlock extends BaseEntityBlock {
    static final MapCodec<TotemBlock> CODEC = simpleCodec(TotemBlock::new);
    static final VoxelShape SHAPE = Block.box(2, 2, 2, 14, 14, 14);

    public TotemBlock() {
        this(Properties.ofFullCopy(Blocks.GLASS)
            .strength(-1.0f, 3600000.0f)
            .noCollission()
            .noOcclusion()
            .noLootTable());
    }

    private TotemBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TotemBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    /**
     * Executed when the game wants to delete a totem block.
     */
    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState state, boolean bl) {
        CommonHandlers.handleServerWantsToRemoveBlock(level, pos);
        super.onRemove(blockState, level, pos, state, bl);
    }

    /**
     * Executed when an entity is within the block bounds.
     */
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        CommonHandlers.handlePlayerEnteredBlock(level, pos, entity);
        super.entityInside(state, level, pos, entity);
    }
}
