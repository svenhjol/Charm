package svenhjol.charm.potion;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.RegistryHelper;

public interface ICharmStatusEffect {
    default void register(CharmModule module, String name) {
        Identifier id = new Identifier(module.mod, name);
        RegistryHelper.statusEffect(id, (StatusEffect)this);
    }
}
