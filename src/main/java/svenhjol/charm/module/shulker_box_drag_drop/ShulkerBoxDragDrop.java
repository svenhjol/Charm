package svenhjol.charm.module.shulker_box_drag_drop;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.event.StackItemOnItemCallback;
import svenhjol.charm.event.StackItemOnItemCallback.Direction;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID)
public class ShulkerBoxDragDrop extends CharmModule {
    @Override
    public void runWhenEnabled() {
        StackItemOnItemCallback.EVENT.register(this::handleInventoryInteraction);
    }

    private boolean handleInventoryInteraction(Direction direction, ItemStack source, ItemStack dest, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (clickAction != ClickAction.SECONDARY || !slot.allowModification(player)) {
            return false;
        }

        if (Block.byItem(dest.getItem()) instanceof ShulkerBoxBlock shulkerBoxBlock) {
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
                    slot.safeInsert(out);

                } else {
                    // add hovering item into the container
                    ItemStack result = container.addItem(source);
                    source.setCount(result.getCount());
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
}
