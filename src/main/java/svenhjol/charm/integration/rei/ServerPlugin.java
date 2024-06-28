package svenhjol.charm.integration.rei;

import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;

/**
 * See me.shedaniel.rei.plugin.common.DefaultPlugin for reference implementation.
 */
public final class ServerPlugin implements REIServerPlugin {
    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(Definitions.WOODCUTTING, WoodcuttingDisplay.serializer());
        registry.register(Definitions.FIRING, FiringDisplay.serializer(FiringDisplay::new));
    }
}
