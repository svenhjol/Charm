package svenhjol.charm.entity.goal;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import svenhjol.charm.entity.MoobloomEntity;
import svenhjol.charm.mixin.accessor.BeeEntityAccessor;
import svenhjol.charm.module.Core;

import java.util.List;
import java.util.function.Predicate;

public class BeeMoveToMoobloomGoal extends Goal {
    private static final int MAX_MOVE_TICKS = 1200;
    private static final int RANGE = 24;

    private final BeeEntity bee;
    private final World world;
    private MoobloomEntity moobloom = null;
    private int moveTicks;
    private int lastTried = 0;

    public BeeMoveToMoobloomGoal(BeeEntity bee) {
        this.bee = bee;
        this.world = bee.world;
    }

    @Override
    public boolean canStart() {
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
        bee.resetPollinationTicks();

        Box box = bee.getBoundingBox().expand(RANGE, RANGE / 2.0, RANGE);
        Predicate<MoobloomEntity> selector = entity -> !entity.isPollinated() && entity.isAlive();
        List<MoobloomEntity> entities = world.getEntitiesByClass(MoobloomEntity.class, box, selector);

        if (entities.size() > 0) {
            moobloom = entities.get(world.random.nextInt(entities.size()));
            bee.setCannotEnterHiveTicks(MAX_MOVE_TICKS);
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
        bee.getNavigation().resetRangeMultiplier();
    }

    @Override
    public boolean shouldContinue() {
        return moobloom != null && moobloom.isAlive() && moveTicks < MAX_MOVE_TICKS;
    }

    @Override
    public void tick() {
        moveTicks++;

        if (moobloom == null || !moobloom.isAlive())
            return;

        if (moveTicks > MAX_MOVE_TICKS) {
            moobloom = null;
        } else if (!bee.getNavigation().isFollowingPath()) {
            ((BeeEntityAccessor) bee).invokeStartMovingTo(moobloom.getBlockPos());

            if (Core.debug)
                bee.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 100));
        } else {

            // update bee tracking to take into account a moving moobloom
            if (moveTicks % 50 == 0)
                ((BeeEntityAccessor) bee).invokeStartMovingTo(moobloom.getBlockPos());

            double dist = bee.getPos().distanceTo(moobloom.getPos());
            if (dist < 2.2) {
                ((BeeEntityAccessor)bee).invokeSetHasNectar(false);

                if (Core.debug)
                    bee.removeStatusEffect(StatusEffects.GLOWING);

                moobloom.pollinate();
                moobloom = null;
            }
        }
    }
}
