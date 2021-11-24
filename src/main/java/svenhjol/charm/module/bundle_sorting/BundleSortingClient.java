package svenhjol.charm.module.bundle_sorting;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.event.RenderTooltipCallback;
import svenhjol.charm.event.ScrollMouseCallback;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.helper.NetworkHelper;
import svenhjol.charm.loader.CharmModule;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@ClientModule(module = BundleSorting.class)
public class BundleSortingClient extends CharmModule {
    public static ItemStack selectedBundle = null;

    @Override
    public void register() {
        ScrollMouseCallback.EVENT.register(this::handleScrollMouse);
        RenderTooltipCallback.EVENT.register(this::handleRenderTooltip);
        ClientTickEvents.END_CLIENT_TICK.register(this::handleClientTick);
    }

    private void handleScrollMouse(double direction) {
        Optional<Minecraft> opt = ClientHelper.getClient();
        if (opt.isEmpty()) return;

        Minecraft client = opt.get();

        if (client.level != null && selectedBundle != null) {
            Screen screen = client.screen;
            if (screen instanceof AbstractContainerScreen) {
                Slot hoveredSlot = ((AbstractContainerScreen<?>)screen).hoveredSlot;
                if (hoveredSlot == null) return;

                NetworkHelper.sendPacketToServer(BundleSorting.MSG_SERVER_SCROLLED_ON_HOVER,
                    buf -> {
                        buf.writeInt(hoveredSlot.index);
                        buf.writeBoolean(direction > 0);
                    });
            }
        }
    }

    private void handleRenderTooltip(PoseStack pose, @Nullable ItemStack stack, List<ClientTooltipComponent> lines, int x, int y) {
        if (stack != null && BundleSorting.SORTABLE.contains(stack.getItem())) {
            selectedBundle = stack;
        }
    }

    private void handleClientTick(Minecraft client) {
        selectedBundle = null;
    }
}
