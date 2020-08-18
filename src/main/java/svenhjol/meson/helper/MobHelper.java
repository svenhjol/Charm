package svenhjol.meson.helper;

import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.mob.MobEntity;
import svenhjol.charm.mixin.accessor.GoalSelectorAccessor;
import svenhjol.charm.mixin.accessor.MobEntityAccessor;

import java.util.Set;

public class MobHelper {
    public static Set<PrioritizedGoal> getGoals(MobEntity mob) {
        return ((GoalSelectorAccessor)getGoalSelector(mob)).getGoals();
    }

    public static GoalSelector getGoalSelector(MobEntity mob) {
        return ((MobEntityAccessor)mob).getGoalSelector();
    }
}
