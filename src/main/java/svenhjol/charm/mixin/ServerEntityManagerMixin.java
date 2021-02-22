package svenhjol.charm.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerEntityManager;
import net.minecraft.world.entity.EntityLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.event.AddEntityCallback;

@Mixin(ServerEntityManager.class)
public class ServerEntityManagerMixin<T extends EntityLike> {
    @Inject(
        method = "addEntity(Lnet/minecraft/world/entity/EntityLike;Z)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/math/ChunkSectionPos;toLong(Lnet/minecraft/util/math/BlockPos;)J"
        )
    )
    private void hookLoadEntity(T entity, boolean existing, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof Entity) {
            AddEntityCallback.EVENT.invoker().interact((Entity)entity);
        }
    }
}
