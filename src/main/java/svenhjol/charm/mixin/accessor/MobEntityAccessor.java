package svenhjol.charm.mixin.accessor;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(Mob.class)
@CharmMixin(required = true)
public interface MobEntityAccessor {
    @Accessor()
    GoalSelector getGoalSelector();
}
