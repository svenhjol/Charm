package svenhjol.meson.compat;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public interface IQuarkCompat {
    void onCommonSetup(FMLCommonSetupEvent event, IEventBus forgeEventBus);
    boolean isModuleEnabled(String name);
    boolean isModulePresent(String name);
}
