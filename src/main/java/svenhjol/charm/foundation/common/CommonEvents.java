package svenhjol.charm.foundation.common;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.alchemy.PotionBrewing;
import svenhjol.charm.api.event.LevelLoadEvent;
import svenhjol.charm.feature.recipes.SortingRecipeManager;

public final class CommonEvents {
    private static boolean initialized = false;

    private final CommonRegistry registry;

    public CommonEvents(CommonRegistry registry) {
        this.registry = registry;

        FabricBrewingRecipeRegistryBuilder.BUILD.register(this::handleBrewingRecipeRegister);
    }

    public String id() {
        return registry.id();
    }

    public static void runOnce() {
        if (initialized) return;

        // Add our custom recipe sorting handler - should only be added once!
        ResourceManagerHelper.get(PackType.SERVER_DATA)
            .registerReloadListener(new SortingRecipeManager());

        ServerWorldEvents.LOAD.register(CommonEvents::handleServerWorldLoad);

        initialized = true;
    }

    private void handleBrewingRecipeRegister(PotionBrewing.Builder builder) {
        for (var mix : registry.potionMixes()) {
            builder.addMix(mix.input(), mix.reagent().get(), mix.output());
        }
    }

    static void handleServerWorldLoad(MinecraftServer server, ServerLevel level) {
        LevelLoadEvent.INSTANCE.invoke(server, level);
    }
}
