package svenhjol.charm.feature.item_hover_sorting.common;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import svenhjol.charm.charmony.annotation.Packet;
import svenhjol.charm.charmony.event.ItemHoverSortEvent;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.charmony.iface.PacketRequest;
import svenhjol.charm.feature.item_hover_sorting.ItemHoverSorting;

public final class Networking extends FeatureHolder<ItemHoverSorting> {
    public Networking(ItemHoverSorting feature) {
        super(feature);
    }

    @Packet(
            id = "charm:item_hover_sorting_scroll",
            description = "Send the slot index and direction of scroll to the server."
    )
    public static class C2SScrollOnHover implements PacketRequest {
        private int slotIndex;
        private ItemHoverSortEvent.SortDirection sortDirection;

        public C2SScrollOnHover() {this(0, ItemHoverSortEvent.SortDirection.DOWN);}

        public C2SScrollOnHover(int slotIndex, ItemHoverSortEvent.SortDirection sortDirection) {
            this.slotIndex = slotIndex;
            this.sortDirection = sortDirection;
        }

        public static void send(int slotIndex, ItemHoverSortEvent.SortDirection sortDirection) {
            var message = new C2SScrollOnHover(slotIndex, sortDirection);
            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
            message.encode(buffer);
            ClientPlayNetworking.send(message.id(), buffer);
        }

        @Override
        public void encode(FriendlyByteBuf buf) {
            buf.writeInt(slotIndex);
            buf.writeEnum(sortDirection);
        }

        @Override
        public void decode(FriendlyByteBuf buf) {
            slotIndex = buf.readInt();
            sortDirection = buf.readEnum(ItemHoverSortEvent.SortDirection.class);
        }

        public int getSlotIndex() {
            return slotIndex;
        }

        public ItemHoverSortEvent.SortDirection getSortDirection() {
            return sortDirection;
        }
    }
}
