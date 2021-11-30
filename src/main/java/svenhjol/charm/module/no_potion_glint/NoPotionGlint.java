package svenhjol.charm.module.no_potion_glint;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Removes the potion enchantment glint so you can see what the potion color is.")
public class NoPotionGlint extends CharmModule {
    public static boolean shouldRemoveGlint() {
        return Charm.LOADER.isEnabled(NoPotionGlint.class);
    }
}
