package svenhjol.charm.feature.recipes;

import net.minecraft.world.item.crafting.RecipeManager;
import svenhjol.charm.Charm;
import svenhjol.charm.api.iface.IConditionalRecipe;
import svenhjol.charm.api.iface.IConditionalRecipeProvider;
import svenhjol.charm.foundation.Log;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.helper.ApiHelper;

import java.util.ArrayList;
import java.util.List;

public class Recipes extends CommonFeature {
    static final List<IConditionalRecipe> CONDITIONS = new ArrayList<>();
    static final Log LOGGER = new Log(Charm.ID, "Recipes");

    /**
     * Holds a reference to the global RecipeManager.
     * @see svenhjol.charm.mixin.feature.recipes.ReloadableServerResourcesMixin
     */
    public static RecipeManager managerHolder;

    @Override
    public String description() {
        return """
            Filter recipes when Charmony-mod features or settings are disabled.
            Disabling this feature will cause unexpected behavior and potentially unachievable recipes.""";
    }

    @Override
    public int priority() {
        return 10;
    }

    @Override
    public void onEnabled() {
        ApiHelper.consume(IConditionalRecipeProvider.class,
            provider -> CONDITIONS.addAll(provider.getRecipeConditions()));
    }
}
