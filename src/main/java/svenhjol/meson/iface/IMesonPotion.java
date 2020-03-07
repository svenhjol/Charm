package svenhjol.meson.iface;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import svenhjol.meson.MesonModule;
import svenhjol.meson.handler.RegistryHandler;

public interface IMesonPotion {
    void registerRecipe(Potion input, Item reagant);

    default void register(MesonModule module, String name) {
        RegistryHandler.registerPotion((Potion) this, new ResourceLocation(module.mod, name));
    }
}
