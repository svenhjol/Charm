package svenhjol.charm.mixin.feature.core.custom_recipes;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.core.custom_recipes.CustomRecipes;

import java.util.HashMap;
import java.util.Map;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin extends SimpleJsonResourceReloadListener {
    public RecipeManagerMixin(Gson gson, String string) {
        super(gson, string);
    }

    @Override
    protected Map<ResourceLocation, JsonElement> prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        var handlers = Resolve.feature(CustomRecipes.class).handlers;
        var map = super.prepare(resourceManager, profilerFiller);
        var copy = new HashMap<>(map);
        map.forEach((key, val) -> {
            if (handlers.shouldRemove(key)) {
                copy.remove(key);
            }
        });
        return copy;
    }
}
