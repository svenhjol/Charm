package svenhjol.charm.building.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import svenhjol.charm.building.module.BlockOfMoonstone;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;

import java.util.Random;

public class MoonstoneBlock extends MesonBlock
{
    public static IntegerProperty LEVEL = IntegerProperty.create("level", 0, 15);

    public MoonstoneBlock(MesonModule module, DyeColor color)
    {
        super(module, "moonstone_block_" + color.getName(), Block.Properties
            .create(Material.ROCK)
            .hardnessAndResistance(0.8F)
            .harvestTool(ToolType.PICKAXE)
            .lightValue(0)
            .tickRandomly()
        );

        setDefaultState(this.getDefaultState().with(LEVEL, 0));
    }

    @Override
    public ItemGroup getItemGroup()
    {
        return ItemGroup.BUILDING_BLOCKS;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (!world.isRemote) {
            updateState(world, pos, state);
        }
    }

    @Override
    public int getLightValue(BlockState state)
    {
        return state.get(LEVEL);
    }

    @Override
    public int getLightValue(BlockState state, IEnviromentBlockReader world, BlockPos pos)
    {
        return state.get(LEVEL);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        super.fillStateContainer(builder);
        builder.add(LEVEL);
    }

    @Override
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random)
    {
        updateState(worldIn, pos, state);
    }

    protected void updateState(World world, BlockPos pos, BlockState state)
    {
        if (BlockOfMoonstone.moonglow) {
            int level = (int) (world.getCurrentMoonPhaseFactor() * 15.0);
            world.setBlockState(pos, state.with(LEVEL, level), 2);
        }
    }
}
