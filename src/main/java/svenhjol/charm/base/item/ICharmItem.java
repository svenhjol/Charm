package svenhjol.charm.base.item;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.base.CharmModule;

public interface ICharmItem {
    boolean enabled();

    default void register(CharmModule module, String name) {
        Registry.register(Registry.ITEM, new Identifier(module.mod, name), (Item)this);
    }

    default void setBurnTime(int burnTime) {
        FuelRegistry.INSTANCE.add((Item)this, burnTime);
    }
}
