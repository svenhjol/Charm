package svenhjol.charm.module.bookcases;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.helper.TextHelper;
import svenhjol.charm.helper.WorldHelper;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class BookcaseBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {
    public static final int SIZE = 9;
    private final int[] SLOTS = IntStream.range(0, SIZE).toArray();
    private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

    protected BookcaseBlockEntity(BlockPos pos, BlockState state) {
        super(Bookcases.BLOCK_ENTITY, pos, state);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

        if (!tryLoadLootTable(nbt)) {
            ContainerHelper.loadAllItems(nbt, items);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        if (!trySaveLootTable(nbt)) {
            ContainerHelper.saveAllItems(nbt, items);
        }
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return Bookcases.isValidItem(itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int i, ItemStack itemStack, Direction direction) {
        return true;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, stack);
        updateCapacity();
    }

    @Override
    protected Component getDefaultName() {
        return TextHelper.translatable("container.charm.bookcase");
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new BookcaseMenu(syncId, playerInventory, this);
    }

    @Override
    public int getContainerSize() {
        return SIZE;
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    public int getFilledSlots() {
        var filledslots = 0;

        for (int i = 0; i < SIZE; i++) {
            if (!getItem(i).isEmpty()) {
                filledslots++;
            }
        }

        return filledslots;
    }

    @Nullable
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::getUpdateTag);
    }

    public CompoundTag getUpdateTag() {
        CompoundTag updateTag = new CompoundTag();
        saveAdditional(updateTag);
        return updateTag;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        updateCapacity();
        syncToClient();
    }

    public void syncToClient() {
        Level level = getLevel();
        if (level != null && !level.isClientSide) {
            WorldHelper.syncBlockEntityToClient((ServerLevel)level, getBlockPos());
        }
    }

    @Override
    public void startOpen(Player player) {
        player.level.playSound(null, worldPosition, Bookcases.BOOKCASE_OPEN_SOUND, SoundSource.BLOCKS, 0.6f, player.level.random.nextFloat() * 0.1f + 0.9f);
    }

    @Override
    public void stopOpen(Player player) {
        player.level.playSound(null, worldPosition, Bookcases.BOOKCASE_CLOSE_SOUND, SoundSource.BLOCKS, 0.6f, player.level.random.nextFloat() * 0.1f + 0.9f);
    }

    private void updateCapacity() {
        if (getLevel() == null) return;

        var filledslots = getFilledSlots();
        int capacity;

        var state = getLevel().getBlockState(getBlockPos());

        if (state.getBlock() instanceof BookcaseBlock) {
            if (filledslots == 0) {
                capacity = 0;
            } else if (filledslots >= 1 && filledslots <= Math.ceil(SIZE * 0.44)) {
                capacity = 1;
            } else if (filledslots < SIZE) {
                capacity = 2;
            } else {
                capacity = 3;
            }

            state = state.setValue(BookcaseBlock.CAPACITY, capacity);
            getLevel().setBlockAndUpdate(getBlockPos(), state);
        }
    }
}
