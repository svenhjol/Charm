package svenhjol.charm.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.PlayerTickCallback;
import svenhjol.charm.module.vanillachanges.ParrotsStayOnShoulder;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity {
    @Shadow private long shoulderEntityAddedTime;

    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
        method = "dropShoulderEntities",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookSpawnShoulderEntities(CallbackInfo ci) {
        if (ParrotsStayOnShoulder.shouldParrotStayMounted(this.world, this.shoulderEntityAddedTime))
            ci.cancel();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void hookTick(CallbackInfo ci) {
        PlayerTickCallback.EVENT.invoker().interact((PlayerEntity)(Object)this);
    }
}
