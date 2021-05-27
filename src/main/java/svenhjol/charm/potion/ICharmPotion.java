package svenhjol.charm.potion;

import net.minecraft.potion.Potion;
import net.minecraft.util.Identifier;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.RegistryHelper;

public interface ICharmPotion {
    default void register(CharmModule module, String name) {
        Identifier id = new Identifier(module.mod, name);
        RegistryHelper.potion(id, (Potion)this);
    }
}
