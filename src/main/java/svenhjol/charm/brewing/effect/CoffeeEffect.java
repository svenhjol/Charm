package svenhjol.charm.brewing.effect;

import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import svenhjol.charm.brewing.module.Coffee;
import svenhjol.meson.MesonEffect;
import svenhjol.meson.MesonModule;

public class CoffeeEffect extends MesonEffect {
    public CoffeeEffect(MesonModule module) {
        super(module, EffectType.BENEFICIAL, "coffee_effect", Coffee.color);

        addEffect(Effects.STRENGTH);
        addEffect(Effects.HASTE);
        addEffect(Effects.SPEED);
    }
}
