package svenhjol.charm.feature.atlases;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ComplexItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Packet;
import svenhjol.charmony.base.Mods;
import svenhjol.charmony.enums.PacketDirection;
import svenhjol.charmony.iface.IClientNetwork;
import svenhjol.charmony.iface.IPacketRequest;
import svenhjol.charmony.iface.IServerNetwork;

public class AtlasesNetwork {
    public static void register() {
        var registry = Mods.common(Charm.ID).registry();
        registry.packet(new TransferAtlas(), () -> Atlases::handleTransferAtlas);
        registry.packet(new SwapAtlasSlot(), () -> Atlases::handleSwappedSlot);
        registry.packet(new SwappedAtlasSlot(), () -> AtlasesClient::handleSwappedSlot);
        registry.packet(new UpdateInventory(), () -> AtlasesClient::handleUpdateInventory);
    }

    public static IClientNetwork getClientNetwork() {
        return Mods.client(Charm.ID).network();
    }

    public static IServerNetwork getServerNetwork() {
        return Mods.common(Charm.ID).network();
    }

    public static void sendMapToClient(ServerPlayer player, ItemStack map, boolean markDirty) {
        if (map.getItem().isComplex()) {
            if (markDirty) {
                var mapId = MapItem.getMapId(map);
                var mapData = MapItem.getSavedData(mapId, player.level());

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
        id = "charm:swap_atlas_slot",
        direction = PacketDirection.CLIENT_TO_SERVER
    )
    public static class SwapAtlasSlot implements IPacketRequest {
        private int slot;

        private SwapAtlasSlot() {}

        public static void send(int slot) {
            var message = new SwapAtlasSlot();
            message.slot = slot;
            getClientNetwork().send(message);
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
            this.slot = buf.readInt();
        }
    }

    @Packet(
        id = "charm:swapped_atlas_slot",
        direction = PacketDirection.SERVER_TO_CLIENT
    )
    public static class SwappedAtlasSlot implements IPacketRequest {
        private int slot;

        private SwappedAtlasSlot() {}

        public static void send(Player player, int slot) {
            var message = new SwappedAtlasSlot();
            message.slot = slot;
            getServerNetwork().send(message, player);
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
            this.slot = buf.readInt();
        }
    }

    @Packet(
        id = "charm:transfer_atlas",
        direction = PacketDirection.CLIENT_TO_SERVER
    )
    public static class TransferAtlas implements IPacketRequest {
        private int atlasSlot;
        private int mapX;
        private int mapZ;
        private MoveMode moveMode;

        private TransferAtlas() {}

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

        public static void send(int atlasSlot, int mapX, int mapZ, MoveMode moveMode) {
            var message = new TransferAtlas();
            message.atlasSlot = atlasSlot;
            message.mapX = mapX;
            message.mapZ = mapZ;
            message.moveMode = moveMode;
            getClientNetwork().send(message);
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
            this.atlasSlot = buf.readInt();
            this.mapX = buf.readInt();
            this.mapZ = buf.readInt();
            this.moveMode = buf.readEnum(MoveMode.class);
        }
    }

    @Packet(
        id = "charm:update_atlas_inventory",
        direction = PacketDirection.SERVER_TO_CLIENT,
        description = "The slot that contains the active atlas is transferred to the client."
    )
    public static class UpdateInventory implements IPacketRequest {
        private int slot = Integer.MIN_VALUE;

        private UpdateInventory() {}

        public static void send(Player player, int slot) {
            var message = new UpdateInventory();
            message.slot = slot;
            getServerNetwork().send(message, player);
        }

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

        public int getSlot() {
            return slot;
        }
    }
}
