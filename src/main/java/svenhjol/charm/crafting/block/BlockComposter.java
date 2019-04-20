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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.feature.Composter;
import svenhjol.charm.crafting.message.MessageComposterAddLevel;
import svenhjol.meson.MesonBlock;
import svenhjol.meson.NetworkHandler;
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
                if (world.isRemote) playOutputSound(world, pos);

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

                if (world.isRemote) playAddItemSound(world, pos);

                if (!world.isRemote) {
                    if (world.rand.nextFloat() < Composter.inputs.get(itemName)) {
                        newLevel++;

                        // let clients know the level has increased
                        NetworkHandler.INSTANCE.sendToAll(new MessageComposterAddLevel(pos, newLevel));
                    }
                }

            } else {
                return false;
            }
        }

        if (newLevel != level) {
            world.setBlockState(pos, state.withProperty(LEVEL, newLevel), 2);
        }

        return true;
    }

    @SideOnly(Side.CLIENT)
    public void playAddItemSound(World world, BlockPos pos)
    {
        SoundHelper.playSoundAtPos(world, pos, SoundEvents.ITEM_HOE_TILL, 1.0f, 1.0f);
    }

    @SideOnly(Side.CLIENT)
    public void playOutputSound(World world, BlockPos pos)
    {
        SoundHelper.playSoundAtPos(world, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
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
}
