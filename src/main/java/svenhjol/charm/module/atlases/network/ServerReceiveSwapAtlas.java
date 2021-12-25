package svenhjol.charm.module.atlases.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.module.atlases.AtlasItem;
import svenhjol.charm.module.atlases.Atlases;
import svenhjol.charm.network.Id;
import svenhjol.charm.network.ServerReceiver;

import java.util.function.Consumer;

@Id("strange:swap_atlas")
public class ServerReceiveSwapAtlas extends ServerReceiver {
    @Override
    public void handle(MinecraftServer server, ServerPlayer player, FriendlyByteBuf buffer) {
        if (player == null) return;
        int swappedSlot = buffer.readInt();

        server.execute(() -> {
            ItemStack offhandItem = player.getOffhandItem().copy();
            ItemStack mainHandItem = player.getMainHandItem().copy();
            Inventory inventory = player.getInventory();

            Consumer<Integer> doSwap = i -> {
                ItemStack swap = inventory.getItem(i).copy();
                inventory.setItem(i, mainHandItem);
                player.setItemInHand(InteractionHand.MAIN_HAND, swap);
                Atlases.SERVER_SEND_SWAPPED_SLOT.send(player, i);
            };

            if (mainHandItem.getItem() instanceof AtlasItem) {
                if (swappedSlot >= 0) {
                    doSwap.accept(swappedSlot);
                    return;
                }
            }

            if (offhandItem.getItem() instanceof AtlasItem) return;

            int slot = -1;
            for (int i = 0; i < 36; i++) {
                ItemStack inv = inventory.getItem(i);
                if (inv.getItem() instanceof AtlasItem) {
                    slot = i;
                    break;
                }
            }

            if (slot == -1) return;
            doSwap.accept(slot);
        });
    }
}
