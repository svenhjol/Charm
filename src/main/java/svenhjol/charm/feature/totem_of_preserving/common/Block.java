package svenhjol.charm.feature.totem_of_preserving.common;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreserving;
import svenhjol.charm.charmony.feature.FeatureResolver;

public class Block extends BaseEntityBlock implements FeatureResolver<TotemOfPreserving> {
    private static final MapCodec<Block> CODEC = simpleCodec(Block::new);
    private static final VoxelShape SHAPE = net.minecraft.world.level.block.Block.box(2, 2, 2, 14, 14, 14);

    public Block() {
        this(Properties.ofFullCopy(Blocks.GLASS)
            .strength(-1.0f, 3600000.0f)
            .noCollission()
            .noOcclusion()
            .noLootTable());
    }

    private Block(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public net.minecraft.world.level.block.entity.BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntity(pos, state);
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
        feature().handlers.serverWantsToRemoveBlock(level, pos);
        super.onRemove(blockState, level, pos, state, bl);
    }

    /**
     * Executed when an entity is within the block bounds.
     */
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        feature().handlers.playerEnteredBlock(level, pos, entity);
        super.entityInside(state, level, pos, entity);
    }

    @Override
    public Class<TotemOfPreserving> typeForFeature() {
        return TotemOfPreserving.class;
    }
}
