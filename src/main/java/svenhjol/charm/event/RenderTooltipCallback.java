package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import net.minecraft.util.ActionResult;

import java.util.List;

public interface RenderTooltipCallback {
    Event<RenderTooltipCallback> EVENT = EventFactory.createArrayBacked(RenderTooltipCallback.class, (listeners) -> (matrices, stack, lines, x, y) -> {
        for (RenderTooltipCallback listener : listeners) {
            ActionResult result = listener.interact(matrices, stack, lines, x, y);
            if (result != ActionResult.PASS)
                return result;
        }

        return ActionResult.PASS;
    });

    ActionResult interact(MatrixStack matrices, ItemStack stack, List<? extends OrderedText> lines, int x, int y);
}
