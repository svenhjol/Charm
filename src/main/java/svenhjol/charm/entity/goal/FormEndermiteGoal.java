package svenhjol.charm.entity.goal;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import svenhjol.charm.module.BlockOfEnderPearls;
import svenhjol.charm.base.helper.MobHelper;

import java.util.EnumSet;
import java.util.Random;

public class FormEndermiteGoal extends WanderAroundGoal {
    private final SilverfishEntity silverfish;
    private Direction facing;
    private boolean merge;

    public FormEndermiteGoal(SilverfishEntity silverfish) {
        super(silverfish, 0.6D);
        this.silverfish = silverfish;
        setControls(EnumSet.of(Goal.Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (!silverfish.getEntityWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
            return false;
        } else if (silverfish.getTarget() != null) {
            return false;
        } else if (!silverfish.getNavigation().isIdle()) {
            return false;
        } else {
            Random random = silverfish.getRandom();

            if (random.nextFloat() < 0.8D) {
                facing = Direction.random(random);
                BlockPos pos = getSilverfishPosition(silverfish).offset(facing);
                BlockState state = silverfish.getEntityWorld().getBlockState(pos);

                if (state.getBlock() == BlockOfEnderPearls.ENDER_PEARL_BLOCK) {
                    merge = true;
                    return true;
                }
            }

            merge = false;
            return super.canStart();
        }
    }

    @Override
    public boolean shouldContinue() {
        return !merge && super.shouldContinue();
    }

    @Override
    public void start() {
        World world = silverfish.getEntityWorld();
        if (world.isClient)
            return;

        BlockPos silverfishPos = getSilverfishPosition(silverfish);
        if (facing == null)
            return;

        BlockPos pos = silverfishPos.offset(facing);
        BlockState state = world.getBlockState(pos);
        BlockPos entityPos = new BlockPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

        if (state.getBlock() == BlockOfEnderPearls.ENDER_PEARL_BLOCK) {
            EndermiteEntity endermite = MobHelper.spawn(EntityType.ENDERMITE, (ServerWorld)world, entityPos, SpawnReason.CONVERSION);

            if (endermite != null) {
                world.removeBlock(pos, false);
                silverfish.playSpawnEffects();
                silverfish.discard();
                world.spawnEntity(endermite);
            }
        }
    }

    private BlockPos getSilverfishPosition(SilverfishEntity silverfishEntity) {
        BlockPos entityPos = silverfishEntity.getBlockPos();
        return new BlockPos(entityPos.getX(), entityPos.getY() + 0.5D, entityPos.getZ());
    }
}
