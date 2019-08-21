package svenhjol.charm.brewing.module;

import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.brewing.effect.CoffeeEffect;
import svenhjol.charm.brewing.potion.CoffeePotion;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(category = CharmCategories.BREWING)
public class Coffee extends MesonModule
{
    public static CoffeeEffect effect;
    public static CoffeePotion potion;
    public static int duration = 10;
    public static int color = 0x602000;

    @Override
    public void init()
    {
        effect = new CoffeeEffect();
        potion = new CoffeePotion();
    }
}
