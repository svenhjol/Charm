package svenhjol.meson;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import svenhjol.meson.iface.IMesonPotion;

public abstract class MesonPotion extends Potion implements IMesonPotion
{
    protected String baseName;
    protected Ingredient base;
    protected Ingredient reagant;

    public MesonPotion(String baseName, EffectInstance... effects)
    {
        super(effects);
        this.baseName = baseName;
        register();
    }

    @Override
    public String getBaseName()
    {
        return baseName;
    }

    public Ingredient getBase()
    {
        return base;
    }

    public Ingredient getReagant()
    {
        return reagant;
    }
}
