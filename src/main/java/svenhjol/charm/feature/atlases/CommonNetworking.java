package svenhjol.charm.feature.atlases;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ComplexItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import svenhjol.charm.foundation.Networking;

public class CommonNetworking extends Networking<Atlases> {
    public CommonNetworking(Atlases feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();

        // Server senders
        registry.serverPacketSender(SwappedAtlasSlot.TYPE, SwappedAtlasSlot.CODEC);
        registry.serverPacketSender(UpdateInventory.TYPE, UpdateInventory.CODEC);

        // Client senders
        registry.clientPacketSender(ClientNetworking.SwapAtlasSlot.TYPE, ClientNetworking.SwapAtlasSlot.CODEC);
        registry.clientPacketSender(ClientNetworking.TransferAtlas.TYPE, ClientNetworking.TransferAtlas.CODEC);

        // Server receivers
        registry.packetReceiver(ClientNetworking.SwapAtlasSlot.TYPE,
            Atlases::handleSwappedSlot);
        registry.packetReceiver(ClientNetworking.TransferAtlas.TYPE,
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
    public record SwappedAtlasSlot(int slot) implements CustomPacketPayload {
        static CustomPacketPayload.Type<SwappedAtlasSlot> TYPE = CustomPacketPayload.createType("charm:swapped_atlas_slot");
        static StreamCodec<FriendlyByteBuf, SwappedAtlasSlot> CODEC = StreamCodec.of(SwappedAtlasSlot::encode, SwappedAtlasSlot::decode);

        public static void send(ServerPlayer player, int slot) {
            ServerPlayNetworking.send(player, new SwappedAtlasSlot(slot));
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public int getSlot() {
            return slot;
        }

        private static void encode(FriendlyByteBuf buf, SwappedAtlasSlot self) {
            buf.writeInt(self.slot);
        }

        private static SwappedAtlasSlot decode(FriendlyByteBuf buf) {
            return new SwappedAtlasSlot(buf.readInt());
        }
    }

    // Server-to-client
    public record UpdateInventory(int slot) implements CustomPacketPayload {
        static CustomPacketPayload.Type<UpdateInventory> TYPE = CustomPacketPayload.createType("charm:update_atlas_inventory");
        static StreamCodec<FriendlyByteBuf, UpdateInventory> CODEC = StreamCodec.of(UpdateInventory::encode, UpdateInventory::decode);

        public static void send(ServerPlayer player, int slot) {
            ServerPlayNetworking.send(player, new UpdateInventory(slot));
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        public int getSlot() {
            return slot;
        }

        private static void encode(FriendlyByteBuf buf, UpdateInventory self) {
            if (self.slot >= 0) {
                buf.writeInt(self.slot);
            }
        }

        private static UpdateInventory decode(FriendlyByteBuf buf) {
            return new UpdateInventory(buf.readInt());
        }
    }
}
