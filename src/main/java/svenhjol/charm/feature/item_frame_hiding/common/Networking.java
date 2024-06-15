package svenhjol.charm.feature.item_frame_hiding.common;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import svenhjol.charm.charmony.annotation.Packet;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.charmony.iface.PacketRequest;
import svenhjol.charm.feature.item_frame_hiding.ItemFrameHiding;

public final class Networking extends FeatureHolder<ItemFrameHiding> {
    public Networking(ItemFrameHiding feature) {
        super(feature);
    }
    
    // Server-to-client
    @Packet(
            id = "charm:add_amethyst_to_item_frame",
            description = "Send the position of the frame that has had an amethyst shard added."
    )
    public static class AddAmethyst implements PacketRequest, ItemFrameInteraction {
        private BlockPos pos;

        public AddAmethyst() {}

        public static void send(BlockPos pos, ServerPlayer player) {
            var message = new AddAmethyst();
            message.pos = pos;
            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
            message.encode(buffer);
            ServerPlayNetworking.send(player, message.id(), buffer);
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

    // Server-to-client
    @Packet(
            id = "charm:remove_amethyst_from_item_frame",
            description = "Send the position of the frame that has had an amethyst shard removed."
    )
    public static class RemoveAmethyst implements PacketRequest, ItemFrameInteraction {
        private BlockPos pos;

        public RemoveAmethyst() {}

        public static void send(BlockPos pos, ServerPlayer player) {
            var message = new AddAmethyst();
            message.pos = pos;
            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
            message.encode(buffer);
            ServerPlayNetworking.send(player, message.id(), buffer);
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

    public interface ItemFrameInteraction {
        BlockPos getPos();
        SoundEvent getSound();
    }
}
