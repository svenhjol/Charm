package svenhjol.charm.mixin.feature.campfires_heal_players;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.feature.campfires_heal_players.CampfiresHealPlayers;
import svenhjol.charm.foundation.Resolve;

@Mixin(CampfireBlockEntity.class)
public class CampfireBlockEntityMixin {
    @Inject(
        method = "cookTick",
        at = @At("HEAD")
    )
    private static void hookCookTick(Level level, BlockPos pos, BlockState blockState, CampfireBlockEntity campfireBlockEntity, CallbackInfo ci) {
        Resolve.feature(CampfiresHealPlayers.class).handlers.tryRegeneratePlayersAroundFire(level, pos);
    }
}
