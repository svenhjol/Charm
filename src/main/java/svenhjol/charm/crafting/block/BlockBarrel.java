package svenhjol.charm.crafting.block;

import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.base.GuiHandler;
import svenhjol.charm.crafting.feature.Barrel;
import svenhjol.charm.crafting.tile.TileBarrel;
import svenhjol.meson.MesonBlockTE;
import svenhjol.meson.iface.IMesonBlock;
import vazkii.quark.api.IRotationLockHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Optional.Interface(iface = "vazkii.quark.api.IRotationLockHandler", modid = "quark")
public class BlockBarrel extends MesonBlockTE<TileBarrel> implements IMesonBlock, IRotationLockHandler
{
    public static final PropertyDirection FACING = BlockDirectional.FACING;
    public static PropertyEnum<WoodVariant> VARIANT = PropertyEnum.create("variant", WoodVariant.class);

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
    public String[] getVariants()
    {
        List<String> variants = new ArrayList<>();
        for (WoodVariant variant : WoodVariant.values()) {
            variants.add(variant.toString().toLowerCase());
        }

        return variants.toArray(new String[0]);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        TileBarrel barrel = getTileEntity(worldIn, pos);
        if (validTileEntity(barrel)) {
            EnumFacing facing = state.getValue(FACING);
            barrel.setFacing(facing);
            barrel.setName(stack.getDisplayName());
        }
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
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

        if (worldIn.isRemote && !playerIn.isSneaking()) {
            playerIn.playSound(CharmSounds.WOOD_OPEN, 1.0f, 1.0f);
        }

        return true;
    }

    /**
     * Return the state that should be placed in the world for the rotation lock
     * currently enabled.
     * @param facing The face currently locked
     * @param hasHalf true if the rotation lock specifies a block half, false otherwise
     * @param topHalf true if the rotation lock applies to the upper half of the block, false otherwise
     */
    @Override
    public IBlockState setRotation(World world, BlockPos pos, IBlockState state, EnumFacing facing, boolean hasHalf, boolean topHalf)
    {
        TileBarrel barrel = getTileEntity(world, pos);
        if (validTileEntity(barrel)) {
            barrel.setFacing(facing);
        }

        state = state.withProperty(FACING, facing);
        world.setBlockState(pos, state, 2);
        return state;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        ItemStack thisBarrel = new ItemStack(this, 1, getMetaFromState(state));

        TileBarrel barrel = getTileEntity(world, pos);
        if (validTileEntity(barrel)) {
            dropsInventory(barrel, TileBarrel.SIZE, (World)world, pos);
            thisBarrel.setStackDisplayName(barrel.getName());
        }
        // drop the barrel after dropping the barrel's inventory
        drops.add(thisBarrel);
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
        return getDefaultState()
            .withProperty(VARIANT, WoodVariant.byMetadata(meta))
            .withProperty(FACING, facing);
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
        return state.getValue(VARIANT).ordinal();
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, WoodVariant.byMetadata(meta));
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileBarrel barrel = (TileBarrel)worldIn.getTileEntity(pos);
        if (barrel != null && validTileEntity(barrel)) {
            EnumFacing facing = barrel.getFacing();

            if (facing.getAxis() == EnumFacing.Axis.Y) {
                facing = EnumFacing.UP;
            }

            state = state.withProperty(FACING, facing);
        }
        return state;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, VARIANT);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this, 1, getMetaFromState(world.getBlockState(pos)));
    }
}
