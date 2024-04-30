package svenhjol.charm.foundation.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.api.event.ClientEntityJoinEvent;
import svenhjol.charm.api.event.ClientStartEvent;
import svenhjol.charm.api.event.HeldItemRenderEvent;
import svenhjol.charm.api.event.KeyPressEvent;
import svenhjol.charm.foundation.Log;
import svenhjol.charm.foundation.event.RenderHeldItemCallback;

public final class ClientEvents {
    private static final Log LOGGER = new Log(Charm.ID, "ClientEvents");
    private static boolean initialized = false;

    private final ClientRegistry registry;

    public ClientEvents(ClientRegistry registry) {
        this.registry = registry;

        // Spin up a dedicated listener for key presses.
        ClientTickEvents.END_CLIENT_TICK.register(this::handleKeyPresses);
    }

    public static void runOnce() {
        if (initialized) return;

        ClientEntityEvents.ENTITY_LOAD.register(ClientEvents::handleClientEntityLoad);
        ClientLifecycleEvents.CLIENT_STARTED.register(ClientEvents::handleClientStarted);
        RenderHeldItemCallback.EVENT.register(ClientEvents::handleRenderHeldItem);

        initialized = true;
    }

    private static void handleClientEntityLoad(Entity entity, ClientLevel level) {
        ClientEntityJoinEvent.INSTANCE.invoke(entity, level);
    }

    private static void handleClientStarted(Minecraft client) {
        ClientStartEvent.INSTANCE.invoke(client);
    }

    private static InteractionResult handleRenderHeldItem(float tickDelta, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equipProgress, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        for (var handler : HeldItemRenderEvent.INSTANCE.getHandlers()) {
            var result = handler.run(tickDelta, pitch, hand, swingProgress, stack, equipProgress, poseStack, bufferSource, light);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }
        return InteractionResult.PASS;
    }

    private void handleKeyPresses(Minecraft client) {
        var handlers = KeyPressEvent.INSTANCE.getHandlers();
        var keyMappings = registry.keyMappings();

        for (var id : keyMappings.keySet()) {
            var mapping = keyMappings.get(id);
            while (mapping.consumeClick()) {
                handlers.forEach(handler -> handler.run(id));
            }
        }
    }
}
