package svenhjol.charm.decoration.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import svenhjol.charm.decoration.module.BookshelfChests;
import svenhjol.charm.decoration.tileentity.BookshelfChestTileEntity;
import svenhjol.meson.MesonContainer;

public class BookshelfChestContainer extends MesonContainer {
    private BookshelfChestContainer(ContainerType<?> type, int id, PlayerInventory player, IInventory inventory) {
        super(type, id, player, inventory);

        int index = 0;

        // container's inventory slots
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new BookshelfChestSlot(inventory, index++, 8 + (i * 18), 18));
        }

        index = 9;

        // player's main inventory slots
        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 9; ++c) {
                this.addSlot(new Slot(player, index++, 8 + c * 18, 50 + r * 18));
            }
        }

        // player's hotbar
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(player, i, 8 + (i * 18), 108));
        }
    }

    public static BookshelfChestContainer instance(int id, PlayerInventory playerInventory, IInventory inventory) {
        return new BookshelfChestContainer(BookshelfChests.container, id, playerInventory, inventory);
    }

    public static BookshelfChestContainer instance(int id, PlayerInventory playerInventory) {
        return instance(id, playerInventory, new Inventory(BookshelfChestTileEntity.SIZE));
    }
}
