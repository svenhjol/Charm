package svenhjol.charm.api.iface;

import java.util.List;

/**
 * Define conditional recipes from any Charmony mod.
 * @see IConditionalRecipe
 */
@SuppressWarnings("unused")
public interface IConditionalRecipeProvider {
    List<IConditionalRecipe> getRecipeConditions();
}
