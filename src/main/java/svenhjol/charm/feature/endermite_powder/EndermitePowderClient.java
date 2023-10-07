package svenhjol.charm.feature.endermite_powder;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmonyFeature;

@ClientFeature(mod = CharmClient.MOD_ID, feature = EndermitePowder.class)
public class EndermitePowderClient extends CharmonyFeature {
    @Override
    public void register() {
        var registry = CharmClient.instance().registry();
        registry.entityRenderer(EndermitePowder.entity, () -> EndermitePowderEntityRenderer::new);
    }

    @Override
    public void runWhenEnabled() {
        var registry = CharmClient.instance().registry();

        registry.itemTab(
            EndermitePowder.item,
            CreativeModeTabs.TOOLS_AND_UTILITIES,
            Items.ENDER_EYE
        );
    }
}
