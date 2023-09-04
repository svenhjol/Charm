package svenhjol.charm.feature.variant_chest_boats;

import net.minecraft.world.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class VariantChestBoatsClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(VariantChestBoats.class));
    }

    public void register() {
        CharmClient.instance().registry().itemColor(VariantChestBoatsClient::handleItemColor,
            new ArrayList<>(VariantChestBoats.CHEST_BOAT_MAP_SUPPLIER.values()));
    }

    private static int handleItemColor(ItemStack stack, int layer) {
        return layer == 0 ? -1 : VariantChestBoats.getLayerColor(stack);
    }
}
