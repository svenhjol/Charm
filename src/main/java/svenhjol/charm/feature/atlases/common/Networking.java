package svenhjol.charm.feature.atlases.common;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ComplexItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import svenhjol.charm.charmony.annotation.Packet;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.charmony.iface.PacketRequest;
import svenhjol.charm.feature.atlases.Atlases;

public final class Networking extends FeatureHolder<Atlases> {
    public Networking(Atlases feature) {
        super(feature);
    }

    public void sendMapToClient(ServerPlayer player, ItemStack map, boolean markDirty) {
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

    @Packet(
            id = "charm:swapped_atlas_slot"
    )
    public static class S2CSwappedAtlasSlot implements PacketRequest {
        private int slot;

        public S2CSwappedAtlasSlot() {this(0);}

        public S2CSwappedAtlasSlot(int slot) {this.slot = slot;}

        public static void send(ServerPlayer player, int slot) {
            var message = new S2CSwappedAtlasSlot(slot);
            var buffer = new FriendlyByteBuf(Unpooled.buffer());
            message.encode(buffer);
            ServerPlayNetworking.send(player, message.id(), buffer);
        }

        public int getSlot() {return slot;}

        @Override
        public void encode(FriendlyByteBuf buf) {
            buf.writeInt(slot);
        }

        @Override
        public void decode(FriendlyByteBuf buf) {
            slot = buf.readInt();
        }
    }

    @Packet(
            id = "charm:update_atlas_inventory"
    )
    public static class S2CUpdateInventory implements PacketRequest {
        private int slot;

        public S2CUpdateInventory() {this(0);}

        public S2CUpdateInventory(int slot) {this.slot = slot;}

        public static void send(ServerPlayer player, int slot) {
            var message = new S2CUpdateInventory(slot);
            var buffer = new FriendlyByteBuf(Unpooled.buffer());
            message.encode(buffer);
            ServerPlayNetworking.send(player, message.id(), buffer);
        }

        public int getSlot() {return slot;}

        @Override
        public void encode(FriendlyByteBuf buf) {
            if (slot >= 0) {
                buf.writeInt(slot);
            }
        }

        @Override
        public void decode(FriendlyByteBuf buf) {
            slot = buf.readInt();
        }
    }

    @Packet(
            id = "swap_atlas_slot"
    )
    public static class C2SSwapAtlasSlot implements PacketRequest {
        private int slot;

        public C2SSwapAtlasSlot() {this(0);}

        public C2SSwapAtlasSlot(int slot) {this.slot = slot;}

        public static void send(int slot) {
            var message = new C2SSwapAtlasSlot(slot);
            var buffer = new FriendlyByteBuf(Unpooled.buffer());
            message.encode(buffer);
            ClientPlayNetworking.send(message.id(), buffer);
        }

        public int getSlot() {
            return slot;
        }

        @Override
        public void encode(FriendlyByteBuf buf) {
            buf.writeInt(slot);
        }

        @Override
        public void decode(FriendlyByteBuf buf) {
            slot = buf.readInt();
        }
    }

    @Packet(
            id = "charm:transfer_atlas"
    )
    public static class C2STransferAtlas implements PacketRequest {
        private int atlasSlot;
        private int mapX;
        private int mapZ;
        private MoveMode moveMode;

        public C2STransferAtlas() {this(0, 0, 0, MoveMode.TO_HAND);}

        public C2STransferAtlas(int atlasSlot, int mapX, int mapZ, MoveMode moveMode) {
            this.atlasSlot = atlasSlot;
            this.mapX = mapX;
            this.mapZ = mapZ;
            this.moveMode = moveMode;
        }

        public static void send(int atlasSlot, int mapX, int mapZ, MoveMode moveMode) {
            var message = new C2STransferAtlas(atlasSlot, mapX, mapZ, moveMode);
            var buffer = new FriendlyByteBuf(Unpooled.buffer());
            message.encode(buffer);
            ClientPlayNetworking.send(message.id(), buffer);
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

        @Override
        public void encode(FriendlyByteBuf buf) {
            buf.writeInt(atlasSlot);
            buf.writeInt(mapX);
            buf.writeInt(mapZ);
            buf.writeEnum(moveMode);
        }

        @Override
        public void decode(FriendlyByteBuf buf) {
            atlasSlot = buf.readInt();
            mapX = buf.readInt();
            mapZ = buf.readInt();
            moveMode = buf.readEnum(MoveMode.class);
        }
    }
}
