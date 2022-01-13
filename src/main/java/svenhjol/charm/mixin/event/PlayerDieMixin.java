package svenhjol.charm.mixin.event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.api.event.PlayerDieCallback;

@Mixin(ServerPlayer.class)
public abstract class PlayerDieMixin extends LivingEntity {
    protected PlayerDieMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Fires the {@link PlayerDieCallback} event when the player is killed.
     */
    @Inject(
        method = "die",
        at = @At("HEAD")
    )
    private void hookOnDeath(DamageSource source, CallbackInfo ci) {
        var deathMessage = getCombatTracker().getDeathMessage();
        PlayerDieCallback.EVENT.invoker().interact((ServerPlayer)(Object)this, deathMessage);
    }
}
