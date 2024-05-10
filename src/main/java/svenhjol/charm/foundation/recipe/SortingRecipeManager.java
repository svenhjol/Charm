package svenhjol.charm.foundation.recipe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import svenhjol.charm.Charm;

import java.util.Map;

public class SortingRecipeManager extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {
    private static final String ID = "charmony_sorting_recipe_manager";
    private static final Gson GSON = (new GsonBuilder())
        .setPrettyPrinting()
        .setLenient()
        .disableHtmlEscaping()
        .excludeFieldsWithoutExposeAnnotation()
        .create();

    public SortingRecipeManager() {
        super(GSON, ID);
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        if (RecipeHandlers.managerHolder != null) {
            RecipeHandlers.LOGGER.debug("Holding recipe manager reference: " + RecipeHandlers.managerHolder);
            var byType = RecipeHandlers.managerHolder.byType;

            if (!byType.isEmpty()) {
                RecipeHandlers.managerHolder.byType = RecipeHandlers.sortAndFilter(byType);
            }
        }
    }

    @Override
    public ResourceLocation getFabricId() {
        return Charm.id("sorting_recipe_manager");
    }
}