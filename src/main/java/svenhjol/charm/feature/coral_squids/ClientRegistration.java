package svenhjol.charm.feature.coral_squids;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.foundation.Registration;

public final class ClientRegistration extends Registration<CoralSquidsClient> {
    public ClientRegistration(CoralSquidsClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();

        CoralSquidsClient.layer = registry.modelLayer(
            () -> new ModelLayerLocation(registry.id("coral_squid"), "main"), CoralSquidEntityModel::getTexturedModelData);

        registry.entityRenderer(CoralSquids.entity, () ->
            ctx -> new CoralSquidEntityRenderer<>(ctx, new CoralSquidEntityModel<>(ctx.bakeLayer(CoralSquidsClient.layer.get()))));
    }

    @Override
    public void onEnabled() {
        var registry = feature.registry();

        registry.itemTab(CoralSquids.bucketItem, CreativeModeTabs.TOOLS_AND_UTILITIES, Items.AXOLOTL_BUCKET);
        registry.itemTab(CoralSquids.spawnEggItem, CreativeModeTabs.SPAWN_EGGS, Items.AXOLOTL_SPAWN_EGG);
    }
}
