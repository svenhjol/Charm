package svenhjol.charm.module.hover_sorting;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.event.RenderTooltipCallback;
import svenhjol.charm.event.ScrollMouseCallback;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.hover_sorting.network.ClientSendScrolledOnHover;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@ClientModule(module = HoverSorting.class)
public class HoverSortingClient extends CharmModule {
    public static ItemStack hoveredItem = null;

    public static ClientSendScrolledOnHover CLIENT_SEND_SCROLLED_ON_HOVER;

    @Override
    public void register() {
        ScrollMouseCallback.EVENT.register(this::handleScrollDirection);
        RenderTooltipCallback.EVENT.register(this::handleRenderTooltip);
        ClientTickEvents.END_CLIENT_TICK.register(this::handleClientTick);
        ScreenEvents.BEFORE_INIT.register(this::handleScreenInit);
    }

    @Override
    public void runWhenEnabled() {
        CLIENT_SEND_SCROLLED_ON_HOVER = new ClientSendScrolledOnHover();
    }

    private void handleScreenInit(Minecraft minecraft, Screen screen, int width, int height) {
        if (!(screen instanceof AbstractContainerScreen)) return;
        ScreenKeyboardEvents.beforeKeyPress(screen).register(this::handleKeyPress);
    }

    private void handleKeyPress(Screen screen, int key, int scancode, int modifiers) {
        Options options = Minecraft.getInstance().options;
        if (key == InputConstants.KEY_LEFT || options.keyLeft.matches(key, scancode)) {
            handleScrollDirection(-1);
        } else if (key == InputConstants.KEY_RIGHT || options.keyRight.matches(key, scancode)) {
            handleScrollDirection(1);
        }
    }

    private void handleScrollDirection(double direction) {
        Optional<Minecraft> opt = ClientHelper.getClient();
        if (opt.isEmpty()) return;

        Minecraft client = opt.get();

        if (client.level != null && hoveredItem != null) {
            Screen screen = client.screen;
            if (screen instanceof AbstractContainerScreen) {
                Slot hoveredSlot = ((AbstractContainerScreen<?>)screen).hoveredSlot;
                if (hoveredSlot == null) return;
                CLIENT_SEND_SCROLLED_ON_HOVER.send(hoveredSlot.index, direction > 0);
            }
        }
    }

    private void handleRenderTooltip(Screen screen, PoseStack pose, @Nullable ItemStack stack, List<ClientTooltipComponent> lines, int x, int y) {
        if (stack != null) {
            Item item = stack.getItem();
            if (HoverSorting.SORTABLE.contains(item) || HoverSorting.SORTABLE.contains(Block.byItem(item))) {
                hoveredItem = stack;
            }
        }
    }

    private void handleClientTick(Minecraft client) {
        hoveredItem = null;
    }
}
