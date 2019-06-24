package svenhjol.charm.crafting.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.ItemStackHandler;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.block.BlockBookshelfChest;
import svenhjol.charm.crafting.feature.BookshelfChest;
import svenhjol.meson.MesonTileInventory;
import svenhjol.meson.helper.ItemHelper;
import vazkii.quark.api.IDropoffManager;

import javax.annotation.Nonnull;

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
            boolean allowed = !ItemHelper.getMatchingItemKey(BookshelfChest.validItems, stack).isEmpty();
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
        if (world.getBlockState(pos).getBlock() == BookshelfChest.bookshelfChest) {
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

    @Override
    public int getComparatorOutput()
    {
        return getNumberOfFilledSlots();
    }

    @Override
    public String getDefaultName()
    {
        //noinspection deprecation
        return I18n.translateToLocal("tile.charm:bookshelf_chest.name");
    }
}
