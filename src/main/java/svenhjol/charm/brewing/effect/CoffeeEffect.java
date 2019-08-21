package svenhjol.charm.brewing.effect;

import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.brewing.module.Coffee;
import svenhjol.meson.MesonEffect;

public class CoffeeEffect extends MesonEffect
{
    public CoffeeEffect()
    {
        super(EffectType.BENEFICIAL, Coffee.color);

        addEffect(Effects.STRENGTH);
        addEffect(Effects.HASTE);
        addEffect(Effects.SPEED);

        register(new ResourceLocation(Charm.MOD_ID, "coffee_effect"));
    }
}
