package svenhjol.charm.potion;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import svenhjol.charm.registry.CommonRegistry;
import svenhjol.charm.loader.CharmModule;

public interface ICharmStatusEffect {
    default void register(CharmModule module, String name) {
        ResourceLocation id = new ResourceLocation(module.getModId(), name);
        CommonRegistry.statusEffect(id, (MobEffect)this);
    }
}
