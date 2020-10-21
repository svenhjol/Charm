package svenhjol.charm.screenhandler;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.slot.Slot;
import svenhjol.charm.base.screenhandler.CharmScreenHandler;
import svenhjol.charm.blockentity.BookcaseBlockEntity;
import svenhjol.charm.module.Bookcases;

public class BookcaseScreenHandler extends CharmScreenHandler {
    public BookcaseScreenHandler(int syncId, PlayerInventory player) {
        this(syncId, player, new SimpleInventory(BookcaseBlockEntity.SIZE));
    }

    public BookcaseScreenHandler(int syncId, PlayerInventory player, Inventory inventory) {
        super(Bookcases.SCREEN_HANDLER, syncId, player, inventory);
        int index = 0;

        // container's inventory slots
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new BookcaseSlot(inventory, index++, 8 + (i * 18), 18));
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new BookcaseSlot(inventory, index++, 8 + (i * 18), 36));
        }

        index = 9; // start of player inventory

        // player's main inventory slots
        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 9; ++c) {
                this.addSlot(new Slot(player, index++, 8 + c * 18, 68 + r * 18));
            }
        }

        // player's hotbar
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(player, i, 8 + (i * 18), 126));
        }
    }
}
