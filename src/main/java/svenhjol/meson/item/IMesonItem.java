package svenhjol.meson.item;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import svenhjol.meson.MesonModule;

public interface IMesonItem {
    boolean enabled();

    default void register(MesonModule module, String name) {
        Registry.register(Registry.ITEM, new Identifier(module.mod.getId(), name), (Item)this);
    }

    default void setBurnTime(int burnTime) {
        FuelRegistry.INSTANCE.add((Item)this, burnTime);
    }
}
