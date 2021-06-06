package svenhjol.charm.module.bookcases;

import svenhjol.charm.init.CharmSounds;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.module.bookcases.BookcaseBlock;
import svenhjol.charm.module.bookcases.BookcaseScreenHandler;
import svenhjol.charm.module.bookcases.Bookcases;

import java.util.stream.IntStream;

public class BookcaseBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {
    public static int SIZE = 18;
    private static final int[] SLOTS = IntStream.range(0, SIZE).toArray();
    private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

    public BookcaseBlockEntity(BlockPos pos, BlockState state) {
        super(svenhjol.charm.module.bookcases.Bookcases.BLOCK_ENTITY, pos, state);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
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
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> list) {
        this.items = list;
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return new TranslatableComponent("container.charm.bookcase");
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.charm.bookcase");
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new BookcaseScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, stack);
        updateBlockState();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
        return Bookcases.canContainItem(stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public int getContainerSize() {
        return SIZE;
    }

    @Override
    public void startOpen(Player player) {
        player.level.playSound(null, worldPosition, CharmSounds.BOOKSHELF_OPEN, SoundSource.BLOCKS, 0.5f, player.level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void stopOpen(Player player) {
        player.level.playSound(null, worldPosition, CharmSounds.BOOKSHELF_CLOSE, SoundSource.BLOCKS, 0.5f, player.level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void setChanged() {
        updateBlockState();
        super.setChanged();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty())
                return false;
        }
        return true;
    }

    protected void updateBlockState() {
        int filled = 0;

        for (int i = 0; i < SIZE; i++) {
            if (level == null)
                continue;

            ItemStack stack = getItem(i);
            if (stack == null || !stack.isEmpty())
                filled++;
        }

        if (level != null && level.getBlockState(worldPosition).getBlock() instanceof svenhjol.charm.module.bookcases.BookcaseBlock)
            level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(BookcaseBlock.SLOTS, filled), 2);
    }
}
