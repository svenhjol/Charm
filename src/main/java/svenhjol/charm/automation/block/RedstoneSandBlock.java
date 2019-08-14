package svenhjol.charm.automation.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import svenhjol.charm.Charm;
import svenhjol.meson.iface.IMesonBlock;

public class RedstoneSandBlock extends FallingBlock implements IMesonBlock
{
    public RedstoneSandBlock()
    {
        super(Block.Properties
            .create(Material.SAND)
            .sound(SoundType.SAND)
            .hardnessAndResistance(0.5F)
        );

        register(new ResourceLocation(Charm.MOD_ID, "redstone_sand"));
    }

    @Override
    public ItemGroup getItemGroup()
    {
        return ItemGroup.REDSTONE;
    }

    @Override
    public boolean canProvidePower(BlockState state)
    {
        return true;
    }

    @Override
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side)
    {
        return 15;
    }
}
