package svenhjol.charm.feature.endermite_powder;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class EndermitePowderClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(EndermitePowder.class));
    }

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
