package svenhjol.charm.charmony.common.block.entity;

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
import svenhjol.charm.charmony.Feature;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public abstract class CharmSyncedBlockEntity<F extends Feature> extends CharmBlockEntity<F> {
    public CharmSyncedBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
    }

    @Override
    public CompoundTag getUpdateTag() {
        var updateTag = new CompoundTag();
        this.saveAdditional(updateTag);
        return updateTag;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.syncToClient();
    }

    private void syncToClient() {
        Level level = this.getLevel();
        if (level != null && !level.isClientSide()) {
            syncBlockEntityToClient((ServerLevel)level, this.getBlockPos());
        }
    }

    public static void syncBlockEntityToClient(ServerLevel level, BlockPos pos) {
        level.getChunkSource().blockChanged(pos);
    }
}
