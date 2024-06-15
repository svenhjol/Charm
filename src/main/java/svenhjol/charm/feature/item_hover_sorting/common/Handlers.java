package svenhjol.charm.feature.item_hover_sorting.common;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import svenhjol.charm.charmony.common.helper.ItemTidyingHelper;
import svenhjol.charm.charmony.event.ItemHoverSortEvent;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.item_hover_sorting.ItemHoverSorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.Collectors;

public final class Handlers extends FeatureHolder<ItemHoverSorting> {
    private final Registers registers;

    public Handlers(ItemHoverSorting feature) {
        super(feature);

        registers = feature().registers;
    }

    public void sortBundle(ServerPlayer player, ItemStack stack, ItemHoverSortEvent.SortDirection direction) {
        if (stack.is(Items.BUNDLE) && registers.sortables.contains(Items.BUNDLE)) {
            var itemsTag = BundleItem.TAG_ITEMS;
            var bundleContents = BundleItem.getContents(stack)
                    .collect(Collectors.toCollection(LinkedList::new));

            if (bundleContents.isEmpty()) {
                return;
            }

            ItemHoverSortEvent.sortByScrollDirection(bundleContents, direction);
            stack.removeTagKey(itemsTag);

            var tag = stack.getOrCreateTag();
            tag.put(itemsTag, new ListTag());
            var list = tag.getList(itemsTag, 10);

            Collections.reverse(bundleContents);
            bundleContents.forEach(itemStack -> {
                var t = new CompoundTag();
                itemStack.save(t);
                list.add(0, t);
            });
        }
    }

    public void sortShulkerBox(ServerPlayer player, ItemStack stack, ItemHoverSortEvent.SortDirection direction) {
        if (Block.byItem(stack.getItem()) instanceof ShulkerBoxBlock block && registers.sortables.contains(block)) {
            var tag = BlockItem.getBlockEntityData(stack);
            BlockEntity blockEntity;

            if (tag == null) {
                // Generate a new empty block entity.
                blockEntity = block.newBlockEntity(BlockPos.ZERO, block.defaultBlockState());
            } else {
                // Instantiate existing shulkerbox block entity from its tag.
                blockEntity = BlockEntity.loadStatic(BlockPos.ZERO, block.defaultBlockState(), tag);
            }

            if (blockEntity instanceof ShulkerBoxBlockEntity shulkerBox) {
                var itemStacks = shulkerBox.itemStacks;
                if (itemStacks.isEmpty()) return;

                // Merge and sort.
                var stacks = new ArrayList<>(itemStacks);
                ItemTidyingHelper.mergeStacks(stacks);
                ItemHoverSortEvent.sortByScrollDirection(stacks, direction);

                // Write contents back to shulker box.
                NonNullList<ItemStack> list = NonNullList.create();
                list.addAll(stacks);
                shulkerBox.itemStacks = list;
                shulkerBox.saveToItem(stack);

                feature().advancements.sortedItemsWhileHovering(player);
            }
        }
    }

    /**
     * Network callback from the client scroll event.
     * @see Networking.ScrollOnHover
     */
    public void scrollOnHover(Networking.ScrollOnHover packet, Player player) {
        var container = player.containerMenu;
        var slotIndex = packet.getSlotIndex();
        var direction = packet.getSortDirection();

        // Check that the slot index is not out of bounds.
        if (slotIndex >= container.slots.size() || slotIndex < 0) return;

        var itemInSlot = container.getSlot(slotIndex).getItem();

        // Invoke the hover sort event on the hovered item.
        ItemHoverSortEvent.INSTANCE.invoke((ServerPlayer)player, itemInSlot, direction);
    }
}
