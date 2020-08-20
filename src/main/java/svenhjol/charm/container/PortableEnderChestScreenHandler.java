package svenhjol.charm.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class PortableEnderChestScreenHandler extends GenericContainerScreenHandler {
    public PortableEnderChestScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ScreenHandlerType.GENERIC_9X3, syncId, playerInventory, inventory, 3);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true; // any instances where player can't use an Ender Chest?
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);

        if (!player.world.isClient) {
            // TODO open inventory
        }
    }
}
