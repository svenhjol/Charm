package svenhjol.charm.feature.clear_item_frames;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import svenhjol.charm.Charm;
import svenhjol.charm_core.annotation.Packet;
import svenhjol.charm_core.enums.PacketDirection;
import svenhjol.charm_core.iface.IPacketRequest;

public class ClearItemFramesNetwork {
    public static void register() {
        Charm.instance().registry().packet(new AddAmethyst(), () -> ClearItemFramesClient::handleItemFrameInteraction);
        Charm.instance().registry().packet(new RemoveAmethyst(), () -> ClearItemFramesClient::handleItemFrameInteraction);
    }

    interface IItemFrameInteraction {
        BlockPos getPos();

        SoundEvent getSound();
    }

    @Packet(
        id = "charm:add_amethyst_to_item_frame",
        direction = PacketDirection.SERVER_TO_CLIENT,
        description = "Send the position of the frame that has had an amethyst shard added."
    )
    static class AddAmethyst implements IPacketRequest, IItemFrameInteraction {
        private BlockPos pos;

        private AddAmethyst() {}

        public static void send(BlockPos pos, ServerPlayer player) {
            var message = new AddAmethyst();
            message.pos = pos;
            Charm.instance().network().send(message, player);
        }

        public BlockPos getPos() {
            return pos;
        }

        @Override
        public SoundEvent getSound() {
            return SoundEvents.SMALL_AMETHYST_BUD_PLACE;
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

    @Packet(
        id = "charm:remove_amethyst_from_item_frame",
        direction = PacketDirection.SERVER_TO_CLIENT,
        description = "Send the position of the frame that has had an amethyst shard removed."
    )
    static class RemoveAmethyst implements IPacketRequest, IItemFrameInteraction {
        private BlockPos pos;

        private RemoveAmethyst() {}

        public static void send(BlockPos pos, ServerPlayer player) {
            var message = new AddAmethyst();
            message.pos = pos;
            Charm.instance().network().send(message, player);
        }

        public BlockPos getPos() {
            return pos;
        }

        @Override
        public SoundEvent getSound() {
            return SoundEvents.SMALL_AMETHYST_BUD_BREAK;
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
