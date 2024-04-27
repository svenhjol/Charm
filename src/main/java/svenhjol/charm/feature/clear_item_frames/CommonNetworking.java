package svenhjol.charm.feature.clear_item_frames;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import svenhjol.charm.foundation.Networking;

public class CommonNetworking extends Networking<ClearItemFrames> {
    public CommonNetworking(ClearItemFrames feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        feature.registry().packetSender(CommonNetworking.AddAmethyst.TYPE, CommonNetworking.AddAmethyst.CODEC);
        feature.registry().packetSender(CommonNetworking.RemoveAmethyst.TYPE, CommonNetworking.RemoveAmethyst.CODEC);
    }

    interface ItemFrameInteraction {
        BlockPos getPos();
        SoundEvent getSound();
    }

    record AddAmethyst(BlockPos pos) implements CustomPacketPayload, ItemFrameInteraction {
        static CustomPacketPayload.Type<AddAmethyst> TYPE = CustomPacketPayload.createType("charm:add_amethyst_to_item_frame");
        static StreamCodec<FriendlyByteBuf, AddAmethyst> CODEC = StreamCodec.of(AddAmethyst::encode, AddAmethyst::decode);

        public static void send(ServerPlayer player, BlockPos pos) {
            ServerPlayNetworking.send(player, new AddAmethyst(pos));
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public BlockPos getPos() {
            return pos;
        }

        public SoundEvent getSound() {
            return SoundEvents.SMALL_AMETHYST_BUD_PLACE;
        }

        private static AddAmethyst decode(FriendlyByteBuf buf) {
            return new AddAmethyst(buf.readBlockPos());
        }

        private static void encode(FriendlyByteBuf buf, AddAmethyst self) {
            buf.writeBlockPos(self.pos);
        }
    }

    record RemoveAmethyst(BlockPos pos) implements CustomPacketPayload, ItemFrameInteraction {
        static CustomPacketPayload.Type<RemoveAmethyst> TYPE = CustomPacketPayload.createType("charm:remove_amethyst_from_item_frame");
        static StreamCodec<FriendlyByteBuf, RemoveAmethyst> CODEC = StreamCodec.of(RemoveAmethyst::encode, RemoveAmethyst::decode);

        public static void send(ServerPlayer player, BlockPos pos) {
            ServerPlayNetworking.send(player, new RemoveAmethyst(pos));
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public BlockPos getPos() {
            return pos;
        }

        public SoundEvent getSound() {
            return SoundEvents.SMALL_AMETHYST_BUD_PLACE;
        }

        private static RemoveAmethyst decode(FriendlyByteBuf buf) {
            return new RemoveAmethyst(buf.readBlockPos());
        }

        private static void encode(FriendlyByteBuf buf, RemoveAmethyst self) {
            buf.writeBlockPos(self.pos);
        }
    }
}
