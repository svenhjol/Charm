package svenhjol.charm.crafting.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.block.BlockBookshelfChest;
import svenhjol.meson.MesonTileInventory;
import vazkii.quark.api.IDropoffManager;

import javax.annotation.Nonnull;

public class TileBookshelfChest extends MesonTileInventory implements IDropoffManager
{
    public static final int SIZE = 9;
    public ItemStackHandler inventory = new ItemStackHandler(SIZE)
    {
        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack)
        {
            Item item = stack.getItem();

            boolean allowed = item == Items.ENCHANTED_BOOK
                || item == Items.BOOK
                || item == Items.WRITABLE_BOOK
                || item == Items.WRITTEN_BOOK
                || item == Items.KNOWLEDGE_BOOK
            ;

            return allowed ? 64 : 0;
        }

        @Override
        protected void onContentsChanged(int slot)
        {
            TileBookshelfChest.this.markDirty();
            TileBookshelfChest.this.updateBlock();
        }
    };

    protected void updateBlock()
    {
        int slots = getNumberOfFilledSlots();
        IBlockState state = world.getBlockState(pos).withProperty(BlockBookshelfChest.SLOTS, slots);
        world.setBlockState(pos, state, 2);
    }

    @Override
    public boolean acceptsDropoff(EntityPlayer player)
    {
        return true;
    }

    @Override
    public ItemStackHandler getInventory()
    {
        return inventory;
    }

    @Override
    public int getInventorySize()
    {
        return SIZE;
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    public int getNumberOfFilledSlots()
    {
        int occupied = 0;
        for (int i = 0; i < this.inventory.getSlots(); i++) {
            if (!this.inventory.getStackInSlot(i).isEmpty()) occupied++;
        }
        return occupied;
    }
}
