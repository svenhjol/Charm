package svenhjol.charm.crafting.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.base.GuiHandler;
import svenhjol.charm.crafting.feature.BookshelfChest;
import svenhjol.charm.crafting.tile.TileBookshelfChest;
import svenhjol.meson.MesonBlockTE;
import svenhjol.meson.iface.IMesonBlock;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockBookshelfChest extends MesonBlockTE<TileBookshelfChest> implements IMesonBlock
{
    public static final PropertyInteger SLOTS = PropertyInteger.create("slots", 0, 9);

    public BlockBookshelfChest()
    {
        super(Material.WOOD, "bookshelf_chest");
        setHardness(BookshelfChest.hardness);
        setSoundType(SoundType.WOOD);
        setCreativeTab(CreativeTabs.DECORATIONS);
        setDefaultState(getBlockState().getBaseState().withProperty(SLOTS, 0));
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        ItemStack thisChest = new ItemStack(this, 1, 0);

        TileBookshelfChest chest = getTileEntity(world, pos);
        if (validTileEntity(chest)) {
            dropsInventory(chest, TileBookshelfChest.SIZE, (World)world, pos);
            thisChest.setStackDisplayName(chest.getName());
        }
        // drop the chest after dropping the inventory
        drops.add(thisChest);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        TileBookshelfChest chest = getTileEntity(worldIn, pos);
        if (validTileEntity(chest)) {
            chest.setName(stack.getDisplayName());
        }
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!world.isRemote) {

            TileBookshelfChest bookshelf = getTileEntity(world, pos);
            if (!validTileEntity(bookshelf)) return false;
            if (player.isSneaking()) return false;

            if (bookshelf.hasLootTable()) {
                bookshelf.generateLoot(player);
            }

            player.openGui(Charm.instance, GuiHandler.BOOKSHELF_CHEST, world, pos.getX(), pos.getY(), pos.getZ());
        }

        if (world.isRemote) {
            player.playSound(CharmSounds.BOOKSHELF_OPEN, 1.0f, 1.0f);
        }
        return true;
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
    public Class<TileBookshelfChest> getTileEntityClass()
    {
        return TileBookshelfChest.class;
    }

    @Nullable
    @Override
    public TileBookshelfChest createTileEntity(World world, IBlockState state)
    {
        return new TileBookshelfChest();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, SLOTS);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(SLOTS);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(SLOTS, meta & 9);
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        int slots = this.getNumberOfOccupiedSlots(worldIn, pos);
        return state.withProperty(SLOTS, slots);
    }

    @Override
    public float getEnchantPowerBonus(World world, BlockPos pos)
    {
        return this.getNumberOfOccupiedSlots(world, pos) > 0 ? 1 : 0;
    }

    @Override
    @Nullable
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.BOOK;
    }

    @Override
    public int quantityDropped(Random random)
    {
        return 3;
    }

    private int getNumberOfOccupiedSlots(IBlockAccess world, BlockPos pos)
    {
        TileBookshelfChest chest = (TileBookshelfChest) world.getTileEntity(pos);
        return getNumberOfOccupiedSlots(chest);
    }

    private int getNumberOfOccupiedSlots(TileBookshelfChest chest)
    {
        int slots = 0;

        if (validTileEntity(chest) && chest != null) {
            slots = chest.getNumberOfFilledSlots();
        }

        return slots;
    }
}
