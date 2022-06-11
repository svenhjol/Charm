package svenhjol.charm.module.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import svenhjol.charm.Charm;
import svenhjol.charm.helper.LogHelper;
import svenhjol.charm.helper.RecipeHelper;
import svenhjol.charm.init.CharmResources;

import java.util.Map;

public class SortingRecipeManager extends SimpleJsonResourceReloadListener implements IdentifiableResourceReloadListener {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().setLenient().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();

    public SortingRecipeManager() {
        super(GSON, "charm_sortingrecipes");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        if (CharmResources.recipeManagerHolder != null) {
            var holder = CharmResources.recipeManagerHolder;
            LogHelper.debug(getClass(), holder.toString());

            Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> existing = holder.recipes;

            if (!existing.isEmpty()) {
                holder.recipes = RecipeHelper.sortAndFilterRecipes(existing, true);
            }
        }
    }

    @Override
    public ResourceLocation getFabricId() {
        return new ResourceLocation(Charm.MOD_ID, "sortingrecipes");
    }
}
