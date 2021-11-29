package svenhjol.charm.mixin.parrots_stay_on_shoulder;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.parrots_stay_on_shoulder.ParrotsStayOnShoulder;

@Mixin(Player.class)
public abstract class KeepShoulderEntitiesMixin extends Entity {
    @Shadow private long timeEntitySatOnShoulder;

    public KeepShoulderEntitiesMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    /**
     * Defer to {@link ParrotsStayOnShoulder#shouldParrotStayMounted}.
     * If check passes, return early so that entities do not dismount.
     */
    @Inject(
        method = "removeEntitiesOnShoulder",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookSpawnShoulderEntities(CallbackInfo ci) {
        if (ParrotsStayOnShoulder.shouldParrotStayMounted(this.level, this.timeEntitySatOnShoulder)) {
            ci.cancel();
        }
    }

}
