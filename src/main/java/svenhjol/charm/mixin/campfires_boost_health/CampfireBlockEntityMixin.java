package svenhjol.charm.mixin.campfires_boost_health;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.feature.campfires_boost_health.CampfiresBoostHealth;

@Mixin(CampfireBlockEntity.class)
public class CampfireBlockEntityMixin {
    @Inject(
        method = "cookTick",
        at = @At("HEAD")
    )
    private static void hookCookTick(Level level, BlockPos pos, BlockState blockState, CampfireBlockEntity campfireBlockEntity, CallbackInfo ci) {
        CampfiresBoostHealth.tryRegeneratePlayersAroundFire(level, pos);
    }
}
