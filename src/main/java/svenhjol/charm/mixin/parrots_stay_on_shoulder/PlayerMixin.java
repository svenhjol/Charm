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
import svenhjol.charm.feature.parrots_stay_on_shoulder.ParrotsStayOnShoulder;

@Mixin(Player.class)
public abstract class PlayerMixin extends Entity {
    @Shadow
    private long timeEntitySatOnShoulder;

    public PlayerMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    /**
     * Defer to {@link ParrotsStayOnShoulder#shouldParrotStayMounted}.
     * If check passes then return early so that entities do not dismount.
     */
    @Inject(
        method = "removeEntitiesOnShoulder",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookSpawnShoulderEntities(CallbackInfo ci) {
        if (ParrotsStayOnShoulder.shouldParrotStayMounted(level(), timeEntitySatOnShoulder)) {
            ci.cancel();
        }
    }
}
