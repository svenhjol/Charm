package svenhjol.charm.feature.inventory_tidying;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import svenhjol.charm.foundation.feature.Network;

public final class CommonNetworking extends Network<InventoryTidying> {
    public CommonNetworking(InventoryTidying feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();

        // Client senders
        registry.clientPacketSender(C2STidyInventory.TYPE, C2STidyInventory.CODEC);

        // Server receivers
        registry.packetReceiver(C2STidyInventory.TYPE,
            InventoryTidying::handleTidyInventory);
    }

    // Client-to-server packet to tidy the inventory or the viewed container.
    public record C2STidyInventory(TidyType tidyType) implements CustomPacketPayload {
        static final String ID = "charm:tidy_inventory";
        static CustomPacketPayload.Type<C2STidyInventory> TYPE
            = CustomPacketPayload.createType(ID);
        static StreamCodec<FriendlyByteBuf, C2STidyInventory> CODEC
            = StreamCodec.of(C2STidyInventory::encode, C2STidyInventory::decode);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        private static C2STidyInventory decode(FriendlyByteBuf buf) {
            return new C2STidyInventory(buf.readEnum(TidyType.class));
        }

        private static void encode(FriendlyByteBuf buf, C2STidyInventory self) {
            buf.writeEnum(self.tidyType);
        }

        public static void send(TidyType tidyType)  {
            ClientPlayNetworking.send(new C2STidyInventory(tidyType));
        }
    }
}
