package svenhjol.charm.mobs.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.MOBS, description = "Configurable delay chance between parrot mimic sounds.")
public class ParrotMimicDelay extends MesonModule {
    private static final int DEFAULT = 50;
    private static int cachedChance = -1;

    @Config(name = "Delay chance", description = "Chance (1 in X) of a parrot mimicking a mob every tick.\nVanilla is 50.")
    public static int chance = 200;

    public static int getChance() {
        if (cachedChance == -1) {
            cachedChance = Meson.isModuleEnabled("charm:parrot_mimic_delay") ? chance : DEFAULT;
        }
        return cachedChance;
    }
}
