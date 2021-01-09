package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;

import javax.annotation.Nullable;
import java.util.List;

public interface RenderTooltipCallback {
    Event<RenderTooltipCallback> EVENT = EventFactory.createArrayBacked(RenderTooltipCallback.class, (listeners) -> (matrices, stack, lines, x, y) -> {
        for (RenderTooltipCallback listener : listeners) {
            listener.interact(matrices, stack, lines, x, y);
        }
    });

    void interact(MatrixStack matrices, @Nullable ItemStack stack, List<? extends OrderedText> lines, int x, int y);
}
