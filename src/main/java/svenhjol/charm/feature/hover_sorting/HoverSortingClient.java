package svenhjol.charm.feature.hover_sorting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import svenhjol.charm.Charm;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_api.event.ItemHoverSortEvent;
import svenhjol.charm_api.event.MouseScrollEvent;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm.mixin.accessor.AbstractContainerScreenAccessor;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class HoverSortingClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.LOADER.isEnabled(HoverSorting.class));
    }

    @Override
    public void runWhenEnabled() {
        MouseScrollEvent.ON_SCREEN.handle(this::handleMouseScroll);
    }

    private void handleMouseScroll(double direction) {
        var client = Minecraft.getInstance();
        if (client != null && client.level != null) {
            var screen = client.screen;
            if (screen instanceof AbstractContainerScreen<?> acs) {
                var hoveredSlot = ((AbstractContainerScreenAccessor)acs).getHoveredSlot();
                if (hoveredSlot == null) return;

                HoverSortingNetwork.ScrollOnHover.send(hoveredSlot.index,
                    direction > 0 ? ItemHoverSortEvent.SortDirection.UP : ItemHoverSortEvent.SortDirection.DOWN);
            }
        }
    }
}
