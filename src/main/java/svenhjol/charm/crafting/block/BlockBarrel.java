package svenhjol.charm.crafting.block;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.base.GuiHandler;
import svenhjol.charm.crafting.feature.Barrel;
import svenhjol.charm.crafting.tile.TileBarrel;
import svenhjol.meson.IMesonBlock;
import svenhjol.meson.MesonBlockTE;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockBarrel extends MesonBlockTE<TileBarrel> implements IMesonBlock
{
    public static final PropertyDirection FACING = BlockDirectional.FACING;

    public BlockBarrel()
    {
        super(Material.WOOD, "barrel");
        this.setHardness(Barrel.hardness);
        this.setSoundType(SoundType.WOOD);
        setCreativeTab(CreativeTabs.DECORATIONS);
        setDefaultState(getBlockState().getBaseState().withProperty(FACING, EnumFacing.UP));
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote) {

            TileBarrel barrel = getTileEntity(worldIn, pos);
            if (!validTileEntity(barrel)) return false;
            if (playerIn.isSneaking()) return false;

            if (barrel.hasLootTable()) {
                barrel.generateLoot(playerIn);
            }

            playerIn.openGui(Charm.instance, GuiHandler.BARREL, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        if (worldIn.isRemote) {
            playerIn.playSound(CharmSounds.WOOD_OPEN, 1.0f, 1.0f);
        }

        return true;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        TileBarrel barrel = getTileEntity(world, pos);
        if (validTileEntity(barrel)) {
            dropsInventory(barrel, TileBarrel.SIZE, (World)world, pos);
        }
        // drop the barrel after dropping the barrel's inventory
        drops.add(new ItemStack(this));
    }

    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.AIR;
    }

    @Override
    public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return true;
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
    {
        return 50;
    }

    @Override
    public Class<TileBarrel> getTileEntityClass()
    {
        return TileBarrel.class;
    }

    @Nullable
    @Override
    public TileBarrel createTileEntity(World world, IBlockState state)
    {
        return new TileBarrel();
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getDefaultState().withProperty(FACING, facing);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withProperty(FACING, mirrorIn.mirror(state.getValue(FACING)));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getIndex();
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.byIndex(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.UP;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }
}
