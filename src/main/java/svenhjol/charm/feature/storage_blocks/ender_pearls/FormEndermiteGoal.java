package svenhjol.charm.feature.storage_blocks.ender_pearls;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charmony.helper.MobHelper;

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
                BlockPos pos = getPosition(silverfish).relative(facing);
                BlockState state = silverfish.getCommandSenderWorld().getBlockState(pos);

                if (state.is(EnderPearls.block.get())) {
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
        var level = silverfish.getCommandSenderWorld();
        if (level.isClientSide()) return;

        var serverLevel = (ServerLevel)level;
        var pos = getPosition(silverfish);

        if (facing == null) return;

        var relative = pos.relative(facing);
        var state = level.getBlockState(relative);
        var entityPos = new BlockPos((int) (relative.getX() + 0.5), (int) (relative.getY() + 0.5), (int) (relative.getZ() + 0.5));

        if (state.is(EnderPearls.block.get())) {
             MobHelper.spawn(EntityType.ENDERMITE, serverLevel, entityPos, MobSpawnType.CONVERSION, m -> {
                level.removeBlock(relative, false);
                silverfish.spawnAnim();
                silverfish.discard();
                EnderPearls.triggerConvertedSilverfishForNearbyPlayers((ServerLevel)level, pos);
            });
        }
    }

    private BlockPos getPosition(Silverfish silverfish) {
        var pos = silverfish.blockPosition();
        return new BlockPos(pos.getX(), (int) (pos.getY() + 0.5D), pos.getZ());
    }
}
