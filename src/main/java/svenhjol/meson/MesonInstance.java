package svenhjol.meson;

import com.google.common.collect.ArrayListMultimap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;
import svenhjol.meson.handler.LogHandler;
import svenhjol.meson.handler.PacketHandler;
import svenhjol.meson.loader.ModuleLoader;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Meson modules (Charm, Strange, etc.) should extend this.
 * The constructor creates a module-specific loader and packethander.
 * You can override the forge startup methods if needed.
 * Put all init/earlyinit stuff in the constructor.
 */
public class MesonInstance {
    public LogHandler log;

    private String id;
    private ModuleLoader moduleLoader;
    private PacketHandler packetHandler;
    public ArrayListMultimap<Class<?>, Supplier<IForgeRegistryEntry<?>>> registerQueue = ArrayListMultimap.create();

    public MesonInstance(String id, LogHandler log) {
        this.id = id;
        this.log = log;

        Meson.INSTANCE.register(this);

        this.moduleLoader = new ModuleLoader(this);
        this.packetHandler = new PacketHandler(this);

        // run on both sides
        try {
            forEachModule(MesonModule::init);
            moduleLoader.refreshConfig();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to initialize modules: " + e.getMessage());
        }

        // run on client side only
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> forEachModule(MesonModule::initClient));
    }

    public static ModuleLoader getModuleLoader(String modId) {
        return Meson.getInstance(modId).getModuleLoader();
    }

    public static PacketHandler getPacketHandler(String modId) {
        return Meson.getInstance(modId).getPacketHandler();
    }

    public String getId() {
        return id;
    }

    public PacketHandler getPacketHandler() {
        return packetHandler;
    }

    public ModuleLoader getModuleLoader() {
        return moduleLoader;
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        moduleLoader.refreshConfig();
        moduleLoader.refreshShouldRunSetup();

        forEachEnabledModule(module -> {
            log.info("Loading module " + module.getName());
            if (module.hasSubscriptions)
                MinecraftForge.EVENT_BUS.register(module);

            module.onCommonSetup(event);
        });
    }

    public void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        forEachEnabledModule(module -> module.onServerAboutToStart(event));
    }

    public void onServerStarting(FMLServerStartingEvent event) {
        forEachEnabledModule(module -> module.onServerStarting(event));
    }

    public void onServerStarted(FMLServerStartedEvent event) {
        forEachEnabledModule(module -> module.onServerStarted(event));
    }

    public void onModConfig(ModConfig.ModConfigEvent event) {
        moduleLoader.refreshShouldRunSetup();

        forEachEnabledModule(module -> module.onModConfig(event));
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        forEachEnabledModule(module -> module.onClientSetup(event));
    }

    public void onLoadComplete(FMLLoadCompleteEvent event) {
        forEachEnabledModule(module -> module.onLoadComplete(event));
    }

    public void forEachModule(Consumer<MesonModule> consumer) {
        List<MesonModule> modules = moduleLoader.getModules();
        modules.forEach(consumer);
    }

    public void forEachEnabledModule(Consumer<MesonModule> consumer) {
        List<MesonModule> enabledModules = moduleLoader.getEnabledModules();
        enabledModules.forEach(consumer);
    }

    public boolean isModuleEnabled(String module) {
        return moduleLoader.isModuleEnabled(module);
    }
}
