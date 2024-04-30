package svenhjol.charm.feature.atlases;

import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import net.minecraft.world.item.Items;
import svenhjol.charm.mixin.feature.atlases.CartographyTableScreenMixin;

public class ClientCallbacks {
    /**
     * Callback from {@link CartographyTableScreenMixin} to check
     * if the cartography table contains a map or an atlas.
     * @param screen The cartography table screen.
     * @return True if the cartography table contains a map or an atlas.
     */
    public static boolean shouldDrawAtlasCopy(CartographyTableScreen screen) {
        return screen.getMenu().getSlot(0).getItem().getItem() == Atlases.item.get()
            && screen.getMenu().getSlot(1).getItem().getItem() == Items.MAP;
    }
}
