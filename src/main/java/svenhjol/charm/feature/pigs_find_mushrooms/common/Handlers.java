package svenhjol.charm.feature.pigs_find_mushrooms.common;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import svenhjol.charm.feature.pigs_find_mushrooms.PigsFindMushrooms;
import svenhjol.charm.foundation.feature.FeatureHolder;

import java.util.UUID;
import java.util.WeakHashMap;

public final class Handlers extends FeatureHolder<PigsFindMushrooms> {
    /**
     * Tracks the animation ticks for a pig UUID.
     */
    public final WeakHashMap<UUID, Integer> animationTicks = new WeakHashMap<>();

    public Handlers(PigsFindMushrooms feature) {
        super(feature);
    }

    public void entityJoin(Entity entity, Level level) {
        if (entity instanceof Pig pig) {
            var goalSelector = pig.goalSelector;
            if (goalSelector.getAvailableGoals().stream().noneMatch(
                g -> g.getGoal() instanceof FindMushroomGoal)) {
                goalSelector.addGoal(3, new FindMushroomGoal(pig));
            }
        }
    }

    public float getHeadEatPositionScale(Pig pig, float f) {
        var tick = animationTicks.getOrDefault(pig.getUUID(), 0);
        if (tick <= 0) {
            return 0;
        }
        if (tick >= 4 && tick <= 36) {
            return 1;
        }
        if (tick < 4) {
            return ((float)tick - f) / 4.0f;
        }
        return -((float)(tick - 40) - f) / 4.0f;
    }

    public float getHeadEatAngleScale(Pig pig, float f) {
        var tick = animationTicks.getOrDefault(pig.getUUID(), 0);
        if (tick > 4 && tick <= 36) {
            float g = ((float)(tick - 4) - f) / 32.0f;
            return 0.63f + 0.22f * Mth.sin(g * 28.7f);
        }
        if (tick > 0) {
            return 0.63f;
        }
        return pig.getXRot() * ((float)Math.PI / 180);
    }

}
