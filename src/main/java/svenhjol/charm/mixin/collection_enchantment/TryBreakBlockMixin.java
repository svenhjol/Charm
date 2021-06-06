package svenhjol.charm.mixin.collection_enchantment;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.collection_enchantment.CollectionEnchantment;

@Mixin(ServerPlayerGameMode.class)
public class TryBreakBlockMixin {
    @Final
    @Shadow
    protected ServerPlayer player;

    @Shadow
    protected ServerLevel world;

    @Inject(method = "tryBreakBlock", at = @At("HEAD"))
    private void hookTryBreakBlockBegin(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        CollectionEnchantment.startBreaking(player, player.getMainHandItem());
    }

    @Inject(method = "tryBreakBlock", at = @At("TAIL"))
    private void hookTryBreakBlockEnd(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        CollectionEnchantment.stopBreaking();
    }
}
