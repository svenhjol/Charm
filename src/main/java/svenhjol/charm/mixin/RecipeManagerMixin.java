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

@Mixin(value = RecipeManager.class)
public class RecipeManagerMixin {
    private Map<Identifier, JsonElement> map2 = new TreeMap<>();

    /**
     * Creates map of Charm modules to remove from registered recipes.
     * Prepares conversion of registered recipes map to a TreeMap
     * so that modded recipes are iterated before vanilla recipes.
     * 
     * {@link RecipeHandler#prepareCharmModulesFilter(Map)}
     */
    @Inject(
        method = "apply",
        at = @At("HEAD")
    )
    private void hookApply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo ci) {
        RecipeHandler.prepareCharmModulesFilter(map);
        map2 = new TreeMap<>(map);
    }

    /**
     * Allows RecipeHandler to sort and filter registered recipes.
     *
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
        return RecipeHandler.sortAndFilterRecipes(map2);
    }
}
