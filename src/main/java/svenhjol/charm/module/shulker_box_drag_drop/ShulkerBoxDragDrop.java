package svenhjol.charm.module.shulker_box_drag_drop;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.api.event.StackItemOnItemCallback;
import svenhjol.charm.api.event.StackItemOnItemCallback.Direction;
import svenhjol.charm.helper.TagHelper;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.inventory_tidying.InventoryTidyingHandler;

import java.util.ArrayList;
import java.util.List;

@CommonModule(mod = Charm.MOD_ID, description = "Drag and drop items into a shulkerbox from within your inventory.")
public class ShulkerBoxDragDrop extends CharmModule {
    public static final List<ItemLike> BLACKLIST = new ArrayList<>();

    @Override
    public void runWhenEnabled() {
        StackItemOnItemCallback.EVENT.register(this::handleInventoryInteraction);
        ServerWorldEvents.LOAD.register(this::handleWorldLoad);
    }

    private void handleWorldLoad(MinecraftServer server, ServerLevel level) {
        // do not allow shulkerboxes to be added to shulkerboxes
        if (level.dimension() == Level.OVERWORLD) {
            for (Block block : TagHelper.getBlockValues(BlockTags.SHULKER_BOXES)) {
                if (!BLACKLIST.contains(block)) {
                    BLACKLIST.add(block);
                }
            }
        }
    }

    private boolean handleInventoryInteraction(Direction direction, ItemStack source, ItemStack dest, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (clickAction != ClickAction.SECONDARY || !slot.allowModification(player)) return false;

        if (direction == Direction.STACKED_ON_OTHER && source.isEmpty()) {
            return false;
        }

        if (Block.byItem(dest.getItem()) instanceof ShulkerBoxBlock shulkerBoxBlock) {
            // check if the item is not in the blacklist
            Item item = source.getItem();
            Block block = Block.byItem(item);
            if (BLACKLIST.contains(item) || BLACKLIST.contains(block)) {
                return false;
            }

            CompoundTag shulkerBoxTag = BlockItem.getBlockEntityData(dest);
            BlockEntity blockEntity;

            if (shulkerBoxTag == null) {
                // generate a new empty blockentity
                blockEntity = shulkerBoxBlock.newBlockEntity(BlockPos.ZERO, shulkerBoxBlock.defaultBlockState());
            } else {
                // instantiate existing shulkerbox blockentity from BlockEntityTag
                blockEntity = BlockEntity.loadStatic(BlockPos.ZERO, shulkerBoxBlock.defaultBlockState(), shulkerBoxTag);
            }

            if (blockEntity instanceof ShulkerBoxBlockEntity shulkerBox) {
                int size = ShulkerBoxBlockEntity.CONTAINER_SIZE;

                // populate the container
                SimpleContainer container = new SimpleContainer(size);
                for (int i = 0; i < size; i++) {
                    container.setItem(i, shulkerBox.getItem(i));
                }

                if (source.isEmpty()) {
                    // empty out one item from the container
                    int index = 0;
                    for (int i = size - 1; i >= 0; i--) {
                        if (!container.getItem(i).isEmpty()) {
                            index = i;
                        }
                    }
                    ItemStack stack = container.getItem(index);
                    if (stack.isEmpty()) return false;

                    ItemStack out = stack.copy();
                    container.setItem(index, ItemStack.EMPTY);
                    InventoryTidyingHandler.mergeStacks(container); // merge to remove empty slots
                    if (slot.safeInsert(out).isEmpty()) {
                        playRemoveOneSound(player);
                    }

                } else {
                    // add hovering item into the container
                    ItemStack result = container.addItem(source);
                    source.setCount(result.getCount());

                    if (result.getCount() == 0) {
                        playInsertSound(player);
                    }
                }

                // write container back to shulkerbox
                for (int i = 0; i < size; i++) {
                    ItemStack stackInSlot = container.getItem(i);
                    shulkerBox.setItem(i, stackInSlot);
                }

                shulkerBox.saveToItem(dest);
                return true;
            }
        }

        return false;
    }

    /**
     * Copypasta from {@link net.minecraft.world.item.BundleItem}
     */
    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8f, 0.8f + entity.getLevel().getRandom().nextFloat() * 0.4f);
        entity.playSound(SoundEvents.SHULKER_BOX_OPEN, 0.1f + entity.getLevel().getRandom().nextFloat() * 0.3f, 0.65f + entity.getLevel().getRandom().nextFloat() * 0.4f);
    }

    /**
     * Copypasta from {@link net.minecraft.world.item.BundleItem}
     */
    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8f, 0.8f + entity.getLevel().getRandom().nextFloat() * 0.4f);
        entity.playSound(SoundEvents.SHULKER_BOX_OPEN, 0.1f + entity.getLevel().getRandom().nextFloat() * 0.3f, 0.67f + entity.getLevel().getRandom().nextFloat() * 0.4f);
    }
}
