package svenhjol.charm.feature.item_frame_hiding.common;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import svenhjol.charm.Charm;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.item_frame_hiding.ItemFrameHiding;

public final class Networking extends FeatureHolder<ItemFrameHiding> {
    public Networking(ItemFrameHiding feature) {
        super(feature);
    }
    
    // Server-to-client
    public record S2CAddAmethyst(BlockPos pos) implements CustomPacketPayload, ItemFrameInteraction {
        public static Type<S2CAddAmethyst> TYPE = new Type<>(Charm.id("add_amethyst_to_item_frame"));
        static StreamCodec<FriendlyByteBuf, S2CAddAmethyst> CODEC = StreamCodec.of(S2CAddAmethyst::encode, S2CAddAmethyst::decode);
        
        public static void send(ServerPlayer player, BlockPos pos) {
            ServerPlayNetworking.send(player, new S2CAddAmethyst(pos));
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

        private static S2CAddAmethyst decode(FriendlyByteBuf buf) {
            return new S2CAddAmethyst(buf.readBlockPos());
        }

        private static void encode(FriendlyByteBuf buf, S2CAddAmethyst self) {
            buf.writeBlockPos(self.pos);
        }
    }

    // Server-to-client
    public record S2CRemoveAmethyst(BlockPos pos) implements CustomPacketPayload, ItemFrameInteraction {
        public static Type<S2CRemoveAmethyst> TYPE = new Type<>(Charm.id("remove_amethyst_from_item_frame"));
        static StreamCodec<FriendlyByteBuf, S2CRemoveAmethyst> CODEC = StreamCodec.of(S2CRemoveAmethyst::encode, S2CRemoveAmethyst::decode);

        public static void send(ServerPlayer player, BlockPos pos) {
            ServerPlayNetworking.send(player, new S2CRemoveAmethyst(pos));
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

        private static S2CRemoveAmethyst decode(FriendlyByteBuf buf) {
            return new S2CRemoveAmethyst(buf.readBlockPos());
        }

        private static void encode(FriendlyByteBuf buf, S2CRemoveAmethyst self) {
            buf.writeBlockPos(self.pos);
        }
    }

    interface ItemFrameInteraction {
        BlockPos getPos();
        SoundEvent getSound();
    }
}
