package svenhjol.charm.mixin;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.AddEntityCallback;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    /**
     * Calls our AddEntityCallback event when a player
     * joins the game on the client side.
     *
     * Inspired by Forge's EntityJoinWorldEvent which
     * provides a hook at the same point in the code,
     * except allows it to be cancellable.
     */
    @Inject(
        method = "addEntityPrivate",
        at = @At("HEAD")
    )
    private void hookAddEntityPrivate(int id, Entity entity, CallbackInfo ci) {
        AddEntityCallback.EVENT.invoker().interact(entity);
    }
}
