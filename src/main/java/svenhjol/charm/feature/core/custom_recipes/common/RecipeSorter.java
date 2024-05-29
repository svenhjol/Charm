package svenhjol.charm.feature.core.custom_recipes.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.core.custom_recipes.CustomRecipes;
import svenhjol.charm.charmony.feature.FeatureResolver;

import java.util.Map;

public final class RecipeSorter extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener, FeatureResolver<CustomRecipes> {
    private static final String ID = "charm_recipe_manager";
    private static final Gson GSON = (new GsonBuilder())
        .setPrettyPrinting()
        .setLenient()
        .disableHtmlEscaping()
        .excludeFieldsWithoutExposeAnnotation()
        .create();

    public RecipeSorter() {
        super(GSON, ID);
    }
    
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        var manager = feature().handlers.managerHolder;

        if (manager != null) {
            feature().log().debug("Holding recipe manager reference: " + manager);
            var byType = manager.byType;

            if (!byType.isEmpty()) {
                manager.byType = feature().handlers.sortAndFilter(byType);
            }
        }
    }

    @Override
    public ResourceLocation getFabricId() {
        return Charm.id(ID);
    }

    @Override
    public Class<CustomRecipes> typeForFeature() {
        return CustomRecipes.class;
    }
}
