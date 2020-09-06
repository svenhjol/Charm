package svenhjol.meson.mixin;

import com.google.gson.JsonElement;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.meson.handler.RecipeHandler;

import java.util.Map;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    /**
     * Allows RecipeHandler to remove recipes that are no longer
     * valid according to the module configuration.
     * 
     * {@link RecipeHandler#filter(Map)}
     */
    @Inject(
        method = "apply",
        at = @At("HEAD")
    )
    private void hookApply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci) {
        RecipeHandler.filter(map);
    }
}
