package svenhjol.charm.automation.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import svenhjol.charm.decoration.block.BaseLanternBlock;
import svenhjol.meson.MesonModule;

import javax.annotation.Nullable;
import java.util.Random;

public class RedstoneLanternBlock extends BaseLanternBlock {
    public static BooleanProperty LIT = BlockStateProperties.LIT;

    public RedstoneLanternBlock(MesonModule module) {
        super(module, "redstone_lantern", Block.Properties.from(Blocks.LANTERN)
            .lightValue(0));

        this.setDefaultState(getDefaultState().with(LIT, false));
    }

    @Override
    public int tickRate(IWorldReader worldIn) {
        return 2;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state != null) {
            return state.with(LIT, context.getWorld().isBlockPowered(context.getPos()));
        }
        return null;
    }

    @Override
    public void onEndFalling(World worldIn, BlockPos pos, BlockState fallingState, BlockState hitState) {
        super.onEndFalling(worldIn, pos, fallingState, hitState);
        if (worldIn.isBlockPowered(pos)) {
            BlockState state = worldIn.getBlockState(pos);
            worldIn.setBlockState(pos, state.with(LIT, true), 2);
        }
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isRemote) {
            boolean flag = state.get(LIT);
            if (flag != worldIn.isBlockPowered(pos)) {
                if (flag) {
                    worldIn.getPendingBlockTicks().scheduleTick(pos, this, 4);
                } else {
                    worldIn.setBlockState(pos, state.cycle(LIT), 2);
                }
            }
        }
    }

    @Override
    public int getLightValue(BlockState state) {
        return state.get(LIT) ? 15 : 0;
    }

    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        super.tick(state, worldIn, pos, rand);
        if (state.get(LIT) && !worldIn.isBlockPowered(pos)) {
            worldIn.setBlockState(pos, state.cycle(LIT), 2);
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(LIT);
    }

    @Override
    public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity) {
        return true;
    }

    private void checkLit(BlockState state, ServerWorld worldIn, BlockPos pos) {

    }
}
