package svenhjol.charm.feature.atlases;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ComplexItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import svenhjol.charm.foundation.feature.Network;

public final class CommonNetworking extends Network<Atlases> {
    public CommonNetworking(Atlases feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();

        // Server senders
        registry.serverPacketSender(S2CSwappedAtlasSlot.TYPE, S2CSwappedAtlasSlot.CODEC);
        registry.serverPacketSender(S2CUpdateInventory.TYPE, S2CUpdateInventory.CODEC);

        // Client senders
        registry.clientPacketSender(C2SSwapAtlasSlot.TYPE, C2SSwapAtlasSlot.CODEC);
        registry.clientPacketSender(C2STransferAtlas.TYPE, C2STransferAtlas.CODEC);

        // Server receivers
        registry.packetReceiver(C2SSwapAtlasSlot.TYPE,
            Atlases::handleSwappedSlot);
        registry.packetReceiver(C2STransferAtlas.TYPE,
            Atlases::handleTransferAtlas);
    }

    public static void sendMapToClient(ServerPlayer player, ItemStack map, boolean markDirty) {
        if (map.getItem().isComplex()) {
            if (markDirty) {
                var mapData = MapItem.getSavedData(map, player.level());

                if (mapData == null) {
                    return;
                }

                mapData.setColorsDirty(0, 0);
            }

            map.getItem().inventoryTick(map, player.level(), player, -1, true);
            var packet = ((ComplexItem) map.getItem()).getUpdatePacket(map, player.level(), player);

            if (packet != null) {
                player.connection.send(packet);
            }
        }
    }

    // Server-to-client
    public record S2CSwappedAtlasSlot(int slot) implements CustomPacketPayload {
        static final String ID = "charm:swapped_atlas_slot";
        static CustomPacketPayload.Type<S2CSwappedAtlasSlot> TYPE
            = CustomPacketPayload.createType(ID);
        static StreamCodec<FriendlyByteBuf, S2CSwappedAtlasSlot> CODEC
            = StreamCodec.of(S2CSwappedAtlasSlot::encode, S2CSwappedAtlasSlot::decode);

        public static void send(ServerPlayer player, int slot) {
            ServerPlayNetworking.send(player, new S2CSwappedAtlasSlot(slot));
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        private static void encode(FriendlyByteBuf buf, S2CSwappedAtlasSlot self) {
            buf.writeInt(self.slot);
        }

        private static S2CSwappedAtlasSlot decode(FriendlyByteBuf buf) {
            return new S2CSwappedAtlasSlot(buf.readInt());
        }
    }

    // Server-to-client
    public record S2CUpdateInventory(int slot) implements CustomPacketPayload {
        static final String ID = "charm:update_atlas_inventory";
        static CustomPacketPayload.Type<S2CUpdateInventory> TYPE = CustomPacketPayload.createType(ID);
        static StreamCodec<FriendlyByteBuf, S2CUpdateInventory> CODEC = StreamCodec.of(S2CUpdateInventory::encode, S2CUpdateInventory::decode);

        public static void send(ServerPlayer player, int slot) {
            ServerPlayNetworking.send(player, new S2CUpdateInventory(slot));
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        private static void encode(FriendlyByteBuf buf, S2CUpdateInventory self) {
            if (self.slot >= 0) {
                buf.writeInt(self.slot);
            }
        }

        private static S2CUpdateInventory decode(FriendlyByteBuf buf) {
            return new S2CUpdateInventory(buf.readInt());
        }
    }

    // Client-to-server
    public record C2SSwapAtlasSlot(int slot) implements CustomPacketPayload {
        static final String ID = "charm:swap_atlas_slot";
        static CustomPacketPayload.Type<C2SSwapAtlasSlot> TYPE = CustomPacketPayload.createType(ID);
        static StreamCodec<FriendlyByteBuf, C2SSwapAtlasSlot> CODEC = StreamCodec.of(C2SSwapAtlasSlot::encode, C2SSwapAtlasSlot::decode);

        public static void send(int slot) {
            ClientPlayNetworking.send(new C2SSwapAtlasSlot(slot));
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        private static void encode(FriendlyByteBuf buf, C2SSwapAtlasSlot self) {
            buf.writeInt(self.slot);
        }

        private static C2SSwapAtlasSlot decode(FriendlyByteBuf buf) {
            return new C2SSwapAtlasSlot(buf.readInt());
        }
    }

    // Client-to-server
    public record C2STransferAtlas(int atlasSlot, int mapX, int mapZ, MoveMode moveMode) implements CustomPacketPayload {
        static final String ID = "charm:transfer_atlas";
        static CustomPacketPayload.Type<C2STransferAtlas> TYPE = CustomPacketPayload.createType(ID);
        static StreamCodec<FriendlyByteBuf, C2STransferAtlas> CODEC = StreamCodec.of(C2STransferAtlas::encode, C2STransferAtlas::decode);

        public static void send(int atlasSlot, int mapX, int mapZ, MoveMode moveMode) {
            ClientPlayNetworking.send(new C2STransferAtlas(atlasSlot, mapX, mapZ, moveMode));
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public static void encode(FriendlyByteBuf buf, C2STransferAtlas self) {
            buf.writeInt(self.atlasSlot);
            buf.writeInt(self.mapX);
            buf.writeInt(self.mapZ);
            buf.writeEnum(self.moveMode);
        }

        public static C2STransferAtlas decode(FriendlyByteBuf buf) {
            return new C2STransferAtlas(
                buf.readInt(),
                buf.readInt(),
                buf.readInt(),
                buf.readEnum(MoveMode.class)
            );
        }
    }
}
