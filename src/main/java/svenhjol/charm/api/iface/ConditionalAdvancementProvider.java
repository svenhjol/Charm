package svenhjol.charm.api.iface;

import java.util.List;

/**
 * Define conditional advancements from any Charmony mod.
 * @see ConditionalAdvancement
 */
public interface ConditionalAdvancementProvider {
    List<ConditionalAdvancement> getAdvancementConditions();
}
