package svenhjol.charm.feature.totem_of_preserving;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.foundation.Registration;

public final class ClientRegistration extends Registration<TotemOfPreservingClient> {
    public ClientRegistration(TotemOfPreservingClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();

        registry.blockEntityRenderer(
            TotemOfPreserving.blockEntity,
            () -> TotemBlockEntityRenderer::new);

        registry.blockRenderType(
            TotemOfPreserving.block,
            RenderType::cutout);
    }

    @Override
    public void onEnabled() {
        feature.registry().itemTab(
            TotemOfPreserving.item,
            CreativeModeTabs.COMBAT,
            Items.TOTEM_OF_UNDYING
        );
    }
}
