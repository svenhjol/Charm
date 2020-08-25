package svenhjol.charm.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.WanderingTraderManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.module.WanderingTraderImprovements;

import java.util.Random;

@Mixin(WanderingTraderManager.class)
public class WanderingTraderManagerMixin {
    @Inject(
        method = "method_18018",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/server/world/ServerWorld;getPointOfInterestStorage()Lnet/minecraft/world/poi/PointOfInterestStorage;"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        cancellable = true
    )
    private void hookTraderSpawn(ServerWorld serverWorld, CallbackInfoReturnable<Boolean> cir, PlayerEntity player) {
        if (!WanderingTraderImprovements.checkSpawnConditions(serverWorld, player.getBlockPos()))
            cir.setReturnValue(false);
    }

    @Redirect(
        method = "method_18018",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Random;nextInt(I)I"
        )
    )
    private int hookRandomCheck(Random random, int i) {
        return WanderingTraderImprovements.shouldSpawnFrequently() ? 0 : 10; // 10 is vanilla random value
    }
}
