package svenhjol.charm.base.potion;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;

public interface ICharmStatusEffect {
    default void register(CharmModule module, String name) {
        Identifier id = new Identifier(module.mod, name);
        RegistryHandler.statusEffect(id, (StatusEffect)this);
    }
}
