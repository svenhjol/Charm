package svenhjol.meson;

import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;

public class Meson
{
    public static final String MOD_ID = "Meson";
    private static boolean hasInit = false;

    public static void log(Object... out)
    {
        for (Object obj : out) {
            LogManager.getLogger(MOD_ID).info(obj);
        }
    }

    public static void init()
    {
        if (!hasInit) {
//            MinecraftForge.EVENT_BUS.register(RegistrationHandler.class);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(Meson::commonSetup);
            hasInit = true;
        }
    }

    public static void commonSetup(final FMLCommonSetupEvent event)
    {
        DeferredWorkQueue.runLater(() -> {
            MesonLoader.instances.forEach((id, loader) -> loader.features.forEach(feature -> {

                feature.registerMessages();
                feature.registerComposterItems();

            }));
        });

        Meson.log("Common setup done");
    }
}
