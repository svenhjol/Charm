package svenhjol.charm.mixin.snow_storms;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.snow_storms.SnowStormsClient;

@Mixin(LevelRenderer.class)
public class RenderHeavySnowTextureMixin {
    @Shadow private ClientLevel world;

    private float gradient;

    @Inject(
        method = "renderWeather",
        at = @At("HEAD")
    )
    private void hookRenderWeather(LightTexture manager, float f, double d, double e, double g, CallbackInfo ci) {
        gradient = f;
    }

    /**
     * Calls the snowstorms client to override the snow texture with
     * a custom one when the weather renderer tries to render snow.
     */
    @Inject(
        method = "renderWeather",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/BufferBuilder;begin(Lnet/minecraft/client/render/VertexFormat$DrawMode;Lnet/minecraft/client/render/VertexFormat;)V",
            ordinal = 1,
            shift = At.Shift.BEFORE
        )
    )
    private void hookRenderWeatherTexture(LightTexture manager, float f, double d, double e, double g, CallbackInfo ci) {
        SnowStormsClient.tryHeavySnowTexture(world, gradient);
    }
}
