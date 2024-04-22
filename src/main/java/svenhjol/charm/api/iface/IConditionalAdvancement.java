package svenhjol.charm.api.iface;

import java.util.List;

/**
 * An advancement definition to test for some condition and remove one or more Charmony advancements if the test fails.
 */
public interface IConditionalAdvancement {
    /**
     * If this test fails, all advancements that match patterns returned by advancements() will be removed when the world loads.
     */
    boolean test();

    /**
     * List of advancements.
     * You can use literals such as "charm:azalea_barrel" or fuzzy matches such as "charm:*_barrel".
     * The namespace does not need to be included; "*barrel*" is valid and will match all charmony mods.
     */
    List<String> advancements();
}
