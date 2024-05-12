package svenhjol.charm.feature.coral_squids.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.coral_squids.CoralSquids;
import svenhjol.charm.feature.coral_squids.CoralSquidsClient;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<CoralSquidsClient> {
    public final CoralSquids common;
    public final Supplier<ModelLayerLocation> layer;

    public Registers(CoralSquidsClient feature) {
        super(feature);
        var registry = feature.registry();
        common = feature.common;

        layer = registry.modelLayer(
            () -> new ModelLayerLocation(registry.id("coral_squid"), "main"), Model::getTexturedModelData);

        registry.entityRenderer(common.registers.entity, () ->
            ctx -> new EntityRenderer<>(ctx, new Model<>(ctx.bakeLayer(layer.get()))));
    }

    @Override
    public void onEnabled() {
        var registry = feature().registry();

        registry.itemTab(
            common.registers.bucketItem,
            CreativeModeTabs.TOOLS_AND_UTILITIES,
            Items.AXOLOTL_BUCKET
        );
        registry.itemTab(
            common.registers.spawnEggItem,
            CreativeModeTabs.SPAWN_EGGS,
            Items.AXOLOTL_SPAWN_EGG
        );
    }
}
