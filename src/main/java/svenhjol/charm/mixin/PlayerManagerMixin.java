package svenhjol.charm.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.AutomaticRecipeUnlock;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(method = "onPlayerConnect", at = @At("RETURN"))
    private void hookOnPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        AutomaticRecipeUnlock.unlockRecipes(player);
    }
}
