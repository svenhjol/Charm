package svenhjol.meson;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import svenhjol.meson.iface.IMesonEffect;

public abstract class MesonEffect extends Effect implements IMesonEffect {
    protected MesonModule module;

    public MesonEffect(MesonModule module, EffectType effectType, String name, int color) {
        super(effectType, color);
        this.module = module;
        register(module, name);
    }

    protected void addEffect(Effect effect) {
        effect.getAttributeModifierMap().forEach((attribute, modifier) -> this.addAttributesModifier(attribute, modifier.getID().toString(), modifier.getAmount(), modifier.getOperation()));
    }
}
