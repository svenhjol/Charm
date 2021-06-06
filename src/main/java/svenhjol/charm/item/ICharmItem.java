package svenhjol.charm.item;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.RegistryHelper;

public interface ICharmItem {
    boolean enabled();

    default void register(CharmModule module, String name) {
        RegistryHelper.item(new ResourceLocation(module.mod, name), (Item)this);
    }

    default void setBurnTime(int burnTime) {
        FuelRegistry.INSTANCE.add((Item)this, burnTime);
    }
}
