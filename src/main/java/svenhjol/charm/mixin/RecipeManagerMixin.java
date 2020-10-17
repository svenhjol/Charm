package svenhjol.charm.mixin;

import com.google.gson.JsonElement;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.base.handler.RecipeHandler;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    private Map<Identifier, JsonElement> map2 = new TreeMap<>();

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
        map2 = new TreeMap<>(map);
    }

    /**
     * Allows RecipeHandler to sort Minecraft's recipes
     * prioritising modded recipes over vanilla.
     * @param set
     * @return
     */
    @Redirect(
        method = "apply",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Set;iterator()Ljava/util/Iterator;"
        )
    )
    private Iterator<?> hookNewHashMap(Set set) {
        return RecipeHandler.sortedRecipes(map2);
    }
}
