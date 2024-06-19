package svenhjol.charm.feature.coral_squids.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.coral_squids.CoralSquidsClient;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<CoralSquidsClient> {
    public final Supplier<ModelLayerLocation> layer;

    public Registers(CoralSquidsClient feature) {
        super(feature);
        var registry = feature.registry();

        layer = registry.modelLayer(
            () -> new ModelLayerLocation(registry.id("coral_squid"), "main"), Model::getTexturedModelData);

        registry.entityRenderer(feature().linked().registers.entity, () ->
            ctx -> new EntityRenderer<>(ctx, new Model<>(ctx.bakeLayer(layer.get()))));
    }

    @Override
    public void onEnabled() {
        var registry = feature().registry();
        var common = feature().linked();

        registry.itemTab(
            common.registers.bucketItem.get(),
            CreativeModeTabs.TOOLS_AND_UTILITIES,
            Items.AXOLOTL_BUCKET
        );
        registry.itemTab(
            common.registers.spawnEggItem.get(),
            CreativeModeTabs.SPAWN_EGGS,
            Items.AXOLOTL_SPAWN_EGG
        );
    }
}
