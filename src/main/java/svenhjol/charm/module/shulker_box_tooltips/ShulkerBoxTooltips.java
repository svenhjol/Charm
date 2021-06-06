package svenhjol.charm.module.shulker_box_tooltips;

import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.module.shulker_box_tooltips.ShulkerBoxTooltipsClient;

@Module(mod = Charm.MOD_ID, client = ShulkerBoxTooltipsClient.class, description = "Shows the contents of a Shulker Box on hover-over.",
    requiresMixins = {"RenderTooltipCallback"})
public class ShulkerBoxTooltips extends CharmModule {
}
