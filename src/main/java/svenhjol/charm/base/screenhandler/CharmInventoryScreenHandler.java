package svenhjol.charm.base.screenhandler;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;

import java.util.function.Predicate;

public class CharmInventoryScreenHandler extends CharmScreenHandler {

    public CharmInventoryScreenHandler(int rows, Predicate<ItemStack> condition, ScreenHandlerType<? extends CharmInventoryScreenHandler> type, int syncId,
                                       PlayerInventory player, Inventory inventory) {
        super(type, syncId, player, inventory);

        // container's inventory slots
        for (int r = 0; r < rows; ++r) {
            for (int c = 0; c < 9; ++c) {
                this.addSlot(new ConditionalSlot(condition, inventory, r * 9 + c, 8 + c * 18, 18 + r * 18));
            }
        }

        // player's main inventory slots
        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 9; ++c) {
                this.addSlot(new Slot(player, 9 + r * 9 + c, 8 + c * 18, 32 + (rows + r) * 18));
            }
        }

        // player's hotbar
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(player, i, 8 + i * 18, 90 + rows * 18));
        }
    }

    private static class ConditionalSlot extends Slot {
        private final Predicate<ItemStack> condition;

        public ConditionalSlot(Predicate<ItemStack> condition, Inventory inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
            this.condition = condition;
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return condition.test(stack);
        }
    }
}
