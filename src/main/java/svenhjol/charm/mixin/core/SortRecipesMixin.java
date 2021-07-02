package svenhjol.charm.mixin.core;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.helper.RecipeHelper;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Mixin(value = RecipeManager.class)
public class SortRecipesMixin {
    private Map<ResourceLocation, JsonElement> map2 = new TreeMap<>();

    /**
     * Creates map of Charm modules to remove from registered recipes.
     * Prepares conversion of registered recipes map to a TreeMap
     * so that modded recipes are iterated before vanilla recipes.
     * 
     * {@link RecipeHelper#prepareCharmModulesFilter(Map)}
     */
    @Inject(
        method = "apply",
        at = @At("HEAD")
    )
    private void hookApply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
        RecipeHelper.prepareCharmModulesFilter(map);
        map2 = new TreeMap<>(map);
    }

    /**
     * Allows RecipeHandler to sort and filter registered recipes.
     */
    @Redirect(
        method = "apply",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Set;iterator()Ljava/util/Iterator;"
        )
    )
    private Iterator<?> hookNewHashMap(Set set) {
        return RecipeHelper.sortAndFilterRecipes(map2);
    }
}
