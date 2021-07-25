package svenhjol.charm.mixin.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.entity.EntityAccess;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.event.AddEntityCallback;

@Mixin(PersistentEntitySectionManager.class)
public class AddEntityManagerMixin<T extends EntityAccess> {
    /**
     * Fires the {@link AddEntityCallback} event when an entity
     * is registered and before tracking is applied.
     */
    @Inject(
        method = "addEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/SectionPos;asLong(Lnet/minecraft/core/BlockPos;)J"
        )
    )
    private void hookLoadEntity(T entity, boolean existing, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof Entity) {
            AddEntityCallback.EVENT.invoker().interact((Entity)entity);
        }
    }
}
