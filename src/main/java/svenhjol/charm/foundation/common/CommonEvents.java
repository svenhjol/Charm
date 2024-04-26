package svenhjol.charm.foundation.common;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.api.event.BlockUseEvent;
import svenhjol.charm.api.event.LevelLoadEvent;
import svenhjol.charm.feature.recipes.SortingRecipeManager;

public final class CommonEvents {
    private static boolean initialized = false;

    private final CommonRegistry registry;

    public CommonEvents(CommonRegistry registry) {
        this.registry = registry;

        // These are events that are specific to an instance of a mod and its registry.
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

        // These are global events that any mod/feature can observe.
        ServerWorldEvents.LOAD.register(CommonEvents::handleServerWorldLoad);
        UseBlockCallback.EVENT.register(CommonEvents::handleUseBlock);

        initialized = true;
    }

    private void handleBrewingRecipeRegister(PotionBrewing.Builder builder) {
        for (var mix : registry.potionMixes()) {
            builder.addMix(mix.input(), mix.reagent().get(), mix.output());
        }
    }

    private static InteractionResult handleUseBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        return BlockUseEvent.INSTANCE.invoke(player, level, hand, hitResult);
    }

    private static void handleServerWorldLoad(MinecraftServer server, ServerLevel level) {
        LevelLoadEvent.INSTANCE.invoke(server, level);
    }
}
