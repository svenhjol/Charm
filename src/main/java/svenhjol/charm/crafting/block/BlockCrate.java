package svenhjol.charm.crafting.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.base.GuiHandler;
import svenhjol.charm.crafting.tile.TileCrate;
import svenhjol.meson.IMesonBlock;
import svenhjol.meson.IMesonBlock.*;
import svenhjol.meson.IMesonEnum;
import svenhjol.meson.MesonBlockTE;
import svenhjol.meson.helper.EntityHelper;
import svenhjol.meson.helper.SoundHelper;

import javax.annotation.Nullable;

public class BlockCrate extends MesonBlockTE<TileCrate> implements IMesonBlock, IBlockDropsInventory
{
    public enum Type implements IMesonEnum
    {
        CRATE,
        CRATE_SEALED
    }
    public Type type;

    public BlockCrate(Type type)
    {
        super(Material.WOOD, type.getName());
        setHardness(1f);
        setResistance(10f);
        setSoundType(SoundType.WOOD);
        setCreativeTab(CreativeTabs.MISC);

        this.type = type;
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        TileCrate crate = getTileEntity(world, pos);
        
        if (!validTileEntity(crate)) {
            super.getDrops(drops, world, pos, state, fortune);
        }

        if (!isSealedCrate()) {
            dropsSelfWithInventory(drops, world, pos);
        }
    }

    /**
     * Sealed crates drop their contents.
     * Override the MesonBlockTE default drops so we can handle different kinds of item.
     */
    @Override
    public void doItemSpawn(ItemStack stack, World world, BlockPos pos)
    {
        // if entity (like spawn egg), hatch it!
        if (EntityHelper.itemHasEntityTag(stack)) {
            EntityHelper.spawnEntityFromItem(stack, world, pos);
            return;
        }

        // if TNT, prime it!
        if (Block.getBlockFromItem(stack.getItem()) instanceof BlockTNT) {
            @SuppressWarnings("ConstantConditions") // igniter of null seems to be valid when fetched via EntityTNTPrimed::getTntPlacedBy
            EntityTNTPrimed tnt = new EntityTNTPrimed(world, (double)((float)pos.getX() + 0.5F), (double)pos.getY(), (double)((float)pos.getZ() + 0.5F), null);

            if (world.spawnEntity(tnt)) {
                SoundHelper.playSoundAtPos(world, tnt.getPosition(), SoundEvents.ENTITY_TNT_PRIMED, 1.0f, 1.0f);
            }
            return;
        }

        super.doItemSpawn(stack, world, pos);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (world.isRemote && validTileEntity(getTileEntity(world, pos)) && isSealedCrate()) {
            SoundHelper.playSoundAtPos(world, pos, CharmSounds.WOOD_SMASH, 0.6f, 1.0f);
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileCrate crate = getTileEntity(world, pos);

        if (validTileEntity(crate)) {
            if (isSealedCrate()) {
                dropsInventory(crate, TileCrate.SIZE, world, pos);
            } else {
                super.breakBlock(world, pos, state);
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        TileCrate crate = getTileEntity(world, pos);

        if (validTileEntity(crate)) {
            world.setBlockState(pos, state, 2);

            // set the name of the crate so we can see hover
            crate.setName(stack.getDisplayName());
            if (stack.getTagCompound() != null) {
                crate.setLootTable(stack.getTagCompound().getString("lootTable"));
                onBlockAdded(world, pos, state);
            }
        } else {
        
            super.onBlockPlacedBy(world, pos, state, placer, stack);
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        super.onBlockAdded(worldIn, pos, state);
    }

    /**
     * Handles block being right clicked
     */
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileCrate crate = getTileEntity(world, pos);
        if (!validTileEntity(crate)) {
            return false;
        }

        if (crate.hasLootTable()) {
            crate.generateLoot(player);
        }

        if (isSealedCrate()) return true;

        if (player.isSneaking()) {
            return false;
        }

        if (!world.isRemote) {
            player.openGui(Charm.instance, GuiHandler.CRATE, world, pos.getX(), pos.getY(), pos.getZ());
        }

        if (world.isRemote && !isSealedCrate()) {
            player.playSound(CharmSounds.WOOD_OPEN, 1.0f, 1.0f);
        }

        return true;
    }

    @Override
    public Class<TileCrate> getTileEntityClass()
    {
        return TileCrate.class;
    }

    @Nullable
    @Override
    public TileCrate createTileEntity(World world, IBlockState state)
    {
        return new TileCrate();    
    }


    @Override
    public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return !isSealedCrate();
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return isSealedCrate() ? 0 : 50;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    public boolean isSealedCrate()
    {
        return this.type == Type.CRATE_SEALED;
    }
}