package svenhjol.charm.mixin.accessor;

import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(MobEntity.class)
@CharmMixin(required = true)
public interface MobEntityAccessor {
    @Accessor()
    GoalSelector getGoalSelector();
}
