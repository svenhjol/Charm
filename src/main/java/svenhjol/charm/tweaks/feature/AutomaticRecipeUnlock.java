package svenhjol.charm.tweaks.feature;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.Feature;

import java.util.Collection;

/**
 * @see net.minecraft.item.KnowledgeBookItem
 * @todo add isEnabled() to check for quark, disable if present
 */
public class AutomaticRecipeUnlock extends Feature
{
    @Override
    public void init()
    {
        super.init();
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event)
    {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            RecipeManager recipeManager = event.getPlayer().world.getRecipeManager();
            Collection<IRecipe<?>> allRecipes = recipeManager.getRecipes();
            event.getPlayer().unlockRecipes(allRecipes);
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
