package svenhjol.charm.module.core;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import svenhjol.charm.helper.RecipeHelper;

import java.util.Map;

public class SortingRecipeManager extends RecipeManager {
    private final ServerResources manager;

    public SortingRecipeManager(ServerResources manager) {
        this.manager = manager;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        super.apply(map, resourceManager, profilerFiller);

        Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> existing = manager.getRecipeManager().recipes;

        if (!existing.isEmpty()) {
            manager.getRecipeManager().recipes = RecipeHelper.sortAndFilterRecipes(existing, true);
        }
    }
}
