package svenhjol.charm.mixin.callback;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.AddEntityCallback;

@Mixin(ClientLevel.class)
public class AddEntityCallbackClientMixin {
    /**
     * Fires the {@link AddEntityCallback} event when an entity (e.g. the player)
     * is added to the *client* world.
     *
     * See {@link AddEntityCallbackServerMixin} for the server-side version of this event.
     *
     * It is cancellable if ActionResult != PASS.
     *
     * If you need the entity's spawn packet (lower-level than this event) then use
     * {@link svenhjol.charm.event.ClientSpawnEntityCallback}.
     *
     * Inspired by Forge's EntityJoinWorldEvent which provides a hook at the same point
     * in the code, except allows it to be cancellable.
     */
    @Inject(
        method = "addEntity",
        at = @At("HEAD")
    )
    private void hookAddEntityPrivate(int id, Entity entity, CallbackInfo ci) {
        AddEntityCallback.EVENT.invoker().interact(entity);
    }
}
