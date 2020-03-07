package svenhjol.charm.tweaks.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS,
    description = "Removes the potion enchantment glint so you can see what the potion color is.")
public class RemovePotionGlint extends MesonModule {

}
