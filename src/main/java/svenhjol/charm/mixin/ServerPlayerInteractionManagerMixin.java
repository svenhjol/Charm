package svenhjol.charm.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.event.PlayerBreakBlockCallback;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @Shadow public ServerPlayerEntity player;

    @Shadow public ServerWorld world;

    @Shadow private GameMode gameMode;

    @Inject(
        method = "tryBreakBlock",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookTryBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        ActionResult result = PlayerBreakBlockCallback.EVENT.invoker().interact(world, gameMode, player, pos);
        if (result != ActionResult.PASS)
            cir.setReturnValue(false);
    }
}
