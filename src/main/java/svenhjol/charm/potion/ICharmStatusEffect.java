package svenhjol.charm.potion;

import svenhjol.charm.loader.CommonModule;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import svenhjol.charm.helper.RegistryHelper;

public interface ICharmStatusEffect {
    default void register(CommonModule module, String name) {
        ResourceLocation id = new ResourceLocation(module.getModId(), name);
        RegistryHelper.statusEffect(id, (MobEffect)this);
    }
}
