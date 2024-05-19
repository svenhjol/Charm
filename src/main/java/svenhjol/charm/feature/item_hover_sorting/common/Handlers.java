package svenhjol.charm.feature.item_hover_sorting.common;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import svenhjol.charm.api.enums.SortDirection;
import svenhjol.charm.api.event.ItemHoverSortEvent;
import svenhjol.charm.feature.item_hover_sorting.ItemHoverSorting;
import svenhjol.charm.foundation.feature.FeatureHolder;
import svenhjol.charm.foundation.helper.ItemTidyingHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class Handlers extends FeatureHolder<ItemHoverSorting> {
    private final Registers registers;

    public Handlers(ItemHoverSorting feature) {
        super(feature);

        registers = feature().registers;
    }

    public void sortBundle(ServerPlayer player, ItemStack stack, SortDirection direction) {
        if (stack.is(Items.BUNDLE) && registers.sortables.contains(Items.BUNDLE)) {
            var bundleContents = stack.get(DataComponents.BUNDLE_CONTENTS);
            if (bundleContents == null) return;

            List<ItemStack> contents = new LinkedList<>();
            bundleContents.itemsCopy().forEach(contents::add);
            if (contents.isEmpty()) return;

            ItemHoverSortEvent.sortByScrollDirection(contents, direction);
            var newContents = new BundleContents(contents);

            stack.set(DataComponents.BUNDLE_CONTENTS, newContents);
            feature().advancements.sortedItemsWhileHovering(player);
        }
    }

    public void sortShulkerBox(ServerPlayer player, ItemStack stack, SortDirection direction) {
        if (Block.byItem(stack.getItem()) instanceof ShulkerBoxBlock block && registers.sortables.contains(block)) {
            var data = stack.get(DataComponents.CONTAINER);

            if (data != null) {
                // Merge and sort.
                var stacks = new ArrayList<>(data.stream().toList());
                ItemTidyingHelper.mergeStacks(stacks);
                ItemHoverSortEvent.sortByScrollDirection(stacks, direction);

                // Write contents back to shulker box.
                var itemContainerContents = ItemContainerContents.fromItems(stacks);
                stack.set(DataComponents.CONTAINER, itemContainerContents);

                feature().advancements.sortedItemsWhileHovering(player);
            }
        }
    }

    /**
     * Network callback from the client scroll event.
     * @see Networking.C2SScrollOnHover
     */
    public void scrollOnHover(Player player, Networking.C2SScrollOnHover packet) {
        var container = player.containerMenu;
        var slotIndex = packet.slotIndex();
        var direction = packet.sortDirection();

        // Check that the slot index is not out of bounds.
        if (slotIndex >= container.slots.size() || slotIndex < 0) return;

        var itemInSlot = container.getSlot(slotIndex).getItem();

        // Invoke the hover sort event on the hovered item.
        ItemHoverSortEvent.INSTANCE.invoke((ServerPlayer)player, itemInSlot, direction);
    }
}
