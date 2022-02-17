package svenhjol.charm.module.core;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import svenhjol.charm.helper.RecipeHelper;

import java.util.Map;

public class SortingRecipeManager extends RecipeManager {
    private final ReloadableServerResources resources;

    public SortingRecipeManager(ReloadableServerResources resources) {
        this.resources = resources;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        super.apply(map, resourceManager, profilerFiller);

        Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> existing = resources.getRecipeManager().recipes;

        if (!existing.isEmpty()) {
            resources.getRecipeManager().recipes = RecipeHelper.sortAndFilterRecipes(existing, true);
        }
    }
}
