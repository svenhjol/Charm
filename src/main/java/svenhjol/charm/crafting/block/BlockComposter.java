package svenhjol.charm.crafting.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.feature.Composter;
import svenhjol.meson.MesonBlock;
import svenhjol.meson.helper.EntityHelper;
import svenhjol.meson.helper.ItemHelper;
import svenhjol.meson.helper.SoundHelper;

public class BlockComposter extends MesonBlock
{
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 8);

    public BlockComposter()
    {
        super(Material.WOOD, "composter");
        setHardness(2.0f);
        setSoundType(SoundType.WOOD);
        setHarvestLevel("axe", 1);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS); // ?
        setDefaultState(blockState.getBaseState().withProperty(LEVEL, 0));
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        int level = state.getValue(LEVEL);
        int newLevel = level;

        if (level == 8) {

            // composter is full
            String itemName = Composter.outputs.get(world.rand.nextInt(Composter.outputs.size()));
            ItemStack stack = ItemHelper.getItemStackFromItemString(itemName);

            if (stack != null) {
                playSpawnOutputSound(world, pos);
                if (!world.isRemote) {
                    EntityHelper.spawnEntityItem(world, pos.offset(EnumFacing.UP), stack);
                }
                newLevel = 0;
            } else {
                return false;
            }

        } else {

            // check item and increase level based on item chance
            ItemStack held = player.getHeldItem(hand);
            String itemName = ItemHelper.getItemStringFromItemStack(held);

            if (Composter.inputs.containsKey(itemName)) {
                held.shrink(1);
                playAddItemSound(world, pos);

                if (world instanceof WorldServer) {
                    if (world.rand.nextFloat() < Composter.inputs.get(itemName)) {
                        newLevel++;
                    }
                }

            } else {
                return false;
            }
        }

        if (newLevel != level) {
            spawnAddLevelParticles(world, pos);
            world.setBlockState(pos, state.withProperty(LEVEL, newLevel), 2);
        }

        return true;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos)
    {
        int out = 0;
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockComposter) {
            out = state.getValue(LEVEL);
        }
        return out;
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

    protected void spawnAddLevelParticles(World world, BlockPos pos)
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
    protected void playAddItemSound(World world, BlockPos pos)
    {
        SoundHelper.playSoundAtPos(world, pos, SoundEvents.ITEM_HOE_TILL, 1.0f, 1.0f);
    }

    @SideOnly(Side.CLIENT)
    protected void playSpawnOutputSound(World world, BlockPos pos)
    {
        SoundHelper.playSoundAtPos(world, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
    }
}
