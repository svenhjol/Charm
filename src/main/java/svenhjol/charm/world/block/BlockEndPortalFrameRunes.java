package svenhjol.charm.world.block;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.world.tile.TileEndPortalRunes;
import svenhjol.meson.MesonBlockTE;
import svenhjol.meson.iface.IMesonBlock;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockEndPortalFrameRunes extends MesonBlockTE<TileEndPortalRunes> implements IMesonBlock
{
    public static final PropertyDirection FACING;
    public static final PropertyEnum<ColorVariant> COLOR;
    protected static final AxisAlignedBB AABB_BLOCK;
    protected static final AxisAlignedBB AABB_RUNE;
    private static BlockPattern portalShape;

    public BlockEndPortalFrameRunes()
    {
        super(Material.ROCK, "end_portal_frame_runes");
        setCreativeTab(CreativeTabs.DECORATIONS);
        setDefaultState(this.blockState.getBaseState()
            .withProperty(FACING, EnumFacing.NORTH)
            .withProperty(COLOR, ColorVariant.WHITE)
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
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        TileEndPortalRunes portalFrame = getTileEntity(worldIn, pos);
        if (validTileEntity(portalFrame)) {
            EnumFacing facing = state.getValue(FACING);
            portalFrame.setFacing(facing);
        }
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    // TODO this should be generic
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
        return this.getDefaultState()
            .withProperty(FACING, facing)
            .withProperty(COLOR, ColorVariant.WHITE);
    }
//
//    @Override
//    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
//    {
//        ItemStack rune = EndPortalRunes.removeRune(worldIn, pos);
//        if (rune != null) {
//            PlayerHelper.addOrDropStack(playerIn, rune);
//            return true;
//        }
//
//        return false;
//    }

    //    public static BlockPattern getOrCreatePortalShape()
//    {
//        if (portalShape == null) {
//            portalShape = FactoryBlockPattern.start().aisle(new String[]{"?vvv?", ">???<", ">???<", ">???<", "?^^^?"})
//                .where('?', BlockWorldState.hasState(BlockStateMatcher.ANY))
//                .where('^', BlockWorldState.hasState(BlockStateMatcher.forBlock(EndPortalRunes.portalFrame)
//                    .where(COLOR, Predicates.equalTo(this.getBlockState().getBaseState().getValue(COLOR)))
//                    .where(FACING, Predicates.equalTo(EnumFacing.SOUTH))
//                ))
//                .where('>', BlockWorldState.hasState(BlockStateMatcher.forBlock(EndPortalRunes.portalFrame)
//                    .where(COLOR, Predicates.equalTo(this.getBlockState().getBaseState().getValue(COLOR)))
//                    .where(FACING, Predicates.equalTo(EnumFacing.WEST))
//                ))
//                .where('v', BlockWorldState.hasState(BlockStateMatcher.forBlock(EndPortalRunes.portalFrame)
//                    .where(COLOR, Predicates.equalTo(this.getBlockState().getBaseState().getValue(COLOR)))
//                    .where(FACING, Predicates.equalTo(EnumFacing.NORTH))
//                ))
//                .where('<', BlockWorldState.hasState(BlockStateMatcher.forBlock(EndPortalRunes.portalFrame)
//                    .where(COLOR, Predicates.equalTo(this.getBlockState().getBaseState().getValue(COLOR)))
//                    .where(FACING, Predicates.equalTo(EnumFacing.EAST))
//                ))
//                .build();
//        }
//
//        return portalShape;
//    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(COLOR).ordinal();
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(COLOR, ColorVariant.byMetadata(meta));
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileEndPortalRunes portal = (TileEndPortalRunes)worldIn.getTileEntity(pos);
        if (portal != null && validTileEntity(portal)) {
            EnumFacing facing = portal.getFacing();
            state = state.withProperty(FACING, facing);
        }
        return state;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, COLOR );
    }

    @Override
    public TileEndPortalRunes createTileEntity(World world, IBlockState state)
    {
        return new TileEndPortalRunes();
    }

    @Override
    public Class<TileEndPortalRunes> getTileEntityClass()
    {
        return TileEndPortalRunes.class;
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

    static {
        FACING = BlockHorizontal.FACING;
        COLOR = PropertyEnum.create("color", ColorVariant.class);
        AABB_BLOCK = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D);
        AABB_RUNE = new AxisAlignedBB(0.3125D, 0.8125D, 0.3125D, 0.6875D, 1.0D, 0.6875D);
    }
}
