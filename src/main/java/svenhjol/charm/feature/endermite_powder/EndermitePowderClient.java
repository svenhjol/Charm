package svenhjol.charm.feature.endermite_powder;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.common.CommonFeature;

public class EndermitePowderClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return EndermitePowder.class;
    }

    @Override
    public void register() {
        mod().registry().entityRenderer(EndermitePowder.entity, () -> EndermitePowderEntityRenderer::new);
    }

    @Override
    public void runWhenEnabled() {
        mod().registry().itemTab(
            EndermitePowder.item,
            CreativeModeTabs.TOOLS_AND_UTILITIES,
            Items.ENDER_EYE
        );
    }
}
