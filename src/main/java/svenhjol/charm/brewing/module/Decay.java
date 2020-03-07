package svenhjol.charm.brewing.module;

import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.brewing.potion.DecayPotion;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.BREWING,
    description = "A potion that gives Wither to living things.")
public class Decay extends MesonModule {
    public static DecayPotion potion;
    public static Effect effect;

    @Config(name = "Duration", description = "Duration (in seconds) of the Decay effect.")
    public static int duration = 10;

    @Override
    public void init() {
        effect = Effects.WITHER;
        potion = new DecayPotion(this);
    }
}
