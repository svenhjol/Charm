package svenhjol.charm.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SuppressWarnings("unused")
public abstract class CharmSyncedBlockEntity extends BlockEntity implements ICharmSyncedBlockEntity {
    public CharmSyncedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public CompoundTag getUpdateTag() {
        CompoundTag updateTag = new CompoundTag();
        saveAdditional(updateTag);
        return updateTag;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        syncToClient();
    }
}
