package svenhjol.meson;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.Objects;

public abstract class MesonBlockTE<TE extends TileEntity> extends MesonBlock
{
    public MesonBlockTE(Material material, String name)
    {
        super(material, name);
    }

    public abstract Class<TE> getTileEntityClass();

    @SuppressWarnings("unchecked")
    public TE getTileEntity(IBlockAccess world, BlockPos pos)
    {
        return (TE)world.getTileEntity(pos);
    }

    /**
     * Returns true if the tile entity matches the defined TE class.
     */
    public boolean validTileEntity(TE tile)
    {
        return tile != null && tile.getClass() == getTileEntityClass();
    }

    /**
     * Handles dropping a TE's contents (inventory) when broken.
     */
    public void dropsInventory(TE tile, int inventorySize, World world, BlockPos pos)
    {
        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

        if (tile instanceof MesonTileInventory) {
            ((MesonTileInventory)tile).generateLoot(null);
        }

        for (int i = 0; i < inventorySize; i++) {
            ItemStack stack = Objects.requireNonNull(itemHandler).getStackInSlot(i);
            if (!stack.isEmpty()) {
                doItemSpawn(stack, world, pos);
            }
        }
    }

    /**
     * Spawns the item at the position.
     */
    public void doItemSpawn(ItemStack stack, World world, BlockPos pos)
    {
        EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
        world.spawnEntity(item);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        if (willHarvest) return true; // delay deletion of block
        return super.removedByPlayer(state, world, pos, player, false);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        super.harvestBlock(world, player, pos, state, te, stack);
        world.setBlockToAir(pos);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public abstract TE createTileEntity(World world, IBlockState state);

    @SuppressWarnings("deprecation")
    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
    {
        int out = 0;

        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof MesonTileInventory) {
            out = ((MesonTileInventory)tile).getComparatorOutput();
        }

        return out;
    }
}