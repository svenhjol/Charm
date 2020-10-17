package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.base.CharmModule;

import java.util.Random;

public class RedstoneLanternBlock extends BaseLanternBlock {
    public static BooleanProperty LIT = Properties.LIT;

    public RedstoneLanternBlock(CharmModule module) {
        super(module, "redstone_lantern", AbstractBlock.Settings.copy(Blocks.LANTERN)
            .luminance(p -> p.get(Properties.LIT) ? 15 : 0));

        this.setDefaultState(this.getDefaultState().with(LIT, false));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        if (state != null)
            return state.with(LIT, ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));

        return null;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        if (state.get(LIT) && !world.isReceivingRedstonePower(pos))
            world.setBlockState(pos, state.cycle(LIT), 2);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(LIT);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (!world.isClient) {
            boolean flag = state.get(LIT);
            if (flag != world.isReceivingRedstonePower(pos)) {
                if (flag) {
                    world.getBlockTickScheduler().schedule(pos, this, 4);
                } else {
                    world.setBlockState(pos, state.cycle(LIT), 2);
                }
            }
        }
    }
}
