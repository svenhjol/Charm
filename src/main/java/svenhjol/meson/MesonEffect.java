package svenhjol.meson;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import svenhjol.meson.iface.IMesonEffect;

public abstract class MesonEffect extends Effect implements IMesonEffect
{
    protected String baseName;

    public MesonEffect(String baseName, EffectType effectType, int color)
    {
        super(effectType, color);
        this.baseName = baseName;
        register();
    }

    @Override
    public String getBaseName()
    {
        return baseName;
    }

    protected void addEffect(Effect effect)
    {
        effect.getAttributeModifierMap().forEach((attribute, modifier) -> {
            this.addAttributesModifier(attribute, modifier.getID().toString(), modifier.getAmount(), modifier.getOperation());
        });
    }
}
