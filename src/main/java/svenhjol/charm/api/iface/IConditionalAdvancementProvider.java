package svenhjol.charm.api.iface;

import java.util.List;

/**
 * Define conditional advancements from any Charmony mod.
 * @see IConditionalAdvancement
 */
public interface IConditionalAdvancementProvider {
    List<IConditionalAdvancement> getAdvancementConditions();
}
