package svenhjol.charm.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.CollectionEnchantment;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {
    @Shadow public ServerPlayerEntity player;

    @Shadow public ServerWorld world;

    @Inject(method = "tryBreakBlock", at = @At("HEAD"))
    private void hookTryBreakBlockBegin(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        CollectionEnchantment.startBreaking(player, player.getMainHandStack());
    }

    @Inject(method = "tryBreakBlock", at = @At("TAIL"))
    private void hookTryBreakBlockEnd(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        CollectionEnchantment.stopBreaking();
    }
}
