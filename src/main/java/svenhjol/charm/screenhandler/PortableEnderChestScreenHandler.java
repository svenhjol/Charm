package svenhjol.charm.screenhandler;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import svenhjol.charm.module.Core;

public class PortableEnderChestScreenHandler extends GenericContainerScreenHandler {
    public PortableEnderChestScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ScreenHandlerType.GENERIC_9X3, syncId, playerInventory, inventory, 3);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);

        if (!player.world.isClient && Core.inventoryButtonReturn)
            ServerPlayNetworking.send((ServerPlayerEntity)player, Core.MSG_SERVER_OPEN_INVENTORY, new PacketByteBuf(Unpooled.buffer()));
    }
}
