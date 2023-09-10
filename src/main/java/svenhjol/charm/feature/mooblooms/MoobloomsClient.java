package svenhjol.charm.feature.mooblooms;

import net.minecraft.client.model.CowModel;
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
public class MoobloomsClient extends CharmFeature {
    static Supplier<ModelLayerLocation> layer;

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(Mooblooms.class));
    }

    @Override
    public void register() {
        var registry = CharmClient.instance().registry();

        layer = registry.modelLayer(
            () -> new ModelLayerLocation(Charm.instance().makeId("moobloom"), "main"), CowModel::createBodyLayer);

        registry.entityRenderer(Mooblooms.entity, () ->
            ctx -> new MoobloomEntityRenderer<>(ctx, new CowModel<>(ctx.bakeLayer(layer.get()))));

        if (isEnabled()) {
            registry.itemTab(Mooblooms.spawnEggItem, CreativeModeTabs.SPAWN_EGGS, Items.AXOLOTL_SPAWN_EGG);
        }
    }
}
