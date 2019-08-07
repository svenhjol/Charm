package svenhjol.charm.crafting.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.tileentity.CrateTileEntity;
import svenhjol.meson.MesonBlockTE;
import svenhjol.meson.enums.WoodType;

import javax.annotation.Nullable;

public abstract class CrateBaseBlock extends MesonBlockTE<CrateTileEntity>
{
    private WoodType wood;

    public CrateBaseBlock(String baseName, WoodType wood)
    {
        super(baseName, Block.Properties.create(Material.WOOD).hardnessAndResistance(1.5F).sound(SoundType.WOOD));
        this.wood = wood;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world)
    {
        return new CrateTileEntity();
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face)
    {
        return 50;
    }

    @Override
    public ItemGroup getItemGroup()
    {
        return ItemGroup.DECORATIONS;
    }

    @Override
    public int getMaxStackSize()
    {
        return 1;
    }

    @Override
    public boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face)
    {
        return true;
    }

    @Override
    public PushReaction getPushReaction(BlockState state)
    {
        return PushReaction.NORMAL;
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState state, World world, BlockPos pos)
    {
        return Container.calcRedstoneFromInventory((IInventory)world.getTileEntity(pos));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack)
    {
        if (stack.hasDisplayName()) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof CrateTileEntity) {
                ((CrateTileEntity) tile).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
    {
        if (state.getBlock() != newState.getBlock()) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof CrateTileEntity) {
                world.updateComparatorOutputLevel(pos, state.getBlock());
            }
            super.onReplaced(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state)
    {
        ItemStack stack = super.getItem(world, pos, state);
        CrateTileEntity crate = (CrateTileEntity)world.getTileEntity(pos);
        CompoundNBT tag = crate.writeToNBT(new CompoundNBT());
        if (!tag.isEmpty()) {
            stack.setTagInfo("BlockEntityTag", tag);
        }
        return stack;
    }

    public WoodType getWood()
    {
        return this.wood;
    }
}
