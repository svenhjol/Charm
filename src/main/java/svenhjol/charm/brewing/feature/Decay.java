package svenhjol.charm.brewing.feature;

import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraftforge.registries.IForgeRegistry;
import svenhjol.charm.brewing.potion.DecayPotion;
import svenhjol.meson.Feature;

public class Decay extends Feature
{
    public static DecayPotion potion;
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

    @Override
    public void onRegisterEffects(IForgeRegistry<Effect> registry)
    {
        registry.register(effect);
    }

    @Override
    public void onRegisterPotions(IForgeRegistry<Potion> registry)
    {
        registry.register(potion);
        potion.registerRecipe();
    }
}
