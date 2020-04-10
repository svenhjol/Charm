package svenhjol.charm.mobs.goal;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndRodBlock;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LandOnEndRodGoal extends MoveToBlockGoal {
    private final ParrotEntity parrot;

    public LandOnEndRodGoal(ParrotEntity parrot, double what) {
        super(parrot, what, 8);
        this.parrot = parrot;
    }

    @Override
    public boolean shouldExecute() {
        return this.parrot.isTamed()
            && !this.parrot.isSitting()
            && super.shouldExecute();
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        this.parrot.setSitting(false);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.parrot.setSitting(false);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getIsAboveDestination()) {
            this.parrot.setSitting(false);
        } else if (!this.parrot.isSitting()) {
            this.parrot.setSitting(false);
        }
    }

    @Override
    protected boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos) {
        if (!worldIn.isAirBlock(pos.up())) {
            return false;
        } else {
            BlockState state = worldIn.getBlockState(pos);
            Block block = state.getBlock();
            List<Direction> directions = new ArrayList<>(Arrays.asList(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST));
            boolean found = block == Blocks.END_ROD && directions.contains(state.get(EndRodBlock.FACING));
            if (found) {
                this.parrot.setPositionAndUpdate(pos.getX() + 0.5D, pos.getY() + 0.64D, pos.getZ() + 0.5D);

                // this lets parrots fly away after a random period of time, might implement as config option
                // if (this.parrot.world.rand.nextInt(1000) == 0) {
                //     this.parrot.getAISit().setSitting(false);
                //     return false;
                // }
            }
            return found;
        }
    }
}
