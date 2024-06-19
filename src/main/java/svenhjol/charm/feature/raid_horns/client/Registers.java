package svenhjol.charm.feature.raid_horns.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.raid_horns.RaidHornsClient;

@SuppressWarnings("deprecation")
public final class Registers extends RegisterHolder<RaidHornsClient> {
    public Registers(RaidHornsClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        var registry = feature().registry();

        registry.itemProperties("minecraft:tooting",
            feature().linked().registers.item, () -> feature().handlers::handleTooting);

        registry.itemTab(
            feature().linked().registers.item.get(),
            CreativeModeTabs.TOOLS_AND_UTILITIES,
            Items.TNT_MINECART
        );
    }
}
