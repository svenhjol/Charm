package svenhjol.charm.mixin.accessor;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HoglinBrain;
import net.minecraft.entity.mob.HoglinEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.base.iface.CharmMixin;

@Mixin(HoglinBrain.class)
@CharmMixin(required = true)
public interface HoglinBrainAccessor {
    @Invoker("avoid")
    static void invokeAvoid(HoglinEntity hoglin, LivingEntity target) {
        throw new IllegalStateException();
    }
}
