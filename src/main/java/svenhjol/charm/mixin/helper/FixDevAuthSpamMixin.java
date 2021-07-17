package svenhjol.charm.mixin.helper;

import com.mojang.authlib.minecraft.OfflineSocialInteractions;
import com.mojang.authlib.minecraft.SocialInteractionsService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class FixDevAuthSpamMixin {
    @Inject(
        method = "createSocialInteractions",
        at = @At("HEAD"),
        remap = false,
        cancellable = true
    )
    private void hookCheckPrivileges(YggdrasilAuthenticationService yggdrasilAuthenticationService, GameConfig gameConfig, CallbackInfoReturnable<SocialInteractionsService> cir) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment())
            cir.setReturnValue(new OfflineSocialInteractions());
    }
}
