package svenhjol.charm.feature.cooking_pots.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.cooking_pots.CookingPotsClient;
import svenhjol.charm.feature.cooking_pots.common.Networking;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.List;

public final class Registers extends RegisterHolder<CookingPotsClient> {
    public Registers(CookingPotsClient feature) {
        super(feature);

        feature.registry().packetReceiver(Networking.S2CAddedToCookingPot.TYPE,
            () -> feature.handlers::handleAddedToCookingPot);
    }

    @Override
    public void onEnabled() {
        var registry = feature().registry();
        var common = feature().common();

        registry.blockColor(feature().handlers::handleBlockColor, List.of(common.registers.block));
        registry.itemTab(common.registers.block, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.CAULDRON);
        registry.itemTab(common.registers.mixedStewItem, CreativeModeTabs.FOOD_AND_DRINKS, Items.RABBIT_STEW);
    }
}
