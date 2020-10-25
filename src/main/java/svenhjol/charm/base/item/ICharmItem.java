package svenhjol.charm.base.item;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;

public interface ICharmItem {
    boolean enabled();

    default void register(CharmModule module, String name) {
        RegistryHandler.item(new Identifier(module.mod, name), (Item)this);
    }

    default void setBurnTime(int burnTime) {
        FuelRegistry.INSTANCE.add((Item)this, burnTime);
    }
}
