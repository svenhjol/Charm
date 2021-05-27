package svenhjol.charm.mixin.accessor;

import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

import java.util.Set;

@Mixin(GoalSelector.class)
@CharmMixin(required = true)
public interface GoalSelectorAccessor {
    @Accessor()
    Set<PrioritizedGoal> getGoals();
}
