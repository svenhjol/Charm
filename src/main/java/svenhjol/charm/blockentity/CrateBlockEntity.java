package svenhjol.charm.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import svenhjol.charm.module.Crates;
import svenhjol.charm.screenhandler.CrateScreenHandler;

import javax.annotation.Nullable;
import java.util.stream.IntStream;

public class CrateBlockEntity extends LootableContainerBlockEntity implements SidedInventory {
    public static int SIZE = 9;
    private static final int[] SLOTS = IntStream.range(0, SIZE).toArray();
    private DefaultedList<ItemStack> items = DefaultedList.ofSize(SIZE, ItemStack.EMPTY);

    public CrateBlockEntity(BlockPos pos, BlockState state) {
        super(Crates.BLOCK_ENTITY, pos, state);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        this.items = DefaultedList.ofSize(SIZE, ItemStack.EMPTY);
        if (!this.deserializeLootTable(tag))
            Inventories.fromTag(tag, this.items);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        if (!this.serializeLootTable(tag))
            Inventories.toTag(tag, this.items, false);

        return tag;
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
    public int[] getAvailableSlots(Direction side) {
        return SLOTS;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return Crates.canCrateInsertItem(stack);
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public int size() {
        return SIZE;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty())
                return false;
        }
        return true;
    }

    @Nullable
    @Override
    public Text getCustomName() {
        return new TranslatableText("container.charm.crate");
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText("container.charm.crate");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new CrateScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public void onOpen(PlayerEntity player) {
        player.world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_BARREL_OPEN, SoundCategory.BLOCKS, 0.5F, player.world.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void onClose(PlayerEntity player) {
        player.world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_BARREL_CLOSE, SoundCategory.BLOCKS, 0.5F, player.world.random.nextFloat() * 0.1F + 0.9F);
    }
}
