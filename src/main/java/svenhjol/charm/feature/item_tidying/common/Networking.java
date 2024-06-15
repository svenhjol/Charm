package svenhjol.charm.feature.item_tidying.common;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import svenhjol.charm.charmony.annotation.Packet;
import svenhjol.charm.charmony.iface.PacketRequest;
import svenhjol.charm.feature.item_tidying.ItemTidying;
import svenhjol.charm.charmony.feature.FeatureHolder;

public final class Networking extends FeatureHolder<ItemTidying> {
    public Networking(ItemTidying feature) {
        super(feature);
    }

    @Packet(
            id = "charm:tidy_inventory",
            description = "A packet sent from the client to instruct the server to tidy the inventory or the viewed container."
    )
    public static class TidyInventory implements PacketRequest {
        private TidyType type;

        public TidyInventory() {}

        public static void send(TidyType type) {
            var message = new TidyInventory();
            message.type = type;
            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
            message.encode(buffer);
            ClientPlayNetworking.send(message.id(), buffer);
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
