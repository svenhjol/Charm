package svenhjol.meson;

import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import svenhjol.meson.handler.RegistryHandler;
import svenhjol.meson.iface.IMesonPotion;

public abstract class MesonPotion extends Potion implements IMesonPotion
{
    protected MesonModule module;

    public MesonPotion(MesonModule module, String name, EffectInstance... effects)
    {
        super(effects);

        this.module = module;
        register(module, name);
    }

    @Override
    public void registerRecipe(Potion input, Item reagant)
    {
        RegistryHandler.registerBrewingRecipe(input, reagant, this);
    }
}
