package svenhjol.charm.api.iface;

import java.util.List;
import java.util.Map;

public interface IConditionalTag {
    /**
     * If this test fails, all resources that match patterns returned by resources() will be removed when the world loads.
     */
    boolean test();

    /**
     * Tag roots to list of resources.
     * Example: minecraft:tags/blocks/mineable/pickaxe.json -> [charm:arcane_purpur_block]
     * You can use literals for the resource name such as "charm:azalea_barrel" or fuzzy matches such as "charm:*_barrel".
     * The namespace does not need to be included; "*barrel*" is valid and will match all charmony mods.
     */
    Map<String, List<String>> resources();
}
