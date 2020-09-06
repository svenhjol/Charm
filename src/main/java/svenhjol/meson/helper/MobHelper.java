package svenhjol.meson.helper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import svenhjol.meson.mixin.accessor.GoalSelectorAccessor;
import svenhjol.meson.mixin.accessor.MobEntityAccessor;

import java.util.Set;

public class MobHelper {
    public static Set<PrioritizedGoal> getGoals(MobEntity mob) {
        return ((GoalSelectorAccessor)getGoalSelector(mob)).getGoals();
    }

    public static GoalSelector getGoalSelector(MobEntity mob) {
        return ((MobEntityAccessor)mob).getGoalSelector();
    }

    public static <T extends Entity> T spawn(EntityType<T> type, ServerWorld world, BlockPos pos, SpawnReason reason) {
        return type.create(world, null, null, null, pos, reason, false, false);
    }
}
