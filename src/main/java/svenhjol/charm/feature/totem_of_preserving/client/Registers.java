package svenhjol.charm.feature.totem_of_preserving.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreservingClient;

public final class Registers extends RegisterHolder<TotemOfPreservingClient> {
    public Registers(TotemOfPreservingClient feature) {
        super(feature);
        var registry = feature.registry();

        registry.blockEntityRenderer(
            feature().linked().registers.blockEntity,
            () -> BlockEntityRenderer::new);

        registry.blockRenderType(
            feature().linked().registers.block,
            RenderType::cutout);
    }

    @Override
    public void onEnabled() {
        feature().registry().itemTab(
            feature().linked().registers.item,
            CreativeModeTabs.COMBAT,
            Items.TOTEM_OF_UNDYING
        );
    }
}
