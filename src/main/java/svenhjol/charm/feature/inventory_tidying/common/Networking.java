package svenhjol.charm.feature.inventory_tidying.common;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import svenhjol.charm.feature.inventory_tidying.InventoryTidying;
import svenhjol.charm.foundation.feature.FeatureHolder;

public final class Networking extends FeatureHolder<InventoryTidying> {
    public Networking(InventoryTidying feature) {
        super(feature);
    }

    // Client-to-server packet to tidy the inventory or the viewed container.
    public record C2STidyInventory(TidyType tidyType) implements CustomPacketPayload {
        static final String ID = "charm:tidy_inventory";
        static Type<C2STidyInventory> TYPE = CustomPacketPayload.createType(ID);
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
