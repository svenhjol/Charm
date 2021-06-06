package svenhjol.charm.module.bookcases;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.module.bookcases.BookcaseBlockEntity;
import svenhjol.charm.module.bookcases.BookcaseSlot;
import svenhjol.charm.module.bookcases.Bookcases;
import svenhjol.charm.screen.CharmScreenHandler;

public class BookcaseScreenHandler extends CharmScreenHandler {
    public BookcaseScreenHandler(int syncId, Inventory player) {
        this(syncId, player, new SimpleContainer(svenhjol.charm.module.bookcases.BookcaseBlockEntity.SIZE));
    }

    public BookcaseScreenHandler(int syncId, Inventory player, Container inventory) {
        super(svenhjol.charm.module.bookcases.Bookcases.SCREEN_HANDLER, syncId, player, inventory);
        int index = 0;

        // container's inventory slots
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new svenhjol.charm.module.bookcases.BookcaseSlot(inventory, index++, 8 + (i * 18), 18));
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

    @Override
    public void clicked(int slotIndex, int button, ClickType actionType, Player player) {
        if (slotIndex > 0 && slotIndex < BookcaseBlockEntity.SIZE) {
            ItemStack stack = this.getCarried();
            if (!player.level.isClientSide && svenhjol.charm.module.bookcases.Bookcases.canContainItem(stack))
                Bookcases.triggerAddedBookToBookcase((ServerPlayer) player);
        }
        super.clicked(slotIndex, button, actionType, player);
    }
}
