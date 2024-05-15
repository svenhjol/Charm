package svenhjol.charm.feature.endermite_powder.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.endermite_powder.EndermitePowderClient;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<EndermitePowderClient> {
    public Registers(EndermitePowderClient feature) {
        super(feature);

        feature().registry().entityRenderer(feature().common().registers.entity, () -> EntityRenderer::new);
    }

    @Override
    public void onEnabled() {
        feature().registry().itemTab(
            feature().common().registers.item,
            CreativeModeTabs.TOOLS_AND_UTILITIES,
            Items.ENDER_EYE
        );
    }
}
