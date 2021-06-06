package svenhjol.charm.screen;

import java.util.function.Predicate;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.screen.CharmScreenHandler;

public class CharmInventoryScreenHandler extends CharmScreenHandler {

    public CharmInventoryScreenHandler(int rows, Predicate<ItemStack> condition, MenuType<? extends CharmInventoryScreenHandler> type, int syncId,
                                       Inventory player, Container inventory) {
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

        public ConditionalSlot(Predicate<ItemStack> condition, Container inventoryIn, int index, int xPosition, int yPosition) {
            super(inventoryIn, index, xPosition, yPosition);
            this.condition = condition;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return condition.test(stack);
        }
    }
}
