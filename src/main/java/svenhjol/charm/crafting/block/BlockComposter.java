package svenhjol.charm.crafting.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.tile.TileComposter;
import svenhjol.meson.iface.IMesonBlock;
import svenhjol.meson.MesonBlockTE;
import svenhjol.meson.helper.SoundHelper;

import javax.annotation.Nullable;
import java.util.List;

public class BlockComposter extends MesonBlockTE<TileComposter> implements IMesonBlock
{
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 8);
    protected static final AxisAlignedBB AABB_WALL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.125D);
    protected static final AxisAlignedBB AABB_WALL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.875D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_EAST = new AxisAlignedBB(0.875D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB AABB_WALL_WEST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.125D, 1.0D, 1.0D);

    public BlockComposter()
    {
        super(Material.WOOD, "composter");
        setHardness(2.0f);
        setSoundType(SoundType.WOOD);
        setHarvestLevel("axe", 1);
        setCreativeTab(CreativeTabs.DECORATIONS);
        setDefaultState(blockState.getBaseState().withProperty(LEVEL, 0));
        this.useNeighborBrightness = true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_WEST);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_NORTH);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_EAST);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_WALL_SOUTH);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public Class<TileComposter> getTileEntityClass()
    {
        return TileComposter.class;
    }

    @Override
    public TileComposter createTileEntity(World world, IBlockState state)
    {
        return new TileComposter();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        boolean added = false;
        TileComposter composter = getTileEntity(world, pos);
        if (validTileEntity(composter)) {
            added = composter.addItem(player.getHeldItem(hand), player.isCreative());
            if (added && world.isRemote) {
                playAddItemSound(world, pos);
                spawnParticles(world, pos);
            }
        }
        return added;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos)
    {
        return blockState.getValue(LEVEL);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, LEVEL);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(LEVEL);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(LEVEL, meta);
    }

    @SideOnly(Side.CLIENT)
    public void spawnParticles(World world, BlockPos pos)
    {
        for (int i = 0; i < 8; ++i) {
            double d0 = world.rand.nextGaussian() * 0.02D;
            double d1 = world.rand.nextGaussian() * 0.02D;
            double d2 = world.rand.nextGaussian() * 0.02D;
            double dx = (float)pos.getX() + MathHelper.clamp(world.rand.nextFloat(), 0.25f, 0.75f);
            double dy = (float)pos.getY() + 1.05f;
            double dz = (float)pos.getZ() + MathHelper.clamp(world.rand.nextFloat(), 0.25f, 0.75f);
            world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, dx, dy, dz, d0, d1, d2);
        }
    }

    @SideOnly(Side.CLIENT)
    public void playAddItemSound(World world, BlockPos pos)
    {
        SoundHelper.playSoundAtPos(world, pos, SoundEvents.BLOCK_CLOTH_BREAK, 1.0f, 1.0f);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return face == EnumFacing.UP ? BlockFaceShape.BOWL : BlockFaceShape.SOLID;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
}
