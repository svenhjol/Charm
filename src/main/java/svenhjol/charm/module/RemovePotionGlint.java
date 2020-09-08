package svenhjol.charm.module;

import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "Removes the potion enchantment glint so you can see what the potion color is.")
public class RemovePotionGlint extends MesonModule {
    private static boolean isEnabled = false;

    @Override
    public void init() {
        isEnabled = this.enabled;
    }

    public static boolean shouldRemoveGlint() {
        return isEnabled;
    }
}
