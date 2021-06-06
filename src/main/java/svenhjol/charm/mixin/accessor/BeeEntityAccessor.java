package svenhjol.charm.mixin.accessor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(Bee.class)
@CharmMixin(required = true)
public interface BeeEntityAccessor {
    @Invoker
    void invokeSetHasNectar(boolean hasNectar);

    @Invoker
    void invokeStartMovingTo(BlockPos pos);
}
