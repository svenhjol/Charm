package svenhjol.charm.brewing.feature;

import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import svenhjol.charm.brewing.potion.DecayPotion;
import svenhjol.meson.Feature;
import svenhjol.meson.iface.IMesonPotion;

public class Decay extends Feature
{
    public static IMesonPotion potion;
    public static Effect effect;
    public static int color;
    public static int duration;

    @Override
    public void configure()
    {
        super.configure();
        duration = 10;
        color = 0x101020;
    }

    @Override
    public void init()
    {
        super.init();
        effect = Effects.WITHER;
        potion = new DecayPotion();
    }
}
