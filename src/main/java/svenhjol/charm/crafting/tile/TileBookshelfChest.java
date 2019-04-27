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
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "vazkii.quark.api.IDropoffManager", modid = "quark")
public class TileBookshelfChest extends MesonTileInventory implements IDropoffManager
{
    private int inventorySize;
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
        protected void onLoad()
        {
            super.onLoad();
            recalculateSize();
        }

        @Override
        protected void onContentsChanged(int slot)
        {
            recalculateSize();
            TileBookshelfChest.this.markDirty();
            TileBookshelfChest.this.updateBlock();
        }

        protected void recalculateSize()
        {
            int occupied = 0;
            for (int i = 0; i < getSlots(); i++) {
                if (!getStackInSlot(i).isEmpty()) occupied++;
            }
            inventorySize = occupied;
        }
    };

    protected void updateBlock()
    {
        if (world.getBlockState(pos).getBlock() instanceof BlockBookshelfChest) {
            int slots = getNumberOfFilledSlots();
            IBlockState state = world.getBlockState(pos).withProperty(BlockBookshelfChest.SLOTS, slots);
            world.setBlockState(pos, state, 2);
        }
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
        if (this.hasLootTable() && this.lootSize > 0) {
            return this.lootSize;
        } else {
            return inventorySize;
        }
    }
}
