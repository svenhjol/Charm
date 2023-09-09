package svenhjol.charm.mixin.collection;

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
import svenhjol.charm.feature.collection.Collection;

@Mixin(ServerPlayerGameMode.class)
public class ServerPlayerGameModeMixin {
    @Final
    @Shadow
    protected ServerPlayer player;
    
    @Shadow
    protected ServerLevel level;
    
    @Inject(method = "destroyBlock", at = @At("HEAD"))
    private void hookTryBreakBlockBegin(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Collection.startBreaking(player, pos);
    }
    
    @Inject(method = "destroyBlock", at = @At("TAIL"))
    private void hookTryBreakBlockEnd(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Collection.stopBreaking(pos);
    }
}