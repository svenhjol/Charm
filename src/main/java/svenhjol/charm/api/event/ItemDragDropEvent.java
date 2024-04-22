package svenhjol.charm.api.event;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class ItemDragDropEvent extends CharmEvent<ItemDragDropEvent.Handler> {
    public static final ItemDragDropEvent INSTANCE = new ItemDragDropEvent();

    private ItemDragDropEvent() {}

    public InteractionResult invoke(StackType stackType, ItemStack source, ItemStack dest, Slot slot, ClickAction clickAction, Player player, @Nullable SlotAccess slotAccess) {
        for (var handler : getHandlers()) {
            var result = handler.run(stackType, source, dest, slot, clickAction, player, slotAccess);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }

        return InteractionResult.PASS;
    }

    @FunctionalInterface
    public interface Handler {
        InteractionResult run(StackType stackType, ItemStack source, ItemStack dest, Slot slot, ClickAction clickAction, Player player, @Nullable SlotAccess slotAccess);
    }

    public enum StackType {
        STACKED_ON_OTHER,
        STACKED_ON_SELF
    }
}
