package svenhjol.charm.feature.item_hover_sorting.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.InteractionResult;
import svenhjol.charm.charmony.enums.SortDirection;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.item_hover_sorting.ItemHoverSortingClient;
import svenhjol.charm.feature.item_hover_sorting.common.Networking;

public final class Handlers extends FeatureHolder<ItemHoverSortingClient> {
    public Handlers(ItemHoverSortingClient feature) {
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
