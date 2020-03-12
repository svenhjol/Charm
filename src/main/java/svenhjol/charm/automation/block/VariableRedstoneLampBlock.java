package svenhjol.charm.automation.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;

import javax.annotation.Nullable;
import java.util.Random;

@SuppressWarnings("deprecation")
public class VariableRedstoneLampBlock extends MesonBlock {
    public static IntegerProperty LEVEL = IntegerProperty.create("level", 0, 15);

    public VariableRedstoneLampBlock(MesonModule module) {
        super(module, "variable_redstone_lamp", Block.Properties
            .create(Material.REDSTONE_LIGHT)
            .sound(SoundType.GLASS)
            .hardnessAndResistance(0.3F)
            .lightValue(0)
        );

        setDefaultState(this.getDefaultState().with(LEVEL, 0));
    }

    @Override
    public ItemGroup getItemGroup() {
        return ItemGroup.REDSTONE;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!world.isRemote) {
            if (state.get(LEVEL) > 0 && !world.isBlockPowered(pos)) {
                updateState(world, pos, state, 0);
            } else if (world.isBlockPowered(pos)) {
                int power = world.getRedstonePowerFromNeighbors(pos);
                updateState(world, pos, state, power);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!world.isRemote) {
            if (state.get(LEVEL) > 0 && !world.isBlockPowered(pos)) {
                world.getPendingBlockTicks().scheduleTick(pos, this, 4);
            } else if (world.isBlockPowered(pos)) {
                int power = world.isBlockPowered(pos) ? world.getRedstonePowerFromNeighbors(pos) : 0;
                updateState(world, pos, state, power);
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
        int power;

        if (!world.isRemote && state.get(LEVEL) > 0) {
            power = world.isBlockPowered(pos) ? world.getRedstonePowerFromNeighbors(pos) : 0;
            updateState(world, pos, state, power);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        int power = context.getWorld().isBlockPowered(context.getPos()) ? context.getWorld().getRedstonePowerFromNeighbors(context.getPos()) : 0;
        return this.getDefaultState().with(LEVEL, power);
    }

    @Override
    public int getLightValue(BlockState state) {
        return state.get(LEVEL);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(LEVEL);
    }

    protected void updateState(World world, BlockPos pos, BlockState state, int power) {
        world.setBlockState(pos, state.with(LEVEL, power), 2);
    }
}
