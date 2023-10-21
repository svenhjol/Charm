package svenhjol.charm.feature.inventory_tidying;

import net.minecraft.network.FriendlyByteBuf;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Packet;
import svenhjol.charmony.base.Mods;
import svenhjol.charmony.enums.PacketDirection;
import svenhjol.charmony.iface.IPacketRequest;

public class InventoryTidyingNetwork {
    public static void register() {
        Mods.common(Charm.ID).registry().packet(new TidyInventory(), () -> InventoryTidying::handleTidyInventory);
    }

    @Packet(
        id = "charm:tidy_inventory",
        direction = PacketDirection.CLIENT_TO_SERVER,
        description = "A packet sent from the client to instruct the server to tidy the inventory or the viewed container."
    )
    public static class TidyInventory implements IPacketRequest {
        private TidyType type;

        private TidyInventory() {}

        public static void send(TidyType type) {
            var message = new TidyInventory();
            message.type = type;
            Mods.client(Charm.ID).network().send(message);
        }

        public TidyType getType() {
            return type;
        }

        @Override
        public void encode(FriendlyByteBuf buf) {
            buf.writeEnum(type);
        }

        @Override
        public void decode(FriendlyByteBuf buf) {
            this.type = buf.readEnum(TidyType.class);
        }
    }
}
