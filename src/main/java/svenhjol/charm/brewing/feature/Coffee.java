package svenhjol.charm.brewing.feature;

import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraftforge.registries.IForgeRegistry;
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

    @Override
    public void registerEffects(IForgeRegistry<Effect> registry)
    {
        registry.register(effect);
    }

    @Override
    public void registerPotions(IForgeRegistry<Potion> registry)
    {
        registry.register(potion);
        potion.registerRecipe();
    }
}
