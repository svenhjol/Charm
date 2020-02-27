package svenhjol.meson;

import com.google.common.base.CaseFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import svenhjol.meson.compat.QuarkCompat;
import svenhjol.meson.handler.LogHandler;
import svenhjol.meson.handler.PlayerQueueHandler;
import svenhjol.meson.helper.ForgeHelper;
import svenhjol.meson.loader.condition.ModuleEnabledCondition;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Meson
{
    public static final String MOD_ID = "Meson";
    public static final Meson INSTANCE = new Meson();
    public static final LogHandler LOG = new LogHandler(MOD_ID);
    public static final Marker INTERNAL = MarkerManager.getMarker("INTERNAL");

    public static boolean DEBUG = true;
    public static Map<String, MesonInstance> instances = new HashMap<>();

    private IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    private IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

    private static QuarkCompat quarkCompat;

    private Meson() {
        // register crafting recipe conditions
        ModuleEnabledCondition.Serializer modEnabledCondition = new ModuleEnabledCondition.Serializer();
        CraftingHelper.register(modEnabledCondition);

        // basic queue for player tick events
        forgeEventBus.register(new PlayerQueueHandler());
    }

    public void register(MesonInstance instance)
    {
        instances.put(instance.getId(), instance);

        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(instance::onCommonSetup);
        modEventBus.addListener(instance::onModConfig);
        modEventBus.addListener(instance::onLoadComplete);
        forgeEventBus.addListener(instance::onServerAboutToStart);
        forgeEventBus.addListener(instance::onServerStarting);
        forgeEventBus.addListener(instance::onServerStarted);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(instance::onClientSetup));
    }

    public void onCommonSetup(FMLCommonSetupEvent event)
    {
        try
        {
            if (quarkCompat == null && ForgeHelper.isModLoaded("quark")) {
                quarkCompat = QuarkCompat.class.newInstance();
                forgeEventBus.addListener(quarkCompat::onModuleLoaded);
                forgeEventBus.addListener(quarkCompat::onModuleStateChanged);
                LOG.debug("Finished Quark compatibility");
            }
        }
        catch (Exception e)
        {
            LOG.error("Error loading QuarkModules");
        }
    }

    public static boolean isModuleEnabled(String res)
    {
        return isModuleEnabled(new ResourceLocation(res));
    }

    public static boolean isModuleEnabled(ResourceLocation res)
    {
        String inst = res.getNamespace();
        String mod = res.getPath();

        if (inst.equals("quark") && quarkCompat != null)
            return quarkCompat.isModuleEnabled(mod);

        if (instances.containsKey(inst)) {
            MesonInstance instance = instances.get(inst);
            if (mod.contains("_"))
                mod = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, mod);

            return instance.isModuleEnabled(mod);
        }

        return false;
    }


    // --- SORT THIS OUT ---


    @Deprecated
    public static void debug(String msg)
    {
        debug(INTERNAL, msg);
    }

    @Deprecated
    public static void debug(Marker marker, String msg)
    {
        if (DEBUG)
            getLogger().debug(marker, msg);
    }

    @Deprecated
    public static void log(String msg)
    {
        log(INTERNAL, msg);
    }

    @Deprecated
    public static void log(Marker marker, String msg)
    {
        getLogger().info(marker, msg);
    }

    @Deprecated
    public static void warn(String msg)
    {
        warn(INTERNAL, msg);
    }

    @Deprecated
    public static void warn(Marker marker, String msg)
    {
        getLogger().warn(marker, msg);
    }

    @Deprecated
    public static void error(String msg)
    {
        error(INTERNAL, msg);
    }

    @Deprecated
    public static void error(Marker marker, String msg)
    {
        getLogger().error(marker, msg);
    }

    @Deprecated
    public static Logger getLogger()
    {
        return LogManager.getLogger("meson");
    }
}
