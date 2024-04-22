package svenhjol.charm.api.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@SuppressWarnings("unused")
public class ItemHoverSortEvent extends CharmEvent<ItemHoverSortEvent.Handler> {
    public static final ItemHoverSortEvent INSTANCE = new ItemHoverSortEvent();

    private ItemHoverSortEvent() {}

    public void invoke(ServerPlayer player, ItemStack stack, SortDirection direction) {
        for (var handler : getHandlers()) {
            handler.run(player, stack, direction);
        }
    }

    public static void sortByScrollDirection(List<ItemStack> contents, SortDirection direction) {
        if (contents.isEmpty()) return;

        switch (direction) {
            case UP -> {
                ItemStack last = contents.remove(contents.size() - 1);
                contents.add(0, last);
            }
            case DOWN -> {
                ItemStack first = contents.remove(0);
                contents.add(first);
            }
        }
    }

    @FunctionalInterface
    public interface Handler {
        void run(ServerPlayer player, ItemStack stack, SortDirection direction);
    }

    public enum SortDirection {
        UP,
        DOWN
    }
}
