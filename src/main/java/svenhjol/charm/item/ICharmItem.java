package svenhjol.charm.item;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import svenhjol.charm.registry.CommonRegistry;
import svenhjol.charm.loader.CharmModule;

public interface ICharmItem {
    boolean enabled();

    default void register(CharmModule module, String name) {
        CommonRegistry.item(new ResourceLocation(module.getModId(), name), (Item)this);
    }

    default void setBurnTime(int burnTime) {
        FuelRegistry.INSTANCE.add((Item)this, burnTime);
    }

    default void setFireproof() {
        FuelRegistry.INSTANCE.remove((Item)this);
    }
}
