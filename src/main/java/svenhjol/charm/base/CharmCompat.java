package svenhjol.charm.base;

import svenhjol.charm.Charm;
import svenhjol.charm.base.compat.QuarkCompat;
import svenhjol.meson.MesonInstance;
import svenhjol.meson.helper.ForgeHelper;

public class CharmCompat {
    public static void init(MesonInstance instance) {
        try {
            if (ForgeHelper.isModLoaded("quark")) {
                Charm.quarkCompat = QuarkCompat.class.newInstance();
                instance.log.debug("Loaded Quark compatibility");
            }
        } catch (Exception e) {
            instance.log.error("Error loading Quark compatibility: " + e.getMessage());
        }
    }
}
