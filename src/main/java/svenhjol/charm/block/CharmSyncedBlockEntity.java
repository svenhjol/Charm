package svenhjol.charm.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.helper.WorldHelper;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public abstract class CharmSyncedBlockEntity extends BlockEntity {
    public CharmSyncedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
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

    public void syncToClient() {
        Level level = getLevel();
        if (level != null && level.isClientSide) {
            WorldHelper.syncBlockEntityToClient((ServerLevel)level, getBlockPos());
        }
    }
}
