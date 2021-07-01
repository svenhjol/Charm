package svenhjol.charm.potion;

import svenhjol.charm.loader.CharmCommonModule;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.Potion;
import svenhjol.charm.helper.RegistryHelper;

public interface ICharmPotion {
    default void register(CharmCommonModule module, String name) {
        ResourceLocation id = new ResourceLocation(module.getModId(), name);
        RegistryHelper.potion(id, (Potion)this);
    }
}
