package svenhjol.charm.world.block;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.world.feature.EndPortalRunes;
import svenhjol.charm.world.tile.TileRunePortalFrame;
import svenhjol.meson.MesonBlockTE;
import svenhjol.meson.iface.IMesonBlock;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class BlockRunePortalFrame extends MesonBlockTE<TileRunePortalFrame> implements IMesonBlock
{
    public static final PropertyDirection FACING;
    public static final PropertyEnum<ColorVariant> VARIANT;
    protected static final AxisAlignedBB AABB_BLOCK;
    protected static final AxisAlignedBB AABB_RUNE;
    private static BlockPattern portalShape;

    public BlockRunePortalFrame()
    {
        super(Material.ROCK, "rune_portal_frame");
        setCreativeTab(CreativeTabs.DECORATIONS);
        setDefaultState(this.blockState.getBaseState()
            .withProperty(FACING, EnumFacing.NORTH)
            .withProperty(VARIANT, ColorVariant.WHITE)
        );
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @SuppressWarnings("deprecation")
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_BLOCK);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_RUNE);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.AIR;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
        EndPortalRunes.deactivate(worldIn, pos);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        TileRunePortalFrame portalFrame = getTileEntity(worldIn, pos);
        if (validTileEntity(portalFrame)) {
            EnumFacing facing = placer.getHorizontalFacing().getOpposite();
            portalFrame.setFacing(facing);

            IBlockState changed = state.withProperty(BlockRunePortalFrame.FACING, facing);
            worldIn.setBlockState(pos, changed, 2);
        }

        // it's possible to put down a valid portal in creative mode
        EndPortalRunes.activate(worldIn, pos);
    }

    @Override
    public String[] getVariants()
    {
        List<String> variants = new ArrayList<>();
        for (ColorVariant variant : ColorVariant.values()) {
            variants.add(variant.toString().toLowerCase());
        }

        return variants.toArray(new String[0]);
    }

    @SuppressWarnings("deprecation")
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(VARIANT, ColorVariant.byMetadata(meta));
    }

    public static BlockPattern getOrCreatePortalShape()
    {
        if (portalShape == null) {
            portalShape = FactoryBlockPattern.start().aisle("?vvv?", ">???<", ">???<", ">???<", "?^^^?")
                .where('?', BlockWorldState.hasState(BlockStateMatcher.ANY))
                .where('^', BlockWorldState.hasState(BlockStateMatcher.forBlock(EndPortalRunes.frame)
                    .where(FACING, enumFacing -> Objects.equals(enumFacing, EnumFacing.SOUTH))
                ))
                .where('>', BlockWorldState.hasState(BlockStateMatcher.forBlock(EndPortalRunes.frame)
                    .where(FACING, enumFacing -> Objects.equals(enumFacing, EnumFacing.WEST))
                ))
                .where('v', BlockWorldState.hasState(BlockStateMatcher.forBlock(EndPortalRunes.frame)
                    .where(FACING, enumFacing -> Objects.equals(enumFacing, EnumFacing.NORTH))
                ))
                .where('<', BlockWorldState.hasState(BlockStateMatcher.forBlock(EndPortalRunes.frame)
                    .where(FACING, enumFacing -> Objects.equals(enumFacing, EnumFacing.EAST))
                ))
                .build();
        }

        return portalShape;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(VARIANT).ordinal();
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, ColorVariant.byMetadata(meta));
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileRunePortalFrame portal = (TileRunePortalFrame)worldIn.getTileEntity(pos);
        if (portal != null && validTileEntity(portal)) {
            EnumFacing facing = portal.getFacing();
            state = state.withProperty(FACING, facing);
        }
        return state;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, VARIANT);
    }

    @Override
    public TileRunePortalFrame createTileEntity(World world, IBlockState state)
    {
        return new TileRunePortalFrame();
    }

    @Override
    public Class<TileRunePortalFrame> getTileEntityClass()
    {
        return TileRunePortalFrame.class;
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
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

    static {
        FACING = BlockHorizontal.FACING;
        VARIANT = PropertyEnum.create("variant", ColorVariant.class);
        AABB_BLOCK = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D);
        AABB_RUNE = new AxisAlignedBB(0.3125D, 0.8125D, 0.3125D, 0.6875D, 1.0D, 0.6875D);
    }
}
