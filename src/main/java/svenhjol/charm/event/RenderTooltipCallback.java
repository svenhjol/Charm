package svenhjol.charm.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public interface RenderTooltipCallback {
    Event<RenderTooltipCallback> EVENT = EventFactory.createArrayBacked(RenderTooltipCallback.class, (listeners) -> (screen, poseStack, stack, lines, x, y) -> {
        for (RenderTooltipCallback listener : listeners) {
            listener.interact(screen, poseStack, stack, lines, x, y);
        }
    });

    void interact(Screen screen, PoseStack poseStack, @Nullable ItemStack stack, List<ClientTooltipComponent> lines, int x, int y);
}
