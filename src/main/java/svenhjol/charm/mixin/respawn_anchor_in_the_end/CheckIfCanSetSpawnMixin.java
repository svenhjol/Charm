package svenhjol.charm.mixin.respawn_anchor_in_the_end;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.respawn_anchor_in_the_end.RespawnAnchorInTheEnd;

@Mixin(RespawnAnchorBlock.class)
public class CheckIfCanSetSpawnMixin {
    @Inject(
        method = "canSetSpawn",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookCanSetSpawn(Level level, CallbackInfoReturnable<Boolean> cir) {
        if (RespawnAnchorInTheEnd.canSetSpawn(level)) {
            cir.setReturnValue(true);
        }
    }
}
