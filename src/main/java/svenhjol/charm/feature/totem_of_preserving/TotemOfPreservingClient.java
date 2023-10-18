package svenhjol.charm.feature.totem_of_preserving;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmonyFeature;

@ClientFeature(mod = CharmClient.MOD_ID, feature = TotemOfPreserving.class)
public class TotemOfPreservingClient extends CharmonyFeature {
    @Override
    public void register() {
        var registry = CharmClient.instance().registry();

        registry.blockEntityRenderer(
            TotemOfPreserving.blockEntity,
            () -> TotemBlockEntityRenderer::new);

        registry.blockRenderType(
            TotemOfPreserving.block,
            RenderType::cutout);
    }

    @Override
    public void runWhenEnabled() {
        var registry = CharmClient.instance().registry();

        registry.itemTab(
            TotemOfPreserving.item,
            CreativeModeTabs.COMBAT,
            Items.TOTEM_OF_UNDYING
        );
    }
}
