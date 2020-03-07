package svenhjol.charm.brewing.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.brewing.effect.CoffeeEffect;
import svenhjol.charm.brewing.potion.CoffeePotion;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.BREWING,
    description = "Brew cocoa beans in water to make Coffee which gives you strength, haste and speed.")
public class Coffee extends MesonModule {
    public static CoffeeEffect effect;
    public static CoffeePotion potion;
    public static int color = 0x602000;

    @Config(name = "Duration", description = "Duration (in seconds) of the Coffee effect.")
    public static int duration = 20;

    @Override
    public void init() {
        effect = new CoffeeEffect(this);
        potion = new CoffeePotion(this);
    }
}
