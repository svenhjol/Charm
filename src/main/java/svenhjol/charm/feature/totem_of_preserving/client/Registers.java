package svenhjol.charm.feature.totem_of_preserving.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreservingClient;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<TotemOfPreservingClient> {
    public Registers(TotemOfPreservingClient feature) {
        super(feature);
        var registry = feature.registry();

        registry.blockEntityRenderer(
            feature().common.registers.blockEntity,
            () -> BlockEntityRenderer::new);

        registry.blockRenderType(
            feature().common.registers.block,
            RenderType::cutout);
    }

    @Override
    public void onEnabled() {
        feature().registry().itemTab(
            feature().common.registers.item,
            CreativeModeTabs.COMBAT,
            Items.TOTEM_OF_UNDYING
        );
    }
}
