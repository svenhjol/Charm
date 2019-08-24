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
import svenhjol.charm.decoration.container.CrateContainer;
import svenhjol.charm.decoration.module.Crates;
import svenhjol.meson.iface.IMesonTileEntity;

public class CrateTileEntity extends LockableLootTileEntity implements IMesonTileEntity
{
    public static int SIZE = 9;
    private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

    public CrateTileEntity()
    {
        super(Crates.tile);
    } /* @todo set wood type so we can display name */

    @Override
    public void read(CompoundNBT tag)
    {
        super.read(tag);
        loadFromNBT(tag);
    }

    public void loadFromNBT(CompoundNBT tag)
    {
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        if (!this.checkLootAndRead(tag) && tag.contains("Items")) {
            ItemStackHelper.loadAllItems(tag, this.items);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag)
    {
        super.write(tag);
        return writeToNBT(tag);
    }

    public CompoundNBT writeToNBT(CompoundNBT tag)
    {
        if (!this.checkLootAndRead(tag)) {
            ItemStackHelper.saveAllItems(tag, this.items, false);
        }
        return tag;
    }

    @Override
    protected Container createMenu(int id, PlayerInventory player)
    {
        return CrateContainer.instance(id, player, this);
    }

    @Override
    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("tile.charm.crate");
    }

    @Override
    public NonNullList<ItemStack> getItems()
    {
        return this.items;
    }

    @Override
    public int getSizeInventory()
    {
        return this.items.size();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public void setItems(NonNullList<ItemStack> items)
    {
        this.items = items;
    }

    @Override
    public void openInventory(PlayerEntity player)
    {
        if (!player.isSpectator()) {
            this.world.playSound(null, this.pos, SoundEvents.BLOCK_BARREL_OPEN, SoundCategory.BLOCKS, 0.5f, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }
    }

    @Override
    public void closeInventory(PlayerEntity player)
    {
        this.world.playSound(null, this.pos, SoundEvents.BLOCK_BARREL_CLOSE, SoundCategory.BLOCKS, 0.5f, this.world.rand.nextFloat() * 0.1F + 0.9F);
    }
}
