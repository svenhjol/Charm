package svenhjol.charm.feature.shulker_box_drag_drop.common;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import svenhjol.charm.api.event.ItemDragDropEvent;
import svenhjol.charm.feature.shulker_box_drag_drop.ShulkerBoxDragDrop;
import svenhjol.charm.foundation.feature.FeatureHolder;
import svenhjol.charm.foundation.helper.InventoryTidyingHelper;

public final class Handlers extends FeatureHolder<ShulkerBoxDragDrop> {

    public Handlers(ShulkerBoxDragDrop feature) {
        super(feature);
    }

    public InteractionResult itemDragDrop(ItemDragDropEvent.StackType direction, ItemStack source, ItemStack dest, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        var registers = feature().registers;

        if (clickAction != ClickAction.SECONDARY || !slot.allowModification(player)) {
            return InteractionResult.PASS;
        }

        if (direction == ItemDragDropEvent.StackType.STACKED_ON_OTHER && source.isEmpty()) {
            return InteractionResult.PASS;
        }

        if (Block.byItem(dest.getItem()) instanceof ShulkerBoxBlock
            && dest.has(DataComponents.CONTAINER)) {
            // Check if the source item is not in the blacklist.
            var blockItem = source.getItem();
            var block = Block.byItem(blockItem);
            if (registers.blacklist.contains(blockItem) || registers.blacklist.contains(block)) {
                return InteractionResult.PASS;
            }

            var containerSize = ShulkerBoxBlockEntity.CONTAINER_SIZE;
            var data = dest.get(DataComponents.CONTAINER);

            if (data != null) {
                // Main a nonnulllist of itemstacks that represent the container contents.
                // Read this from the shulkerbox data and then write it back after modification.
                var containerItems = NonNullList.withSize(containerSize, ItemStack.EMPTY);
                data.copyInto(containerItems);

                // Populate the container.
                var container = new SimpleContainer(containerSize);
                for (var i = 0; i < containerSize; i++) {
                    container.setItem(i, containerItems.get(i));
                }

                if (source.isEmpty()) {
                    // Empty out one item from the container.
                    var index = 0;
                    for (var i = containerSize - 1; i >= 0; i--) {
                        if (!container.getItem(i).isEmpty()) {
                            index = i;
                        }
                    }
                    var stack = container.getItem(index);
                    if (stack.isEmpty()) {
                        return InteractionResult.PASS;
                    }

                    var out = stack.copy();
                    container.setItem(index, ItemStack.EMPTY);
                    InventoryTidyingHelper.mergeStacks(container); // merge to remove empty slots
                    if (slot.safeInsert(out).isEmpty()) {
                        playRemoveOneSound(player);
                    }

                } else {
                    // Add hovering item into the container.
                    var result = container.addItem(source);
                    source.setCount(result.getCount());

                    if (result.getCount() == 0) {
                        playInsertSound(player);
                    }
                }

                // Write container back to shulkerbox.
                for (var i = 0; i < containerSize; i++) {
                    var stackInSlot = container.getItem(i);
                    containerItems.set(i, stackInSlot);
                }

                var itemContainerContents = ItemContainerContents.fromItems(containerItems);
                dest.set(DataComponents.CONTAINER, itemContainerContents);
                feature().advancements.draggedItemToShulkerBox(player);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    /**
     * Copypasta from {@link net.minecraft.world.item.BundleItem}
     */
    public void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE,
            0.8f,
            0.8f + entity.level().getRandom().nextFloat() * 0.4f);

        entity.playSound(SoundEvents.SHULKER_BOX_OPEN,
            0.1f + entity.level().getRandom().nextFloat() * 0.3f,
            0.65f + entity.level().getRandom().nextFloat() * 0.4f);
    }

    /**
     * Copypasta from {@link net.minecraft.world.item.BundleItem}
     */
    public void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT,
            0.8f,
            0.8f + entity.level().getRandom().nextFloat() * 0.4f);

        entity.playSound(SoundEvents.SHULKER_BOX_OPEN,
            0.1f + entity.level().getRandom().nextFloat() * 0.3f,
            0.67f + entity.level().getRandom().nextFloat() * 0.4f);
    }
}
