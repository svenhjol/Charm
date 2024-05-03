package svenhjol.charm.feature.hover_sorting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.InteractionResult;
import svenhjol.charm.api.event.MouseScrollEvent;
import svenhjol.charm.foundation.Registration;

public class ClientRegistration extends Registration<HoverSortingClient> {
    public ClientRegistration(HoverSortingClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
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

                CommonNetworking.C2SScrollOnHover.send(hoveredSlot.index,
                    direction > 0 ? SortDirection.UP : SortDirection.DOWN);

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}
