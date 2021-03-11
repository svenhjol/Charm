package svenhjol.charm.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.client.SnowStormsClient;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow private ClientWorld world;

    private float gradient;

    @Inject(
        method = "renderWeather",
        at = @At("HEAD")
    )
    private void hookRenderWeather(LightmapTextureManager manager, float f, double d, double e, double g, CallbackInfo ci) {
        gradient = f;
    }

    @Redirect(
        method = "renderWeather",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V",
            ordinal = 1
        )
    )
    private void hookRenderWeatherTexture(int i, Identifier id) {
        if (!SnowStormsClient.tryHeavySnowTexture(world, gradient)) {
            RenderSystem.setShaderTexture(i, id);
        }
    }
}
