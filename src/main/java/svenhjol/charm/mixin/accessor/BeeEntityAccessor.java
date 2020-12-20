package svenhjol.charm.mixin.accessor;

import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BeeEntity.class)
public interface BeeEntityAccessor {
    @Invoker
    void invokeSetHasNectar(boolean hasNectar);

    @Invoker
    void invokeStartMovingTo(BlockPos pos);
}
