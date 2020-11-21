package svenhjol.charm.entity.goal;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import svenhjol.charm.entity.MoobloomEntity;

public class MoobloomPlantFlowerGoal extends Goal {
    private final MoobloomEntity mob;
    private final World world;
    private boolean planting;

    public MoobloomPlantFlowerGoal(MoobloomEntity mob) {
        this.mob = mob;
        this.world = mob.world;
    }

    @Override
    public boolean canStart() {
        if (!world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING))
            return false;

        if (planting)
            return false;

        if (mob.isBaby())
            return false;

        if (mob.getRandom().nextInt(1000) != 0)
            return false;

        BlockPos pos = mob.getBlockPos();
        return world.getBlockState(pos).isAir() && world.getBlockState(pos.down()).isOf(Blocks.GRASS_BLOCK);
    }

    @Override
    public void start() {
        this.planting = true;
    }

    @Override
    public void stop() {
        this.planting = false;
    }

    @Override
    public void tick() {
        if (planting) {
            BlockPos pos = mob.getBlockPos();
            if (world.getBlockState(pos).isAir() && world.getBlockState(pos.down()).isOf(Blocks.GRASS_BLOCK)) {
                world.syncWorldEvent(2001, pos, Block.getRawIdFromState(Blocks.GRASS_BLOCK.getDefaultState()));
                world.setBlockState(pos, mob.getMoobloomType().getFlower(), 2);
            }
            planting = false;
        }
    }
}
