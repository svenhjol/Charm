package svenhjol.charm.module.mooblooms;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class MoobloomPlantFlowerGoal extends Goal {
    private final MoobloomEntity mob;
    private final Level level;
    private boolean planting;

    public MoobloomPlantFlowerGoal(MoobloomEntity mob) {
        this.mob = mob;
        this.level = mob.level;
    }

    @Override
    public boolean canUse() {
        if (!level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) return false;
        if (planting) return false;
        if (mob.isBaby()) return false;
        if (mob.getRandom().nextInt(1000) != 0) return false;

        BlockPos pos = mob.blockPosition();
        return level.getBlockState(pos).isAir() && level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK);
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
            BlockPos pos = mob.blockPosition();
            if (level.getBlockState(pos).isAir() && level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK)) {
                level.levelEvent(2001, pos, Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
                level.setBlock(pos, mob.getMoobloomType().getFlower(), 2);
            }
            planting = false;
        }
    }
}
