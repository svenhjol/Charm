package svenhjol.charm.mixin.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.event.AddEntityEvent;

/**
 * Be aware that this event is also fired on the server side
 * by the {@link AddEntityEventManagerMixin}.
 */
@Mixin(ServerLevel.class)
public class AddEntityEventServerMixin {
    /**
     * Fires the {@link AddEntityEvent} event when any entity
     * is added to the server world.
     */
    @Inject(
        method = "addEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/entity/PersistentEntitySectionManager;addNewEntity(Lnet/minecraft/world/level/entity/EntityAccess;)Z"
        )
    )
    private void hookAddEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        AddEntityEvent.EVENT.invoker().interact(entity);
    }

    /**
     * Fires the {@link AddEntityEvent} event when a player
     * joins the game on the server side.
     */
    @Inject(
        method = "addPlayer",
        at = @At("HEAD")
    )
    private void hookAddPlayer(ServerPlayer player, CallbackInfo ci) {
        AddEntityEvent.EVENT.invoker().interact(player);
    }
}
