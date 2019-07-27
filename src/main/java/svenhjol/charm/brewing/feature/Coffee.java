package svenhjol.charm.brewing.feature;

import svenhjol.charm.brewing.effect.CoffeeEffect;
import svenhjol.charm.brewing.potion.CoffeePotion;
import svenhjol.meson.Feature;

public class Coffee extends Feature
{
    public static CoffeeEffect effect;
    public static CoffeePotion potion;
    public static int duration;
    public static int color;

    @Override
    public void configure()
    {
        super.configure();
        duration = 10;
        color = 0x602000;
    }

    @Override
    public void init()
    {
        super.init();
        effect = new CoffeeEffect();
        potion = new CoffeePotion();
    }
}
