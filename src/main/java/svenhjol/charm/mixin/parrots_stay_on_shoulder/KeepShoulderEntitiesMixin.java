package svenhjol.charm.mixin.parrots_stay_on_shoulder;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.parrots_stay_on_shoulder.ParrotsStayOnShoulder;

@Mixin(PlayerEntity.class)
public abstract class KeepShoulderEntitiesMixin extends Entity {
    @Shadow private long shoulderEntityAddedTime;

    public KeepShoulderEntitiesMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * Defer to {@link ParrotsStayOnShoulder#shouldParrotStayMounted}.
     * If check passes, return early so that entities do not dismount.
     */
    @Inject(
        method = "dropShoulderEntities",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookSpawnShoulderEntities(CallbackInfo ci) {
        if (ParrotsStayOnShoulder.shouldParrotStayMounted(this.world, this.shoulderEntityAddedTime))
            ci.cancel();
    }

}
