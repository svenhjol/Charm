package svenhjol.charm.crafting.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockFence;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.crafting.feature.Lantern;
import svenhjol.meson.MesonBlock;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class BlockLantern extends MesonBlock
{
    public static final PropertyBool HANGING = PropertyBool.create("hanging");

    public BlockLantern(String variant) {
        super(Material.IRON, variant + "_lantern");
        setHardness(Lantern.hardness);
        setResistance(Lantern.resistance);
        setLightLevel(Lantern.lightLevel);
        setSoundType(SoundType.METAL);
        setCreativeTab(CreativeTabs.DECORATIONS);

        // lantern not hanging by default
        setDefaultState(blockState.getBaseState().withProperty(HANGING, false));
    }

    @Nonnull
    @Override
    public Block setSoundType(@Nonnull SoundType sound)
    {
        return super.setSoundType(sound);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullBlock(IBlockState state)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        AxisAlignedBB aabb;
        float hpad = 5 / 16F;
        float vpad = 7 / 16F;
        float voff = 0.06F;

        if (state.getValue(HANGING)) {
            aabb = new AxisAlignedBB(hpad, voff, hpad, 1F - hpad, (1F - vpad) + voff, 1F - hpad);
        } else {
            aabb = new AxisAlignedBB(hpad, 0F, hpad, 1F - hpad, 1F - vpad, 1F - hpad);
        }

        return aabb;
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return getBoundingBox(blockState, worldIn, pos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        if (Lantern.playSound && rand.nextFloat() <= 0.04f) {
            worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, CharmSounds.LITTLE_FIRE, SoundCategory.BLOCKS, 0.24F, 1.0F, false);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (Lantern.falling) checkFallable(worldIn, pos);
    }

    @Override
    public int tickRate(World worldIn)
    {
        return 2;
    }

    /**
     * Barely changed Copypasta from Quark BlockCandle's Copypasta from BlockFalling.
     *
     * @link {https://github.com/Vazkii/Quark/blob/master/src/main/java/vazkii/quark/decoration/block/BlockCandle.java}
     * @param worldIn World
     * @param pos Pos
     */
    private void checkFallable(World worldIn, BlockPos pos)
    {
        boolean hanging = worldIn.getBlockState(pos).getValue(BlockLantern.HANGING);
        if (hanging && canPlaceBelow(worldIn, pos)) return;
        if (!hanging && canPlaceAbove(worldIn, pos)) return;

        if ((worldIn.isAirBlock(pos.down()) || BlockFalling.canFallThrough(worldIn.getBlockState(pos.down()))) && pos.getY() >= 0) {
            int i = 32;

            if(!BlockFalling.fallInstantly && worldIn.isAreaLoaded(pos.add(-i, -i, -i), pos.add(i, i, i))) {
                if(!worldIn.isRemote) {
                    EntityFallingBlock entityfallingblock = new EntityFallingBlock(worldIn, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, worldIn.getBlockState(pos).withProperty(HANGING, false));
                    worldIn.spawnEntity(entityfallingblock);
                }
            } else {
                IBlockState state = worldIn.getBlockState(pos);
                worldIn.setBlockToAir(pos);
                BlockPos blockpos;

                //noinspection StatementWithEmptyBody
                for(blockpos = pos.down(); (worldIn.isAirBlock(blockpos) || BlockFalling.canFallThrough(worldIn.getBlockState(blockpos))) && blockpos.getY() > 0; blockpos = blockpos.down());

                if(blockpos.getY() > 0)
                    worldIn.setBlockState(blockpos.up(), state);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(HANGING) ? 1 : 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(HANGING, (meta & 1) > 0);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, HANGING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        if (canPlaceBelow(world, pos)) {
            return this.getDefaultState().withProperty(HANGING, true);
        }
        return this.getDefaultState().withProperty(HANGING, false);
    }

    @Override
    public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos)
    {
        return super.canPlaceBlockAt(world, pos) && (canPlaceAbove(world, pos) || canPlaceBelow(world, pos));
    }

    private boolean canPlaceAbove(World world, BlockPos pos)
    {
        IBlockState stateBelow = world.getBlockState(pos.down());
        Block blockBelow = stateBelow.getBlock();

        return blockBelow == Blocks.LIT_PUMPKIN || (blockBelow != Blocks.AIR && blockBelow.canPlaceTorchOnTop(stateBelow, world, pos.down()));
    }

    // Basically Block.canPlaceTorchOnTop, but for trying to place stuff below a block
    private boolean canPlaceBelow(World world, BlockPos pos)
    {
        IBlockState stateAbove = world.getBlockState(pos.up());
        Block blockAbove = stateAbove.getBlock();

        if (stateAbove == Blocks.AIR)
            return false;
        else if (stateAbove.isSideSolid(world, pos.up(), EnumFacing.DOWN) || blockAbove instanceof BlockFence || blockAbove == Blocks.GLASS || blockAbove == Blocks.COBBLESTONE_WALL || blockAbove == Blocks.STAINED_GLASS)
            return true;

        BlockFaceShape shape = stateAbove.getBlockFaceShape(world, pos.up(), EnumFacing.DOWN);
        return (shape == BlockFaceShape.SOLID || shape == BlockFaceShape.CENTER || shape == BlockFaceShape.CENTER_BIG) && !isExceptionBlockForAttaching(blockAbove);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.CENTER;
    }
}