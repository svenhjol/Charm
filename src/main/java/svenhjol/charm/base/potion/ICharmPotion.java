package svenhjol.charm.base.potion;

import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;

public interface ICharmPotion {
    default void register(CharmModule module, String name) {
        Identifier id = new Identifier(module.mod, name);
        RegistryHandler.potion(id, (Potion)this);
    }
}
