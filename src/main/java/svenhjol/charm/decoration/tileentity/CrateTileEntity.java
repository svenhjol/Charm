package svenhjol.charm.decoration.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import svenhjol.charm.decoration.container.CrateContainer;
import svenhjol.charm.decoration.module.Crates;
import svenhjol.meson.iface.IMesonTileEntity;
import vazkii.quark.api.ITransferManager;

public class CrateTileEntity extends LockableLootTileEntity implements ICapabilityProvider, IMesonTileEntity, ITransferManager {
    public static int SIZE = 9;
    private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

    public CrateTileEntity() {
        super(Crates.tile); // TODO set wood type so we can display name
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(tag)) {
            ItemStackHelper.loadAllItems(tag, this.items);
        }
    }

    @Override
    public boolean acceptsTransfer(PlayerEntity playerEntity) {
        return true;
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        if (!this.checkLootAndWrite(tag)) {
            ItemStackHelper.saveAllItems(tag, this.items);
        }
        return tag;
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return CrateContainer.instance(id, player, this);
    }

    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("tile.charm.crate");
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public int getSizeInventory() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public void openInventory(PlayerEntity player) {
        player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_BARREL_OPEN, SoundCategory.BLOCKS, 0.5F, player.world.rand.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void closeInventory(PlayerEntity player) {
        player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_BARREL_CLOSE, SoundCategory.BLOCKS, 0.5F, player.world.rand.nextFloat() * 0.1F + 0.9F);
    }
}
