package svenhjol.charm.module.portable_crafting.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.module.portable_crafting.PortableCrafting;
import svenhjol.charm.network.Id;
import svenhjol.charm.network.ServerReceiver;

@Id("charm:open_crafting")
public class ServerReceiveOpenCrafting extends ServerReceiver {
    @Override
    public void handle(MinecraftServer server, ServerPlayer player, FriendlyByteBuf buffer) {
        server.execute(() -> {
            if (player == null || !player.inventory.contains(new ItemStack(Blocks.CRAFTING_TABLE))) return;
            PortableCrafting.triggerUsedCraftingTable(player);
            PortableCrafting.openContainer(player);
        });
    }
}
