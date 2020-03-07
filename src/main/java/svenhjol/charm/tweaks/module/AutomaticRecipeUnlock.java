package svenhjol.charm.tweaks.module;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ForgeHelper;
import svenhjol.meson.iface.Module;

import java.util.Collection;

/**
 * @see net.minecraft.item.KnowledgeBookItem
 */
@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS, hasSubscriptions = true,
    description = "Unlocks all vanilla recipes. This will be disabled if Quark is present, because Quark's module is better.")
public class AutomaticRecipeUnlock extends MesonModule {
    @Override
    public boolean shouldRunSetup() {
        return !ForgeHelper.isModLoaded("quark");
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity) {
            RecipeManager recipeManager = event.getPlayer().world.getRecipeManager();
            Collection<IRecipe<?>> allRecipes = recipeManager.getRecipes();
            event.getPlayer().unlockRecipes(allRecipes);
        }
    }
}
