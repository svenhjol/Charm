package svenhjol.charm.feature.shulker_box_drag_drop;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.inventory_tidying.InventoryTidyingHandler;
import svenhjol.charm_api.event.ItemDragDropEvent;
import svenhjol.charm_api.event.ItemDragDropEvent.StackType;
import svenhjol.charm_api.event.LevelLoadEvent;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.helper.TagHelper;

import java.util.ArrayList;
import java.util.List;

@Feature(mod = Charm.MOD_ID, description = "Drag and drop items into a shulkerbox from within your inventory.")
public class ShulkerBoxDragDrop extends CharmFeature {
    public static final List<ItemLike> BLACKLIST = new ArrayList<>();

    @Override
    public void runWhenEnabled() {
        ItemDragDropEvent.INSTANCE.handle(this::handleItemDragDrop);
        LevelLoadEvent.INSTANCE.handle(this::handleLevelLoad);
    }

    private void handleLevelLoad(MinecraftServer server, ServerLevel level) {
        // Do not allow shulkerboxes to be added to shulkerboxes.
        // We do this at world init because otherwise the tags are not resolved.
        if (level.dimension() == Level.OVERWORLD) {
            for (var block : TagHelper.getValues(BuiltInRegistries.BLOCK, BlockTags.SHULKER_BOXES)) {
                if (!BLACKLIST.contains(block)) {
                    BLACKLIST.add(block);
                }
            }
        }
    }

    private InteractionResult handleItemDragDrop(StackType direction, ItemStack source, ItemStack dest, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (clickAction != ClickAction.SECONDARY || !slot.allowModification(player)) {
            return InteractionResult.PASS;
        }

        if (direction == StackType.STACKED_ON_OTHER && source.isEmpty()) {
            return InteractionResult.PASS;
        }

        if (Block.byItem(dest.getItem()) instanceof ShulkerBoxBlock shulkerBoxBlock) {
            // Check if the item is not in the blacklist.
            var item = source.getItem();
            var block = Block.byItem(item);
            if (BLACKLIST.contains(item) || BLACKLIST.contains(block)) {
                return InteractionResult.PASS;
            }

            var shulkerBoxTag = BlockItem.getBlockEntityData(dest);
            BlockEntity blockEntity;

            if (shulkerBoxTag == null) {
                // Generate a new empty blockentity.
                blockEntity = shulkerBoxBlock.newBlockEntity(BlockPos.ZERO, shulkerBoxBlock.defaultBlockState());
            } else {
                // Instantiate existing shulkerbox blockentity from BlockEntityTag.
                blockEntity = BlockEntity.loadStatic(BlockPos.ZERO, shulkerBoxBlock.defaultBlockState(), shulkerBoxTag);
            }

            if (blockEntity instanceof ShulkerBoxBlockEntity shulkerBox) {
                var size = ShulkerBoxBlockEntity.CONTAINER_SIZE;

                // Populate the container.
                var container = new SimpleContainer(size);
                for (var i = 0; i < size; i++) {
                    container.setItem(i, shulkerBox.getItem(i));
                }

                if (source.isEmpty()) {
                    // Empty out one item from the container.
                    var index = 0;
                    for (var i = size - 1; i >= 0; i--) {
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
                    InventoryTidyingHandler.mergeStacks(container); // merge to remove empty slots
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
                for (var i = 0; i < size; i++) {
                    var stackInSlot = container.getItem(i);
                    shulkerBox.setItem(i, stackInSlot);
                }

                shulkerBox.saveToItem(dest);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    /**
     * Copypasta from {@link net.minecraft.world.item.BundleItem}
     */
    private void playRemoveOneSound(Entity entity) {
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
    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT,
            0.8f,
            0.8f + entity.level().getRandom().nextFloat() * 0.4f);

        entity.playSound(SoundEvents.SHULKER_BOX_OPEN,
            0.1f + entity.level().getRandom().nextFloat() * 0.3f,
            0.67f + entity.level().getRandom().nextFloat() * 0.4f);
    }
}
