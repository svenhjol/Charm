package svenhjol.charm.module.hover_sorting;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.api.event.HoverSortItemsCallback;
import svenhjol.charm.module.hover_sorting.network.ServerReceiveScrolledOnHover;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@CommonModule(mod = Charm.MOD_ID, description = "Scroll the mouse while hovering over a bundle or shulker box to change the order of its contents.")
public class HoverSorting extends CharmModule {
    public static ServerReceiveScrolledOnHover SERVER_RECEIVE_SCROLLED_ON_HOVER;

    // add items that should allow hover sorting to this list
    public static final List<ItemLike> SORTABLE = new ArrayList<>();

    @Override
    public void register() {
        SORTABLE.add(Items.BUNDLE);
    }

    @Override
    public void runWhenEnabled() {
        SERVER_RECEIVE_SCROLLED_ON_HOVER = new ServerReceiveScrolledOnHover();
        HoverSortItemsCallback.EVENT.register(this::handleSortItems);
    }

    private void handleSortItems(ServerPlayer player, ItemStack stack, boolean direction) {
        String TAG_ITEMS = "Items"; // must match tag of BundleItem

        if (stack.getItem() instanceof BundleItem) {
            List<ItemStack> contents = BundleItem.getContents(stack).collect(Collectors.toCollection(LinkedList::new));
            if (contents.size() < 1) return;

            HoverSortItemsCallback.sortByScrollDirection(contents, direction);
            stack.removeTagKey(TAG_ITEMS);

            CompoundTag tag = stack.getOrCreateTag();
            tag.put(TAG_ITEMS, new ListTag());
            ListTag list = tag.getList(TAG_ITEMS, 10);

            Collections.reverse(contents);
            contents.forEach(s -> {
                CompoundTag stackNbt = new CompoundTag();
                s.save(stackNbt);
                list.add(0, stackNbt);
            });
        }
    }
}
