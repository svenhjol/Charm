package svenhjol.charm.module.map_tooltips;

import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.module.map_tooltips.MapTooltipsClient;

@Module(mod = Charm.MOD_ID, client = MapTooltipsClient.class, description = "Show maps in tooltips.",
    requiresMixins = {"RenderTooltipCallback"})
public class MapTooltips extends CharmModule {
}
