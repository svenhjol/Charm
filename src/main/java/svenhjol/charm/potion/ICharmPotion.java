package svenhjol.charm.potion;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.Potion;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.loader.CharmModule;

public interface ICharmPotion {
    default void register(CharmModule module, String name) {
        ResourceLocation id = new ResourceLocation(module.getModId(), name);
        RegistryHelper.potion(id, (Potion)this);
    }
}
