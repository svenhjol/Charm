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
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import svenhjol.meson.compat.IQuarkCompat;
import svenhjol.meson.compat.QuarkCompat;
import svenhjol.meson.handler.LogHandler;
import svenhjol.meson.handler.PlayerQueueHandler;
import svenhjol.meson.helper.ForgeHelper;
import svenhjol.meson.loader.condition.ModuleEnabledCondition;
import svenhjol.meson.loader.condition.ModuleNotEnabledCondition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class Meson {
    public static final String MOD_ID = "Meson";
    public static final Meson INSTANCE = new Meson();
    public static final LogHandler LOG = new LogHandler(MOD_ID);
    public static final Marker INTERNAL = MarkerManager.getMarker("INTERNAL");
    public static Map<String, MesonInstance> instances = new ConcurrentHashMap<>();

    private IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    private IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

    private static IQuarkCompat quarkCompat = null;

    private Meson() {
        // register crafting recipe conditions
        ModuleEnabledCondition.Serializer modEnabledCondition = new ModuleEnabledCondition.Serializer();
        ModuleNotEnabledCondition.Serializer modNotEnabledCondition = new ModuleNotEnabledCondition.Serializer();
        CraftingHelper.register(modEnabledCondition);
        CraftingHelper.register(modNotEnabledCondition);

        // basic queue for player tick events
        forgeEventBus.register(new PlayerQueueHandler());
    }

    public void register(MesonInstance instance) {
        instances.put(instance.getId(), instance);
        LOG.info("Added " + instance.getId() + " to Meson");

        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(instance::onCommonSetup);
        modEventBus.addListener(instance::onModConfig);
        modEventBus.addListener(instance::onLoadComplete);
        forgeEventBus.addListener(instance::onServerAboutToStart);
        forgeEventBus.addListener(instance::onServerStarting);
        forgeEventBus.addListener(instance::onServerStarted);

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(instance::onClientSetup));
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        try {
            if (quarkCompat == null && ForgeHelper.isModLoaded("quark")) {
                quarkCompat = QuarkCompat.class.newInstance();
                quarkCompat.onCommonSetup(event, forgeEventBus);
                LOG.debug("Finished Quark compatibility");
            }
        } catch (Exception e) {
            LOG.error("Error loading QuarkModules");
        }
    }

    public static MesonInstance getInstance(String modId) {
        if (instances.containsKey(modId)) {
            return instances.get(modId);
        } else {
            throw new RuntimeException("No such instance " + modId);
        }
    }

    public static boolean isModuleEnabled(String res) {
        return isModuleEnabled(new ResourceLocation(res));
    }

    public static boolean isModuleEnabled(ResourceLocation res) {
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
}
