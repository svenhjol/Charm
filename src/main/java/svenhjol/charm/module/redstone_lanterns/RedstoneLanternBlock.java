package svenhjol.charm.module.redstone_lanterns;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import svenhjol.charm.block.CharmLanternBlock;
import svenhjol.charm.enums.VanillaMetalMaterial;
import svenhjol.charm.loader.CharmModule;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
public class RedstoneLanternBlock extends CharmLanternBlock {
    public static BooleanProperty LIT = BlockStateProperties.LIT;

    public RedstoneLanternBlock(CharmModule module) {
        super(module, "redstone_lantern", VanillaMetalMaterial.IRON, Properties.copy(Blocks.LANTERN)
            .lightLevel(p -> p.getValue(BlockStateProperties.LIT) ? 15 : 0));

        registerDefaultState(defaultBlockState().setValue(LIT, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);
        if (state != null) {
            return state.setValue(LIT, ctx.getLevel().hasNeighborSignal(ctx.getClickedPos()));
        }
        return null;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);
        if (state.getValue(LIT) && !level.hasNeighborSignal(pos)) {
            level.setBlock(pos, state.cycle(LIT), 2);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (!level.isClientSide) {
            boolean flag = state.getValue(LIT);
            if (flag != level.hasNeighborSignal(pos)) {
                if (flag) {
                    level.scheduleTick(pos, this, 4);
                } else {
                    level.setBlock(pos, state.cycle(LIT), 2);
                }
            }
        }
    }
}
