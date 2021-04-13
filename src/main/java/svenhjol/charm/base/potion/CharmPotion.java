package svenhjol.charm.base.potion;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;

public abstract class CharmPotion extends Potion implements ICharmPotion {
    protected CharmModule module;

    public CharmPotion(CharmModule module, String name, StatusEffectInstance... effects) {
        super(effects);

        this.module = module;
        this.register(module, name);
    }

    protected void registerRecipe(Potion input, Item reagant) {
        RegistryHandler.brewingRecipe(input, reagant, this);
    }
}
