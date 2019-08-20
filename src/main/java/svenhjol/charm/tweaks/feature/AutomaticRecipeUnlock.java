package svenhjol.charm.tweaks.feature;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.ForgeHelper;
import svenhjol.meson.iface.MesonLoadModule;

import java.util.Collection;

/**
 * @see net.minecraft.item.KnowledgeBookItem
 */
@MesonLoadModule(category = CharmCategories.TWEAKS, hasSubscriptions = true)
public class AutomaticRecipeUnlock extends Feature
{
    @Override
    public boolean isEnabled()
    {
        return super.isEnabled() && !ForgeHelper.isModLoaded("quark");
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
}
