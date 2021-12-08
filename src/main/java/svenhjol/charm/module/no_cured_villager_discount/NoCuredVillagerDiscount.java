package svenhjol.charm.module.no_cured_villager_discount;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.helper.ModHelper;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, enabledByDefault = false, description = "Removes the trade discount applied when a villager is cured from zombification.\n" +
    "This is an opinionated feature that changes core gameplay and so is disabled by default.\n" +
    "This feature is disabled if villagerfix is present because it's better.")
public class NoCuredVillagerDiscount extends CharmModule {
    @Override
    public void register() {
        addDependencyCheck(m -> !ModHelper.isLoaded("villagerfix"));
    }

    public static boolean shouldRemoveDiscount() {
        return Charm.LOADER.isEnabled(NoCuredVillagerDiscount.class);
    }
}
