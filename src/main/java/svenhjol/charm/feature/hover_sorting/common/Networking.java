package svenhjol.charm.feature.hover_sorting.common;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import svenhjol.charm.api.enums.SortDirection;
import svenhjol.charm.feature.hover_sorting.HoverSorting;
import svenhjol.charm.foundation.feature.FeatureHolder;

public final class Networking extends FeatureHolder<HoverSorting> {
    public Networking(HoverSorting feature) {
        super(feature);
    }

    // Client-to-server packet to set the slot index and direction of scroll.
    public record C2SScrollOnHover(int slotIndex, SortDirection sortDirection) implements CustomPacketPayload {
        static final String ID = "charm:hover_sorting_scroll_on_item";
        static Type<C2SScrollOnHover> TYPE = CustomPacketPayload.createType(ID);
        static StreamCodec<FriendlyByteBuf, C2SScrollOnHover> CODEC
            = StreamCodec.of(C2SScrollOnHover::encode, C2SScrollOnHover::decode);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        private static void encode(FriendlyByteBuf buf, C2SScrollOnHover self) {
            buf.writeInt(self.slotIndex);
            buf.writeEnum(self.sortDirection);
        }

        private static C2SScrollOnHover decode(FriendlyByteBuf buf) {
            return new C2SScrollOnHover(buf.readInt(), buf.readEnum(SortDirection.class));
        }

        public static void send(int slotIndex, SortDirection sortDirection) {
            ClientPlayNetworking.send(new C2SScrollOnHover(slotIndex, sortDirection));
        }
    }
}
