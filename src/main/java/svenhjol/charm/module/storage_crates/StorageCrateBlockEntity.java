package svenhjol.charm.module.storage_crates;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class StorageCrateBlockEntity extends RandomizableContainerBlockEntity implements BlockEntityClientSerializable, WorldlyContainer {
    private static final int[] SLOTS = IntStream.range(0, StorageCrates.maximumStacks).toArray();
    private NonNullList<ItemStack> items = NonNullList.withSize(StorageCrates.maximumStacks, ItemStack.EMPTY);

    public StorageCrateBlockEntity(BlockPos pos, BlockState state) {
        super(StorageCrates.BLOCK_ENTITY, pos, state);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.items = NonNullList.withSize(StorageCrates.maximumStacks, ItemStack.EMPTY);
        if (!this.tryLoadLootTable(nbt))
            ContainerHelper.loadAllItems(nbt, this.items);
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        super.save(nbt);
        if (!this.trySaveLootTable(nbt))
            ContainerHelper.saveAllItems(nbt, this.items);

        return nbt;
    }

    @Override
    protected Component getDefaultName() {
        return null;
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return null;
    }

    @Override
    public void fromClientTag(CompoundTag nbt) {
        load(nbt);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag nbt) {
        return save(nbt);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> list) {
        this.items = list;
    }

    @Override
    public int getContainerSize() {
        return StorageCrates.maximumStacks;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return SLOTS;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, stack);
        this.sync();
    }

    @Override
    public ItemStack getItem(int slot) {
        ItemStack stack = super.getItem(slot);
        this.sync();

        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        doClientRemoveEffect();
        return super.removeItemNoUpdate(slot);
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction dir) {
        if (!this.isEmpty()) {
            for (ItemStack itemStack : items) {
                if (!itemStack.isEmpty() && ItemStack.isSameItemSameTags(itemStack, stack)) {
                    return true;
                }
            }
        } else if (!stack.isEmpty()) {
            return true;
        }

        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return dir == Direction.DOWN && !stack.isEmpty();
    }

    public boolean isFull() {
        return getItems().stream().allMatch(s -> s.getCount() == s.getMaxStackSize());
    }

    @Nullable
    public ItemStack getItemType() {
        return getItems().stream().filter(s -> !s.isEmpty()).findFirst().orElse(null);
    }

    public int filledStacks() {
        return (int)getItems().stream().filter(s -> !s.isEmpty()).count();
    }

    public ItemStack addStack(ItemStack stack, @Nullable Player player) {
        ItemStack copyStack = stack.copy();
        int maxStackSize = copyStack.getMaxStackSize();
        for (int i = 0; i < getItems().size(); i++) {
            ItemStack stackInSlot = getItem(i);
            if (stackInSlot.isEmpty()) {
                super.setItem(i, copyStack);
                copyStack = ItemStack.EMPTY;
                break;

            } else if (canMergeItems(copyStack, stackInSlot)) {
                int c = maxStackSize - stackInSlot.getCount();
                int d = Math.min(copyStack.getCount(), c);
                stackInSlot.grow(d);
                copyStack.shrink(d);
            }

            if (player != null)
                doClientAddEffect();
        }

        this.sync();

        doClientAddEffect();
        return copyStack;
    }

    public ItemStack takeStack(@Nullable Player player) {
        ItemStack stack = getItemType();
        if (stack == null)
            return ItemStack.EMPTY;

        int maxCount = stack.getItem().getMaxStackSize();
        int c = maxCount;

        for (int i = 0; i < getItems().size(); i++) {
            ItemStack stackInSlot = getItem(i);
            if (player != null)
                doClientRemoveEffect();

            if (!stackInSlot.isEmpty()) {
                int d = Math.min(c, stackInSlot.getCount());
                stackInSlot.shrink(d);
                c -= d;
            }
            if (c == 0) {
                break;
            }
        }

        this.sync();

        doClientRemoveEffect();

        stack.setCount(maxCount - c);
        return stack;
    }

    public int getTotalNumberOfItems() {
        return getItems().stream().map(ItemStack::getCount).reduce(Integer::sum).orElse(0);
    }

    private boolean canMergeItems(ItemStack first, ItemStack second) {
        if (!first.is(second.getItem())) {
            return false;
        } else if (first.getDamageValue() != second.getDamageValue()) {
            return false;
        } else if (first.getCount() > first.getMaxStackSize()) {
            return false;
        } else if (second.getCount() == second.getMaxStackSize()) {
            return false;
        } else {
            return ItemStack.isSameItemSameTags(first, second);
        }
    }

    private void doClientAddEffect() {
        if (this.level != null)
            StorageCrates.sendClientEffects(this.level, this.worldPosition, StorageCrates.ActionType.ADDED);
    }

    private void doClientRemoveEffect() {
        if (this.level != null)
            StorageCrates.sendClientEffects(this.level, this.worldPosition, StorageCrates.ActionType.REMOVED);
    }
}
