package svenhjol.charm.crafting.tile;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.block.BlockComposter;
import svenhjol.charm.crafting.feature.Composter;
import svenhjol.charm.crafting.message.MessageComposterAddLevel;
import svenhjol.meson.MesonTile;
import svenhjol.meson.NetworkHandler;
import svenhjol.meson.helper.EntityHelper;
import svenhjol.meson.helper.ItemHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileComposter extends MesonTile
{
    public ItemStackHandler input = new ItemStackHandler(1)
    {
        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
        {
            if (world.getBlockState(pos).getBlock() instanceof BlockComposter) {
                if (!stack.isEmpty() && addItem(stack)) {
                    return ItemStack.EMPTY;
                }
            }

            return stack;
        }

    };

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    public IBlockState getBlockState()
    {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockComposter) {
            return state;
        }

        return null;
    }

    public boolean addItem(ItemStack inputStack)
    {
        IBlockState state = world.getBlockState(pos);
        int level = state.getValue(BlockComposter.LEVEL);
        int newLevel = level;

        if (level == 8) {

            // composter is full
            String itemName = Composter.outputs.get(world.rand.nextInt(Composter.outputs.size()));
            ItemStack stack = ItemHelper.getItemStackFromItemString(itemName);

            if (stack != null) {

                if (!world.isRemote) {
                    stack.setCount(world.rand.nextInt(Composter.maxOutput) + 1);

                    // try pushing into neighbour
                    boolean inserted = false;
                    TileEntity n = world.getTileEntity(pos.down());
                    if (n != null) {
                        IItemHandler ni = n.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
                        if (ni != null) {
                            ItemStack copy = stack.copy();
                            if (ItemHandlerHelper.insertItemStacked(ni, copy, false).isEmpty()) {
                                if (ni instanceof TileEntityHopper) {
                                    ((TileEntityHopper) ni).setTransferCooldown(7);
                                }
                                inserted = true;
                                this.markDirty();
                            }
                        }
                    }
                    if (!inserted) {

                        // if couldn't push into neighbour container, pop it out the top of the composter
                        EntityHelper.spawnEntityItem(world, pos.up(), stack);
                    }
                }
                newLevel = 0;

            } else {
                return false; // invalid output item
            }

        } else {

            // check item and increase level based on item chance
            String itemName = ItemHelper.getItemStringFromItemStack(inputStack);

            if (Composter.inputs.containsKey(itemName)) {

                if (!world.isRemote) {
                    inputStack.shrink(1);
                    if (world.rand.nextFloat() < Composter.inputs.get(itemName)) {
                        newLevel++;

                        // let clients know the level has increased
                        NetworkHandler.INSTANCE.sendToAll(new MessageComposterAddLevel(pos, newLevel));
                    }
                }

            } else {
                return false; // invalid input item
            }
        }

        if (newLevel != level) {
            world.setBlockState(pos, state.withProperty(BlockComposter.LEVEL, newLevel), 2);
            world.updateComparatorOutputLevel(pos, null);
        }

        return true;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        return this.getCapability(capability, facing) != null;
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing != EnumFacing.DOWN) {
                return (T) input;
            }
        }
        return super.getCapability(capability, facing);
    }
}
