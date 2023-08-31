package svenhjol.charm.feature.hover_sorting;

import net.minecraft.network.FriendlyByteBuf;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.Packet;
import svenhjol.charm_api.event.ItemHoverSortEvent;
import svenhjol.charm_core.enums.PacketDirection;
import svenhjol.charm_core.iface.IPacketRequest;

public class HoverSortingNetwork {
    public static void register() {
        Charm.instance().registry().packet(new ScrollOnHover(), () -> HoverSorting::handleScrollOnHover);
    }

    @Packet(
        id = "charm:hover_sorting_scroll_on_item",
        direction = PacketDirection.CLIENT_TO_SERVER,
        description = "Send the slot index and direction of scroll to the server."
    )
    public static class ScrollOnHover implements IPacketRequest {
        private int slotIndex;
        private ItemHoverSortEvent.SortDirection sortDirection;

        private ScrollOnHover() {}

        public static void send(int slotIndex, ItemHoverSortEvent.SortDirection sortDirection) {
            var message = new ScrollOnHover();
            message.slotIndex = slotIndex;
            message.sortDirection = sortDirection;
            CharmClient.instance().network().send(message);
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
