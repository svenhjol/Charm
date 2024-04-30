package svenhjol.charm.feature.atlases;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import svenhjol.charm.foundation.Networking;

public class ClientNetworking extends Networking<AtlasesClient> {
    public ClientNetworking(AtlasesClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();

        // Client receivers
        registry.packetReceiver(CommonNetworking.SwappedAtlasSlot.TYPE,
            AtlasesClient::handleSwappedSlot);
        registry.packetReceiver(CommonNetworking.UpdateInventory.TYPE,
            AtlasesClient::handleUpdateInventory);
    }

    // Client-to-server
    public record SwapAtlasSlot(int slot) implements CustomPacketPayload {
        static CustomPacketPayload.Type<SwapAtlasSlot> TYPE = CustomPacketPayload.createType("charm:swap_atlas_slot");
        static StreamCodec<FriendlyByteBuf, SwapAtlasSlot> CODEC = StreamCodec.of(SwapAtlasSlot::encode, SwapAtlasSlot::decode);

        public static void send(int slot) {
            ClientPlayNetworking.send(new SwapAtlasSlot(slot));
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public int getSlot() {
            return slot;
        }

        private static void encode(FriendlyByteBuf buf, SwapAtlasSlot self) {
            buf.writeInt(self.slot);
        }

        private static SwapAtlasSlot decode(FriendlyByteBuf buf) {
            return new SwapAtlasSlot(buf.readInt());
        }
    }

    // Client-to-server
    public record TransferAtlas(int atlasSlot, int mapX, int mapZ, MoveMode moveMode) implements CustomPacketPayload {
        static CustomPacketPayload.Type<TransferAtlas> TYPE = CustomPacketPayload.createType("charm:transfer_atlas");
        static StreamCodec<FriendlyByteBuf, TransferAtlas> CODEC = StreamCodec.of(TransferAtlas::encode, TransferAtlas::decode);

        public static void send(int atlasSlot, int mapX, int mapZ, MoveMode moveMode) {
            ClientPlayNetworking.send(new TransferAtlas(atlasSlot, mapX, mapZ, moveMode));
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public int getAtlasSlot() {
            return atlasSlot;
        }

        public int getMapX() {
            return mapX;
        }

        public int getMapZ() {
            return mapZ;
        }

        public MoveMode getMoveMode() {
            return moveMode;
        }

        public static void encode(FriendlyByteBuf buf, TransferAtlas self) {
            buf.writeInt(self.atlasSlot);
            buf.writeInt(self.mapX);
            buf.writeInt(self.mapZ);
            buf.writeEnum(self.moveMode);
        }

        public static TransferAtlas decode(FriendlyByteBuf buf) {
            return new TransferAtlas(
                buf.readInt(),
                buf.readInt(),
                buf.readInt(),
                buf.readEnum(MoveMode.class)
            );
        }
    }
}
