package svenhjol.charm.module.hover_sorting.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.api.event.HoverSortItemsCallback;
import svenhjol.charm.network.Id;
import svenhjol.charm.network.ServerReceiver;

@Id("charm:scrolled_on_hover")
public class ServerReceiveScrolledOnHover extends ServerReceiver {
    @Override
    public void handle(MinecraftServer server, ServerPlayer player, FriendlyByteBuf buffer) {
        int slotIndex = buffer.readInt();
        boolean direction = buffer.readBoolean();

        server.execute(() -> {
            ItemStack itemInSlot = player.containerMenu.getSlot(slotIndex).getItem();
            HoverSortItemsCallback.EVENT.invoker().interact(player, itemInSlot, direction);
        });
    }

    @Override
    protected boolean showDebugMessages() {
        return false;
    }
}
