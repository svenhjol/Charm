package svenhjol.charm.api.iface;

import java.util.List;

/**
 * Define conditional recipes from any Charmony mod.
 * @see ConditionalRecipe
 */
@SuppressWarnings("unused")
public interface ConditionalRecipeProvider {
    List<ConditionalRecipe> getRecipeConditions();
}
