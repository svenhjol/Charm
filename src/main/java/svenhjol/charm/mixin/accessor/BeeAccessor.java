package svenhjol.charm.mixin.accessor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Bee;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Bee.class)
public interface BeeAccessor {
    @Invoker
    void invokeSetHasNectar(boolean hasNectar);

    @Invoker
    void invokePathfindRandomlyTowards(BlockPos pos);
}
