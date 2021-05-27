package svenhjol.charm.mixin.callback;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.event.AddEntityCallback;

/**
 * Be aware that this event is also fired on the server side
 * by the {@link AddEntityCallbackManagerMixin}.
 */
@Mixin(ServerWorld.class)
public class AddEntityCallbackServerMixin {

    /**
     * Fires the {@link AddEntityCallback} event when any entity
     * is added to the server world.
     */
    @Inject(
        method = "addEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/world/ServerEntityManager;addEntity(Lnet/minecraft/world/entity/EntityLike;)Z"
        )
    )
    private void hookAddEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        AddEntityCallback.EVENT.invoker().interact(entity);
    }

    /**
     * Fires the {@link AddEntityCallback} event when a player
     * joins the game on the server side.
     */
    @Inject(
        method = "addPlayer",
        at = @At("HEAD")
    )
    private void hookAddPlayer(ServerPlayerEntity player, CallbackInfo ci) {
        AddEntityCallback.EVENT.invoker().interact(player);
    }
}
