package svenhjol.charm.module.bundle_sorting;

import com.mojang.blaze3d.vertex.PoseStack;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.event.RenderTooltipCallback;
import svenhjol.charm.event.ScrollMouseCallback;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.mixin.accessor.AbstractContainerScreenAccessor;
import svenhjol.charm.module.ender_bundles.EnderBundles;

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
        if (opt.isEmpty())
            return;

        Minecraft client = opt.get();

        if (client.level != null && selectedBundle != null) {
            Screen screen = client.screen;
            if (screen instanceof AbstractContainerScreen) {
                Slot hoveredSlot = ((AbstractContainerScreenAccessor) screen).getHoveredSlot();

                FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
                data.writeInt(hoveredSlot.index);
                data.writeBoolean(direction > 0);
                ClientPlayNetworking.send(BundleSorting.MSG_SERVER_CYCLE_BUNDLE_CONTENTS, data);
            }
        }
    }

    private void handleRenderTooltip(PoseStack pose, @Nullable ItemStack stack, List<ClientTooltipComponent> lines, int x, int y) {
        if (stack != null && (stack.getItem() instanceof BundleItem || stack.getItem() == EnderBundles.ENDER_BUNDLE))
            selectedBundle = stack;
    }

    private void handleClientTick(Minecraft client) {
        selectedBundle = null;
    }
}
