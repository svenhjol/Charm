package svenhjol.charm.potion;

import svenhjol.charm.loader.CommonModule;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import svenhjol.charm.helper.RegistryHelper;

public abstract class CharmPotion extends Potion implements ICharmPotion {
    protected CommonModule module;

    public CharmPotion(CommonModule module, String name, MobEffectInstance... effects) {
        super(effects);
        this.setModuleAndName(module, name);
    }

    public CharmPotion(CommonModule module, String name, String basedOn, MobEffectInstance... effects) {
        super(basedOn, effects);
        this.setModuleAndName(module, name);
    }

    protected void setModuleAndName(CommonModule module, String name) {
        this.module = module;
        this.register(module, name);
    }

    protected void registerRecipe(Potion input, Item reagant) {
        RegistryHelper.brewingRecipe(input, reagant, this);
    }
}
