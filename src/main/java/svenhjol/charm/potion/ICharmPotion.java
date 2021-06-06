package svenhjol.charm.potion;

import svenhjol.charm.module.CharmModule;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.Potion;
import svenhjol.charm.helper.RegistryHelper;

public interface ICharmPotion {
    default void register(CharmModule module, String name) {
        ResourceLocation id = new ResourceLocation(module.mod, name);
        RegistryHelper.potion(id, (Potion)this);
    }
}
