package svenhjol.charm.foundation.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.feature.FeatureResolver;

public abstract class CharmBlockEntity<F extends Feature> extends BlockEntity implements FeatureResolver<F> {
    public CharmBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
