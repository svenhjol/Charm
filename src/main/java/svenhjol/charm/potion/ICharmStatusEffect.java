package svenhjol.charm.potion;

import svenhjol.charm.module.CharmModule;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import svenhjol.charm.helper.RegistryHelper;

public interface ICharmStatusEffect {
    default void register(CharmModule module, String name) {
        ResourceLocation id = new ResourceLocation(module.mod, name);
        RegistryHelper.statusEffect(id, (MobEffect)this);
    }
}
