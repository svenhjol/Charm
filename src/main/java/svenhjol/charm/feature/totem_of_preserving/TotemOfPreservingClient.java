package svenhjol.charm.feature.totem_of_preserving;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.common.CommonFeature;

public class TotemOfPreservingClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return TotemOfPreserving.class;
    }

    @Override
    public void register() {
        var registry = mod().registry();

        registry.blockEntityRenderer(
            TotemOfPreserving.blockEntity,
            () -> TotemBlockEntityRenderer::new);

        registry.blockRenderType(
            TotemOfPreserving.block,
            RenderType::cutout);
    }

    @Override
    public void runWhenEnabled() {
        var registry = mod().registry();

        registry.itemTab(
            TotemOfPreserving.item,
            CreativeModeTabs.COMBAT,
            Items.TOTEM_OF_UNDYING
        );
    }
}
