package svenhjol.charm.feature.raid_horns.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.raid_horns.RaidHornsClient;
import svenhjol.charm.foundation.feature.RegisterHolder;

@SuppressWarnings("deprecation")
public final class Registers extends RegisterHolder<RaidHornsClient> {
    public Registers(RaidHornsClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        var registry = feature().registry();

        registry.itemProperties("minecraft:tooting",
            feature().common().registers.item, () -> feature().handlers::handleTooting);

        registry.itemTab(
            feature().common().registers.item,
            CreativeModeTabs.TOOLS_AND_UTILITIES,
            Items.TNT_MINECART
        );
    }
}
