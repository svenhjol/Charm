package svenhjol.charm.feature.hover_sorting.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.InteractionResult;
import svenhjol.charm.api.enums.SortDirection;
import svenhjol.charm.feature.hover_sorting.HoverSortingClient;
import svenhjol.charm.feature.hover_sorting.common.Networking;
import svenhjol.charm.foundation.feature.FeatureHolder;

public final class Handlers extends FeatureHolder<HoverSortingClient> {
    public Handlers(HoverSortingClient feature) {
        super(feature);
    }

    public InteractionResult mouseScroll(double direction) {
        var client = Minecraft.getInstance();
        if (client.level != null) {
            var screen = client.screen;
            if (screen instanceof AbstractContainerScreen<?> acs) {
                var hoveredSlot = acs.hoveredSlot;
                if (hoveredSlot == null) {
                    return InteractionResult.PASS;
                }

                Networking.C2SScrollOnHover.send(hoveredSlot.index,
                    direction > 0 ? SortDirection.UP : SortDirection.DOWN);

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }
}
