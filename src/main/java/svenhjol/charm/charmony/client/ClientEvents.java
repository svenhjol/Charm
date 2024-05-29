package svenhjol.charm.charmony.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.api.event.*;
import svenhjol.charm.charmony.Charmony;
import svenhjol.charm.charmony.Log;
import svenhjol.charm.charmony.client.event.RenderHeldItemCallback;
import svenhjol.charm.charmony.client.event.RenderScreenCallback;
import svenhjol.charm.charmony.client.event.RenderTooltipCallback;
import svenhjol.charm.charmony.client.event.SetupScreenCallback;

import java.util.List;

public final class ClientEvents {
    private static final Log LOGGER = new Log(Charmony.ID, "ClientEvents");
    private static boolean initialized = false;

    private final ClientLoader loader;
    private final ClientRegistry registry;

    public ClientEvents(ClientLoader loader) {
        this.loader = loader;
        this.registry = loader.registry();

        // Spin up a dedicated listener for key presses.
        ClientTickEvents.END_CLIENT_TICK.register(this::handleKeyPresses);

        // Ensures global events are set up.
        runOnce();
    }

    public static void runOnce() {
        if (initialized) return;

        ClientEntityEvents.ENTITY_LOAD.register(ClientEvents::handleClientEntityLoad);
        ClientLifecycleEvents.CLIENT_STARTED.register(ClientEvents::handleClientStarted);
        HudRenderCallback.EVENT.register(ClientEvents::handleHudRender);
        RenderHeldItemCallback.EVENT.register(ClientEvents::handleRenderHeldItem);
        RenderScreenCallback.EVENT.register(ClientEvents::handleRenderScreen);
        RenderTooltipCallback.EVENT.register(ClientEvents::handleRenderTooltip);
        SetupScreenCallback.EVENT.register(ClientEvents::handleSetupScreen);

        LOGGER.debug("Called runOnce");
        initialized = true;
    }

    private static void handleClientEntityLoad(Entity entity, ClientLevel level) {
        ClientEntityJoinEvent.INSTANCE.invoke(entity, level);
    }

    private static void handleClientStarted(Minecraft client) {
        ClientStartEvent.INSTANCE.invoke(client);
    }

    private static void handleHudRender(GuiGraphics guiGraphics, DeltaTracker tickDelta) {
        HudRenderEvent.INSTANCE.getHandlers().forEach(
            handler -> handler.run(guiGraphics, tickDelta));
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

    private static void handleRenderScreen(AbstractContainerScreen<?> container, GuiGraphics guiGraphics, int mouseX, int mouseY) {
        ScreenRenderEvent.INSTANCE.getHandlers().forEach(
            handler -> handler.run(container, guiGraphics, mouseX, mouseY));
    }

    private static void handleSetupScreen(Screen screen) {
        ScreenSetupEvent.INSTANCE.getHandlers().forEach(
            handler -> handler.run(screen));
    }

    private static void handleRenderTooltip(GuiGraphics guiGraphics, ItemStack itemStack, List<ClientTooltipComponent> lines, int x, int y) {
        TooltipRenderEvent.INSTANCE.invoke(guiGraphics, lines, x, y, itemStack);
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
