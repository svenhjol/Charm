package svenhjol.charm.module.scale_cured_villager_discount;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, enabledByDefault = false, description = "Scales the trade discount applied when a villager is cured from zombification.\n" +
    "This is an opinionated feature that changes core gameplay and so is disabled by default.")
public class ScaleCuredVillagerDiscount extends CharmModule {
    @Config(name = "Scale discount", description = "Multiplier (where 1.0 is 100%) of the trade discount applied when a villager is cured.  A value of zero disables all discounts.")
    public static double scale = 0.25D;

    public static int scaleValue(int val) {
        return Math.max(0, Math.round(val * (float)ScaleCuredVillagerDiscount.scale));
    }
}
