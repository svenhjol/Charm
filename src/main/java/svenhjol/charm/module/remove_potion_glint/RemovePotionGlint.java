package svenhjol.charm.module.remove_potion_glint;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;

@CommonModule(mod = Charm.MOD_ID, description = "Removes the potion enchantment glint so you can see what the potion color is.")
public class RemovePotionGlint extends svenhjol.charm.loader.CommonModule {
    private static boolean isEnabled = false;

    @Override
    public void register() {
        isEnabled = this.isEnabled();
    }

    public static boolean shouldRemoveGlint() {
        return isEnabled;
    }
}
