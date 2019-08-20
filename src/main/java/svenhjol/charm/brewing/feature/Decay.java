package svenhjol.charm.brewing.feature;

import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.brewing.potion.DecayPotion;
import svenhjol.meson.Feature;
import svenhjol.meson.iface.MesonLoadModule;

@MesonLoadModule(category = CharmCategories.BREWING)
public class Decay extends Feature
{
    public static DecayPotion potion;
    public static Effect effect;
    public static int color = 0x101020;
    public static int duration = 10;

    @Override
    public void init()
    {
        effect = Effects.WITHER;
        potion = new DecayPotion();
    }
}
