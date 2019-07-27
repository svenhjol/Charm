package svenhjol.charm.brewing.effect;

import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import svenhjol.charm.Charm;
import svenhjol.charm.brewing.feature.Coffee;
import svenhjol.meson.MesonEffect;

public class CoffeeEffect extends MesonEffect
{
    public CoffeeEffect()
    {
        super("coffee_effect", EffectType.BENEFICIAL, Coffee.color);

        addEffect(Effects.STRENGTH);
        addEffect(Effects.HASTE);
        addEffect(Effects.SPEED);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }
}
