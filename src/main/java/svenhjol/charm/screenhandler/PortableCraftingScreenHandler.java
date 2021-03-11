package svenhjol.charm.screenhandler;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import svenhjol.charm.module.Core;

public class PortableCraftingScreenHandler extends CraftingScreenHandler {
    public PortableCraftingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(syncId, playerInventory, context);
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
