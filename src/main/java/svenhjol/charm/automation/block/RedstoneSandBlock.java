package svenhjol.charm.automation.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonFallingBlock;
import svenhjol.meson.iface.IMesonBlock;

@SuppressWarnings("deprecation")
public class RedstoneSandBlock extends MesonFallingBlock implements IMesonBlock {
    public RedstoneSandBlock(MesonModule module) {
        super(module, "redstone_sand", Block.Properties
            .create(Material.SAND)
            .sound(SoundType.SAND)
            .hardnessAndResistance(0.5F)
            .harvestTool(ToolType.SHOVEL)
        );
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.REDSTONE;
    }

    @Override
    public boolean canProvidePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return 15;
    }
}
