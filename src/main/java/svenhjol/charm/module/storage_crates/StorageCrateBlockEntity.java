package svenhjol.charm.module.storage_crates;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class StorageCrateBlockEntity extends LootableContainerBlockEntity implements BlockEntityClientSerializable, SidedInventory {
    private static final int[] SLOTS = IntStream.range(0, StorageCrates.maximumStacks).toArray();
    private DefaultedList<ItemStack> items = DefaultedList.ofSize(StorageCrates.maximumStacks, ItemStack.EMPTY);

    public StorageCrateBlockEntity(BlockPos pos, BlockState state) {
        super(StorageCrates.BLOCK_ENTITY, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.items = DefaultedList.ofSize(StorageCrates.maximumStacks, ItemStack.EMPTY);
        if (!this.deserializeLootTable(nbt))
            Inventories.readNbt(nbt, this.items);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (!this.serializeLootTable(nbt))
            Inventories.writeNbt(nbt, this.items);

        return nbt;
    }

    @Override
    protected Text getContainerName() {
        return null;
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return null;
    }

    @Override
    public void fromClientTag(NbtCompound nbt) {
        readNbt(nbt);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound nbt) {
        return writeNbt(nbt);
    }

    @Override
    public DefaultedList<ItemStack> getInvStackList() {
        return this.items;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.items = list;
    }

    @Override
    public int size() {
        return StorageCrates.maximumStacks;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return SLOTS;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        super.setStack(slot, stack);
        this.sync();

        doClientRemoveEffect();
    }

    @Override
    public ItemStack getStack(int slot) {
        ItemStack stack = super.getStack(slot);
        this.sync();

        if (!stack.isEmpty())
            doClientAddEffect();

        return stack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        doClientRemoveEffect();
        return super.removeStack(slot);
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        if (!this.isEmpty()) {
            for (ItemStack itemStack : items) {
                if (!itemStack.isEmpty() && itemStack.getItem() == stack.getItem()) {
                    return true;
                }
            }
        } else if (!stack.isEmpty()) {
            return true;
        }

        return false;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return dir == Direction.DOWN && !stack.isEmpty();
    }

    public boolean isFull() {
        return getInvStackList().stream().allMatch(s -> s.getCount() == s.getMaxCount());
    }

    @Nullable
    public ItemStack getItemType() {
        return getInvStackList().stream().filter(s -> !s.isEmpty()).findFirst().orElse(null);
    }

    public int filledStacks() {
        return (int)getInvStackList().stream().filter(s -> !s.isEmpty()).count();
    }

    public ItemStack addStack(ItemStack stack) {
        for (int i = 0; i < getInvStackList().size(); i++) {
            ItemStack stackInSlot = super.getStack(i); // call super so we don't get particle effects
            if (stackInSlot.isEmpty()) {
                super.setStack(i, stack); // call super so we don't get particle effects
                stack = ItemStack.EMPTY;
                break;

            } else if (canMergeItems(stack, stackInSlot)) {
                int c = stack.getMaxCount() - stackInSlot.getCount();
                int d = Math.min(stack.getCount(), c);
                stackInSlot.increment(d);
                stack.decrement(d);
            }
        }

        this.sync();

        doClientAddEffect();
        return stack;
    }

    public ItemStack takeStack() {
        ItemStack stack = getItemType();
        if (stack == null)
            return ItemStack.EMPTY;

        int maxCount = stack.getItem().getMaxCount();
        int c = maxCount;

        for (int i = 0; i < getInvStackList().size(); i++) {
            ItemStack stackInSlot = super.getStack(i); // call super so we don't get particle effects
            if (!stackInSlot.isEmpty()) {
                int d = Math.min(c, stackInSlot.getCount());
                stackInSlot.decrement(d);
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
        return getInvStackList().stream().map(ItemStack::getCount).reduce(Integer::sum).orElse(0);
    }

    private boolean canMergeItems(ItemStack first, ItemStack second) {
        if (!first.isOf(second.getItem())) {
            return false;
        } else if (first.getDamage() != second.getDamage()) {
            return false;
        } else if (first.getCount() > first.getMaxCount()) {
            return false;
        } else if (second.getCount() == second.getMaxCount()) {
            return false;
        } else {
            return ItemStack.areTagsEqual(first, second);
        }
    }

    private void doClientAddEffect() {
        if (this.world != null)
            StorageCrates.sendClientEffects(this.world, this.pos, StorageCrates.ActionType.ADDED);
    }

    private void doClientRemoveEffect() {
        if (this.world != null)
            StorageCrates.sendClientEffects(this.world, this.pos, StorageCrates.ActionType.REMOVED);
    }
}
