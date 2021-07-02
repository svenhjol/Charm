package svenhjol.charm.module.mooblooms;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import svenhjol.charm.helper.DebugHelper;
import svenhjol.charm.mixin.accessor.BeeAccessor;

import java.util.List;
import java.util.function.Predicate;

public class BeeMoveToMoobloomGoal extends Goal {
    private static final int MAX_MOVE_TICKS = 1200;
    private static final int RANGE = 24;

    private final Bee bee;
    private final Level world;
    private MoobloomEntity moobloom = null;
    private int moveTicks;
    private int lastTried = 0;

    public BeeMoveToMoobloomGoal(Bee bee) {
        this.bee = bee;
        this.world = bee.level;
    }

    @Override
    public boolean canUse() {
        if (bee.hasNectar()) {
            if (--lastTried <= 0)
                return true;
        }

        return false;
    }

    @Override
    public void start() {
        moobloom = null;
        moveTicks = 0;
        bee.resetTicksWithoutNectarSinceExitingHive();

        AABB box = bee.getBoundingBox().inflate(RANGE, RANGE / 2.0, RANGE);
        Predicate<MoobloomEntity> selector = entity -> !entity.isPollinated() && entity.isAlive();
        List<MoobloomEntity> entities = world.getEntitiesOfClass(MoobloomEntity.class, box, selector);

        if (entities.size() > 0) {
            moobloom = entities.get(world.random.nextInt(entities.size()));
            bee.setStayOutOfHiveCountdown(MAX_MOVE_TICKS);
        } else {
            lastTried = 200;
        }

        super.start();
    }

    @Override
    public void stop() {
        moveTicks = 0;
        moobloom = null;
        bee.getNavigation().stop();
        bee.getNavigation().resetMaxVisitedNodesMultiplier();
    }

    @Override
    public boolean canContinueToUse() {
        return moobloom != null && moobloom.isAlive() && moveTicks < MAX_MOVE_TICKS;
    }

    @Override
    public void tick() {
        moveTicks++;

        if (moobloom == null || !moobloom.isAlive())
            return;

        if (moveTicks > MAX_MOVE_TICKS) {
            moobloom = null;
        } else if (!bee.getNavigation().isInProgress()) {
            ((BeeAccessor) bee).invokePathfindRandomlyTowards(moobloom.blockPosition());

            if (DebugHelper.isDebugMode())
                bee.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100));
        } else {

            // update bee tracking to take into account a moving moobloom
            if (moveTicks % 50 == 0)
                ((BeeAccessor) bee).invokePathfindRandomlyTowards(moobloom.blockPosition());

            double dist = bee.position().distanceTo(moobloom.position());
            if (dist < 2.2) {
                ((BeeAccessor)bee).invokeSetHasNectar(false);

                if (DebugHelper.isDebugMode())
                    bee.removeEffect(MobEffects.GLOWING);

                moobloom.pollinate();
                moobloom = null;
            }
        }
    }
}
