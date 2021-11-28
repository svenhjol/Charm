package svenhjol.charm.potion;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.alchemy.Potion;
import svenhjol.charm.registry.CommonRegistry;
import svenhjol.charm.loader.CharmModule;

public interface ICharmPotion {
    default void register(CharmModule module, String name) {
        ResourceLocation id = new ResourceLocation(module.getModId(), name);
        CommonRegistry.potion(id, (Potion)this);
    }
}
