package svenhjol.charm.feature.coral_squids;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmonyFeature;

import java.util.function.Supplier;

@ClientFeature(mod = CharmClient.MOD_ID, feature = CoralSquids.class)
public class CoralSquidsClient extends CharmonyFeature {
    static Supplier<ModelLayerLocation> layer;

    @Override
    public void register() {
        var registry = CharmClient.instance().registry();

        layer = registry.modelLayer(
            () -> new ModelLayerLocation(Charm.instance().makeId("coral_squid"), "main"), CoralSquidEntityModel::getTexturedModelData);

        registry.entityRenderer(CoralSquids.entity, () ->
            ctx -> new CoralSquidEntityRenderer<>(ctx, new CoralSquidEntityModel<>(ctx.bakeLayer(layer.get()))));

        if (isEnabled()) {
            registry.itemTab(CoralSquids.bucketItem, CreativeModeTabs.TOOLS_AND_UTILITIES, Items.AXOLOTL_BUCKET);
            registry.itemTab(CoralSquids.spawnEggItem, CreativeModeTabs.SPAWN_EGGS, Items.AXOLOTL_SPAWN_EGG);
        }
    }
}
