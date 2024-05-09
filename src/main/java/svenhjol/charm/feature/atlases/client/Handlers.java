package svenhjol.charm.feature.atlases.client;

import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.atlases.AtlasesClient;
import svenhjol.charm.feature.atlases.common.AtlasInventory;
import svenhjol.charm.foundation.feature.FeatureHolder;

public final class Handlers extends FeatureHolder<AtlasesClient> {
    int swappedSlot = -1;

    public Handlers(AtlasesClient feature) {
        super(feature);
    }

    /**
     * Callback from {@link CartographyTableScreenMixin} to check
     * if the cartography table contains a map or an atlas.
     * @param screen The cartography table screen.
     * @return True if the cartography table contains a map or an atlas.
     */
    public boolean shouldDrawAtlasCopy(CartographyTableScreen screen) {
        return screen.getMenu().getSlot(0).getItem().getItem() == feature().common.registers.item.get()
            && screen.getMenu().getSlot(1).getItem().getItem() == Items.MAP;
    }


    public void handleUpdateInventory(Player player, svenhjol.charm.feature.atlases.common.Networking.S2CUpdateInventory packet) {
        var slot = packet.slot();
        ItemStack atlas = player.getInventory().getItem(slot);
        AtlasInventory.get(player.level(), atlas).reload(atlas);
    }

    @SuppressWarnings("unused")
    public void handleSwappedSlot(Player player, svenhjol.charm.feature.atlases.common.Networking.S2CSwappedAtlasSlot packet) {
        swappedSlot = packet.slot();
    }
}
