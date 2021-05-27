package svenhjol.charm.module.remove_potion_glint;

import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.annotation.Module;

@Module(mod = Charm.MOD_ID, description = "Removes the potion enchantment glint so you can see what the potion color is.")
public class RemovePotionGlint extends CharmModule {
    private static boolean isEnabled = false;

    @Override
    public void register() {
        isEnabled = this.enabled;
    }

    public static boolean shouldRemoveGlint() {
        return isEnabled;
    }
}
