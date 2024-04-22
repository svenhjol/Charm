package svenhjol.charm.api.iface;

import java.util.List;

/**
 * A recipe definition to test for some condition and remove one or more Charmony recipes if the test fails.
 */
@SuppressWarnings("unused")
public interface IConditionalRecipe {
    /**
     * If this test fails, all recipes that match patterns returned by recipes() will be removed when the world loads.
     */
    boolean test();

    /**
     * List of recipes.
     * You can use literals such as "charm:azalea_barrel" or fuzzy matches such as "charm:*_barrel".
     * The namespace does not need to be included; "*barrel*" is valid and will match all charmony mods.
     */
    List<String> recipes();
}
