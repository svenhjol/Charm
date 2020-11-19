package svenhjol.charm.mixin;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.module.SnowStorms;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow private ClientWorld world;

    @Shadow @Final private static Identifier SNOW;

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
            target = "Lnet/minecraft/client/texture/TextureManager;bindTexture(Lnet/minecraft/util/Identifier;)V",
            ordinal = 1
        )
    )
    private void hookRenderWeatherTexture(TextureManager textureManager, Identifier id) {
        float h = world.getThunderGradient(this.gradient);
        if (h > 0.0F && ModuleHandler.enabled("charm:snow_storms")) {
            textureManager.bindTexture(SnowStorms.HEAVY_SNOW);
        } else {
            textureManager.bindTexture(SNOW);
        }
    }
}
