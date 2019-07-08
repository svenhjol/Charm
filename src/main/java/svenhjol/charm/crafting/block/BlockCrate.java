package svenhjol.charm.crafting.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.base.GuiHandler;
import svenhjol.charm.crafting.feature.Crate;
import svenhjol.charm.crafting.tile.TileCrate;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonBlockTE;
import svenhjol.meson.helper.EntityHelper;
import svenhjol.meson.helper.SoundHelper;
import svenhjol.meson.iface.IMesonBlock;
import svenhjol.meson.iface.IMesonEnum;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockCrate extends MesonBlockTE<TileCrate> implements IMesonBlock
{
    public static PropertyEnum<WoodVariant> VARIANT = PropertyEnum.create("variant", WoodVariant.class);

    public enum Type implements IMesonEnum
    {
        CRATE,
        CRATE_SEALED
    }
    public Type type;

    public BlockCrate(Type type)
    {
        super(Material.WOOD, type.getName());
        setHardness(Crate.hardness);
        setResistance(10f);
        setSoundType(SoundType.WOOD);
        setCreativeTab(CreativeTabs.DECORATIONS);

        this.type = type;
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
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        TileCrate crate = getTileEntity(world, pos);
        
        if (!validTileEntity(crate)) {
            super.getDrops(drops, world, pos, state, fortune);
        }

        if (!isSealedCrate()) {
            dropsIntactInventory(drops, world, pos);
        }
    }

    /**
     * Sealed crates drop their contents.
     * Override the MesonBlockTE default drops so we can handle different kinds of item.
     */
    @Override
    public void doItemSpawn(ItemStack stack, World world, BlockPos pos)
    {
        if (EntityHelper.itemHasEntityTag(stack)) {

            // if entity (like spawn egg), hatch it!
            EntityHelper.spawnEntityFromItem(stack, world, pos);

        } else if (Block.getBlockFromItem(stack.getItem()) instanceof BlockTNT) {

            @SuppressWarnings("ConstantConditions") // igniter of null seems to be valid when fetched via EntityTNTPrimed::getTntPlacedBy
            EntityTNTPrimed tnt = new EntityTNTPrimed(world, (double)((float)pos.getX() + 0.5F), (double)pos.getY(), (double)((float)pos.getZ() + 0.5F), null);

            if (world.spawnEntity(tnt)) {
                SoundHelper.playSoundAtPos(world, tnt.getPosition(), SoundEvents.ENTITY_TNT_PRIMED, 1.0f, 1.0f);
            }

        } else {
            super.doItemSpawn(stack, world, pos);
        }
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

        // debug mode testing loot tables
        if (Meson.DEBUG && player.isCreative() && player.isSneaking() && player.getHeldItem(hand) == ItemStack.EMPTY) {
            setCreativeLootTable(world, pos);
            world.notifyBlockUpdate(pos, state, state, 2);
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

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, VARIANT);
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

    public boolean isSealedCrate()
    {
        return this.type == Type.CRATE_SEALED;
    }

    // debug mode for testing loot tables
    public void setCreativeLootTable(World world, BlockPos pos)
    {
        if (!world.isRemote) {

            float f = world.rand.nextFloat();
            Crate.RARITY rarity = Crate.RARITY.COMMON;
            if (f <= 0.25) {
                rarity = Crate.RARITY.RARE;
            } else if (f <= 0.5) {
                rarity = Crate.RARITY.VALUABLE;
            } else if (f <= 0.75) {
                rarity = Crate.RARITY.UNCOMMON;
            }
            TileCrate tile = getTileEntity(world, pos);
            List<Crate.CrateType> types = Crate.types.get(rarity);
            Crate.CrateType type = types.get(world.rand.nextInt(types.size()));

            int slots = tile.getInventory().getSlots();
            for (int i = 0; i < slots; i++) {
                tile.getInventory().getStackInSlot(i).setCount(0);
            }

            tile.setLootTable(type.pool);
            tile.setName(type.pool.toString());
            tile.setShowName(true);

        } else {
            for (int i = 0; i < 8; ++i) {
                double d0 = world.rand.nextGaussian() * 0.02D;
                double d1 = world.rand.nextGaussian() * 0.02D;
                double d2 = world.rand.nextGaussian() * 0.02D;
                double dx = (float)pos.getX() + MathHelper.clamp(world.rand.nextFloat(), 0.25f, 0.75f);
                double dy = (float)pos.getY() + 1.35f;
                double dz = (float)pos.getZ() + MathHelper.clamp(world.rand.nextFloat(), 0.25f, 0.75f);
                world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, dx, dy, dz, d0, d1, d2);
            }
        }
    }
}