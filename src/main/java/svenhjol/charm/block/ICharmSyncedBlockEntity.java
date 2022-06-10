package svenhjol.charm.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import svenhjol.charm.helper.WorldHelper;

import javax.annotation.Nullable;

public interface ICharmSyncedBlockEntity {
    @Nullable
    default Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create((BlockEntity)this, BlockEntity::getUpdateTag);
    }

    Level getLevel();

    BlockPos getBlockPos();

    default void syncToClient() {
        Level level = getLevel();
        if (level != null && !level.isClientSide) {
            WorldHelper.syncBlockEntityToClient((ServerLevel)level, getBlockPos());
        }
    }
}
