package svenhjol.charm.module.bundle_sorting.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface SortBundleItemsCallback {
    Event<SortBundleItemsCallback> EVENT = EventFactory.createArrayBacked(SortBundleItemsCallback.class, (listeners) -> (player, stack, direction) -> {
        for (SortBundleItemsCallback listener : listeners) {
            listener.interact(player, stack, direction);
        }
    });

    void interact(ServerPlayer player, ItemStack stack, boolean direction);

    static void sortByScrollDirection(List<ItemStack> contents, boolean direction) {
        if (direction) {
            ItemStack last = contents.remove(contents.size() - 1);
            contents.add(0, last);
        } else {
            ItemStack first = contents.remove(0);
            contents.add(first);
        }
    }
}
