package svenhjol.charm.feature.mooblooms;

import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.common.CommonFeature;

import java.util.function.Supplier;

public class MoobloomsClient extends ClientFeature {
    static Supplier<ModelLayerLocation> layer;

    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return Mooblooms.class;
    }

    @Override
    public void register() {
        var registry = mod().registry();

        layer = registry.modelLayer(
            () -> new ModelLayerLocation(mod().id("moobloom"), "main"), CowModel::createBodyLayer);

        registry.entityRenderer(Mooblooms.entity,
            () -> ctx -> new MoobloomEntityRenderer<>(ctx, new CowModel<>(ctx.bakeLayer(layer.get()))));
    }

    @Override
    public void runWhenEnabled() {
        mod().registry()
            .itemTab(Mooblooms.spawnEggItem, CreativeModeTabs.SPAWN_EGGS, Items.AXOLOTL_SPAWN_EGG);
    }
}
