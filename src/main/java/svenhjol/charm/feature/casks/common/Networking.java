package svenhjol.charm.feature.casks.common;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import svenhjol.charm.charmony.annotation.Packet;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.charmony.helper.PlayerHelper;
import svenhjol.charm.charmony.iface.PacketRequest;
import svenhjol.charm.feature.casks.Casks;

public final class Networking extends FeatureHolder<Casks> {
    public Networking(Casks feature) {
        super(feature);
    }

    @Packet(
            id = "charm:added_to_cask",
            description = "Send the position of the cask that has had a potion added."
    )
    public static class S2CAddedToCask implements PacketRequest {
        private BlockPos pos;

        public S2CAddedToCask() {this(BlockPos.ZERO);}

        public S2CAddedToCask(BlockPos pos) {
            this.pos = pos;
        }

        public static void send(ServerLevel level, BlockPos pos) {
            var message = new S2CAddedToCask(pos);
            var buffer = new FriendlyByteBuf(Unpooled.buffer());
            message.encode(buffer);
            PlayerHelper.getPlayersInRange(level, pos, 8.0d).
                    forEach(player -> ServerPlayNetworking.send((ServerPlayer) player, message.id(), buffer));
        }

        public BlockPos getPos() {
            return pos;
        }

        @Override
        public void encode(FriendlyByteBuf buf) {
            buf.writeBlockPos(pos);
        }

        @Override
        public void decode(FriendlyByteBuf buf) {
            pos = buf.readBlockPos();
        }
    }
}
