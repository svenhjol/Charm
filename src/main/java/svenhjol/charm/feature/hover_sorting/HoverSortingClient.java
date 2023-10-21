package svenhjol.charm.feature.hover_sorting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.InteractionResult;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony_api.event.ItemHoverSortEvent;
import svenhjol.charmony_api.event.MouseScrollEvent;

public class HoverSortingClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return HoverSorting.class;
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
