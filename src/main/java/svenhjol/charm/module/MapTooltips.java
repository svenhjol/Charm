package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.MapTooltipsClient;

@Module(mod = Charm.MOD_ID, client = MapTooltipsClient.class, description = "Show maps in tooltips.",
    requiresMixins = {"RenderTooltipCallback"})
public class MapTooltips extends CharmModule {
}
