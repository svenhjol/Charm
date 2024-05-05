package svenhjol.charm.feature.totem_of_preserving.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreserving;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreservingClient;
import svenhjol.charm.foundation.feature.Register;

public final class Registers extends Register<TotemOfPreservingClient> {
    public Registers(TotemOfPreservingClient feature) {
        super(feature);
        var registry = feature.registry();

        registry.blockEntityRenderer(
            TotemOfPreserving.registers.blockEntity,
            () -> BlockEntityRenderer::new);

        registry.blockRenderType(
            TotemOfPreserving.registers.block,
            RenderType::cutout);
    }

    @Override
    public void onRegister() {
    }

    @Override
    public void onEnabled() {
        feature.registry().itemTab(
            TotemOfPreserving.registers.item,
            CreativeModeTabs.COMBAT,
            Items.TOTEM_OF_UNDYING
        );
    }
}
