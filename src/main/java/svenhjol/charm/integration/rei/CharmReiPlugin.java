package svenhjol.charm.integration.rei;

import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;

/**
 * See me.shedaniel.rei.plugin.common.DefaultPlugin for reference implementation.
 */
public class CharmReiPlugin implements ICharmReiPlugin, REIServerPlugin {
    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(WOODCUTTING, WoodcuttingDisplay.serializer());
        registry.register(FIRING, FiringDisplay.serializer(FiringDisplay::new));
    }
}
