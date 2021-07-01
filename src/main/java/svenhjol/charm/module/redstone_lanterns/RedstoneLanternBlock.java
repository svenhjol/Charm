package svenhjol.charm.module.redstone_lanterns;

import svenhjol.charm.loader.CharmCommonModule;
import svenhjol.charm.block.CharmLanternBlock;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class RedstoneLanternBlock extends CharmLanternBlock {
    public static BooleanProperty LIT = BlockStateProperties.LIT;

    public RedstoneLanternBlock(CharmCommonModule module) {
        super(module, "redstone_lantern", BlockBehaviour.Properties.copy(Blocks.LANTERN)
            .lightLevel(p -> p.getValue(BlockStateProperties.LIT) ? 15 : 0));

        this.registerDefaultState(this.defaultBlockState().setValue(LIT, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        if (state != null)
            return state.setValue(LIT, ctx.getLevel().hasNeighborSignal(ctx.getClickedPos()));

        return null;
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        super.tick(state, world, pos, random);
        if (state.getValue(LIT) && !world.hasNeighborSignal(pos))
            world.setBlock(pos, state.cycle(LIT), 2);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (!world.isClientSide) {
            boolean flag = state.getValue(LIT);
            if (flag != world.hasNeighborSignal(pos)) {
                if (flag) {
                    world.getBlockTicks().scheduleTick(pos, this, 4);
                } else {
                    world.setBlock(pos, state.cycle(LIT), 2);
                }
            }
        }
    }
}
