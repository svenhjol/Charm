package svenhjol.charm.decoration.block;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.world.IBlockReader;
import svenhjol.charm.decoration.tileentity.CustomBarrelTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.IWoodType;
import svenhjol.meson.iface.IMesonBlock;

import javax.annotation.Nullable;

public class CustomBarrelBlock extends BarrelBlock implements IMesonBlock {
    public IWoodType wood;
    protected MesonModule module;

    public CustomBarrelBlock(MesonModule module, IWoodType wood) {
        super(Block.Properties
            .create(Material.WOOD)
            .hardnessAndResistance(2.5F)
            .sound(SoundType.WOOD));

        this.module = module;
        this.wood = wood;
        register(module, "barrel_" + wood.getName());
        setDefaultState(getStateContainer().getBaseState().with(PROPERTY_FACING, Direction.NORTH).with(PROPERTY_OPEN, false));
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.DECORATIONS;
    }

    @Nullable
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new CustomBarrelTileEntity(this.wood);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (isEnabled() && group == ItemGroup.SEARCH) {
            super.fillItemGroup(group, items);
        }
    }

    @Override
    public boolean isEnabled() {
        return module.enabled;
    }
}
