package svenhjol.charm.feature.mooblooms.client;

import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.mooblooms.MoobloomsClient;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<MoobloomsClient> {
    public final Supplier<ModelLayerLocation> layer;

    public Registers(MoobloomsClient feature) {
        super(feature);
        var registry = feature().registry();

        layer = registry.modelLayer(
            () -> new ModelLayerLocation(feature().id("moobloom"), "main"), CowModel::createBodyLayer);

        registry.entityRenderer(feature().linked().registers.entity,
            () -> ctx -> new EntityRenderer<>(ctx, new CowModel<>(ctx.bakeLayer(layer.get()))));
    }

    @Override
    public void onEnabled() {
        feature().registry().itemTab(
            feature().linked().registers.spawnEggItem,
            CreativeModeTabs.SPAWN_EGGS,
            Items.MAGMA_CUBE_SPAWN_EGG
        );
    }
}
