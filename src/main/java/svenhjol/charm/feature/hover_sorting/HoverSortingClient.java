package svenhjol.charm.feature.hover_sorting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.InteractionResult;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony_api.event.ItemHoverSortEvent;
import svenhjol.charmony_api.event.MouseScrollEvent;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class HoverSortingClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(HoverSorting.class));
    }

    @Override
    public void runWhenEnabled() {
        MouseScrollEvent.ON_SCREEN.handle(this::handleMouseScroll);
    }

    private InteractionResult handleMouseScroll(double direction) {
        var client = Minecraft.getInstance();
        if (client.level != null) {
            var screen = client.screen;
            if (screen instanceof AbstractContainerScreen<?> acs) {
                var hoveredSlot = acs.hoveredSlot;
                if (hoveredSlot == null) {
                    return InteractionResult.PASS;
                }

                HoverSortingNetwork.ScrollOnHover.send(hoveredSlot.index,
                    direction > 0 ? ItemHoverSortEvent.SortDirection.UP : ItemHoverSortEvent.SortDirection.DOWN);

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}
