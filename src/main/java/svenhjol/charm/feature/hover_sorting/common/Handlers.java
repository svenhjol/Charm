package svenhjol.charm.feature.hover_sorting.common;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import svenhjol.charm.api.enums.SortDirection;
import svenhjol.charm.api.event.ItemHoverSortEvent;
import svenhjol.charm.feature.hover_sorting.HoverSorting;
import svenhjol.charm.foundation.feature.FeatureHolder;
import svenhjol.charm.foundation.helper.InventoryTidyingHelper;
import svenhjol.charm.foundation.helper.TagHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class Handlers extends FeatureHolder<HoverSorting> {
    private final Registers registers;

    public Handlers(HoverSorting feature) {
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
            feature().advancements.triggerSortedItems(player);
        }
    }

    public void sortShulkerBox(ServerPlayer player, ItemStack stack, SortDirection direction) {
        if (Block.byItem(stack.getItem()) instanceof ShulkerBoxBlock block && registers.sortables.contains(block)) {
            var data = stack.get(DataComponents.CONTAINER);

            if (data != null) {
                // Merge and sort.
                var stacks = new ArrayList<>(data.stream().toList());
                InventoryTidyingHelper.mergeStacks(stacks);
                ItemHoverSortEvent.sortByScrollDirection(stacks, direction);

                // Write contents back to shulker box.
                var itemContainerContents = ItemContainerContents.fromItems(stacks);
                stack.set(DataComponents.CONTAINER, itemContainerContents);

                feature().advancements.triggerSortedItems(player);
            }
        }
    }

    public void levelLoad(MinecraftServer server, ServerLevel level) {
        if (level.dimension() == Level.OVERWORLD) {
            var registryAccess = level.registryAccess();
            List<ItemLike> holder = new ArrayList<>();

            registers.cachedBlockTags.forEach(
                blockTagKey -> holder.addAll(TagHelper.getValues(registryAccess
                    .registryOrThrow(blockTagKey.registry()), blockTagKey)));

            registers.cachedItemTags.forEach(
                itemTagKey -> holder.addAll(TagHelper.getValues(registryAccess
                    .registryOrThrow(itemTagKey.registry()), itemTagKey)));

            holder.addAll(registers.cachedSortables);

            // Clear all sortables and add collected items back to it.
            registers.sortables.clear();
            holder.forEach(item -> {
                if (!registers.sortables.contains(item)) {
                    registers.sortables.add(item);
                }
            });
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
