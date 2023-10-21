package svenhjol.charm.feature.coral_squids;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.common.CommonFeature;

import java.util.function.Supplier;

public class CoralSquidsClient extends ClientFeature {
    static Supplier<ModelLayerLocation> layer;

    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return CoralSquids.class;
    }

    @Override
    public void register() {
        var registry = mod().registry();

        layer = registry.modelLayer(
            () -> new ModelLayerLocation(mod().id("coral_squid"), "main"), CoralSquidEntityModel::getTexturedModelData);

        registry.entityRenderer(CoralSquids.entity, () ->
            ctx -> new CoralSquidEntityRenderer<>(ctx, new CoralSquidEntityModel<>(ctx.bakeLayer(layer.get()))));

        if (isEnabled()) {
            registry.itemTab(CoralSquids.bucketItem, CreativeModeTabs.TOOLS_AND_UTILITIES, Items.AXOLOTL_BUCKET);
            registry.itemTab(CoralSquids.spawnEggItem, CreativeModeTabs.SPAWN_EGGS, Items.AXOLOTL_SPAWN_EGG);
        }
    }
}
