package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;

public interface RenderTooltipCallback {
    Event<RenderTooltipCallback> EVENT = EventFactory.createArrayBacked(RenderTooltipCallback.class, (listeners) -> (matrices, stack, lines, x, y) -> {
        for (RenderTooltipCallback listener : listeners) {
            listener.interact(matrices, stack, lines, x, y);
        }
    });

    void interact(PoseStack matrices, @Nullable ItemStack stack, List<ClientTooltipComponent> lines, int x, int y);
}
