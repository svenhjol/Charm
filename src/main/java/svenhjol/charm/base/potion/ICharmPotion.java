package svenhjol.charm.base.potion;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;

public interface ICharmPotion {
    void registerRecipe(Potion input, Item reagant);

    default void register(CharmModule module, String name) {
        Identifier id = new Identifier(module.mod, name);
        RegistryHandler.potion(id, (Potion)this);
    }
}
