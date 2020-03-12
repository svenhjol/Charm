package svenhjol.charm.base;

import svenhjol.meson.MesonInstance;

public class CharmCompat {
    public static void init(MesonInstance instance) {
        // TODO quark 1.15
//        try {
//            if (ForgeHelper.isModLoaded("quark")) {
//                Charm.quarkCompat = QuarkCompat.class.newInstance();
//                instance.log.debug("Loaded Quark compatibility");
//            }
//        } catch (Exception e) {
//            instance.log.error("Error loading Quark compatibility: " + e.getMessage());
//        }
    }
}
