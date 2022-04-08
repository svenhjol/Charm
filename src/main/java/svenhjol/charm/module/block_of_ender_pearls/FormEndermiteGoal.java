package svenhjol.charm.module.block_of_ender_pearls;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.helper.MobHelper;

import java.util.EnumSet;

public class FormEndermiteGoal extends RandomStrollGoal {
    private final Silverfish silverfish;
    private Direction facing;
    private boolean merge;

    public FormEndermiteGoal(Silverfish silverfish) {
        super(silverfish, 0.6D);
        this.silverfish = silverfish;
        setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!silverfish.getCommandSenderWorld().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            return false;
        } else if (silverfish.getTarget() != null) {
            return false;
        } else if (!silverfish.getNavigation().isDone()) {
            return false;
        } else {
            var random = silverfish.getRandom();

            if (random.nextFloat() < 0.8D) {
                facing = Direction.getRandom(random);
                BlockPos pos = getSilverfishPosition(silverfish).relative(facing);
                BlockState state = silverfish.getCommandSenderWorld().getBlockState(pos);

                if (state.getBlock() == BlockOfEnderPearls.ENDER_PEARL_BLOCK) {
                    merge = true;
                    return true;
                }
            }

            merge = false;
            return super.canUse();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !merge && super.canContinueToUse();
    }

    @Override
    public void start() {
        Level level = silverfish.getCommandSenderWorld();
        if (level.isClientSide)
            return;

        BlockPos silverfishPos = getSilverfishPosition(silverfish);
        if (facing == null)
            return;

        BlockPos pos = silverfishPos.relative(facing);
        BlockState state = level.getBlockState(pos);
        BlockPos entityPos = new BlockPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);

        if (state.getBlock() == BlockOfEnderPearls.ENDER_PEARL_BLOCK) {
             MobHelper.spawn(EntityType.ENDERMITE, (ServerLevel)level, entityPos, MobSpawnType.CONVERSION, m -> {
                level.removeBlock(pos, false);
                silverfish.spawnAnim();
                silverfish.discard();
                BlockOfEnderPearls.triggerConvertedSilverfishForNearbyPlayers((ServerLevel)level, pos);
            });
        }
    }

    private BlockPos getSilverfishPosition(Silverfish silverfishEntity) {
        BlockPos entityPos = silverfishEntity.blockPosition();
        return new BlockPos(entityPos.getX(), entityPos.getY() + 0.5D, entityPos.getZ());
    }
}
