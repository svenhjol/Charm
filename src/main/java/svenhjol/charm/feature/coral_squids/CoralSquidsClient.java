package svenhjol.charm.feature.coral_squids;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@ClientFeature
public class CoralSquidsClient extends CharmFeature {
    private static Supplier<ModelLayerLocation> layer;

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(CoralSquids.class));
    }

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
