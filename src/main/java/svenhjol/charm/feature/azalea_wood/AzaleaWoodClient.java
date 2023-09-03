package svenhjol.charm.feature.azalea_wood;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class AzaleaWoodClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(AzaleaWood.class));
    }

    @Override
    public void register() {
        var registry = CharmClient.instance().registry();

        // Cut out transparent areas of blocks.
        registry.blockRenderType(AzaleaWood.DOOR_BLOCK, RenderType::cutout);
        registry.blockRenderType(AzaleaWood.TRAPDOOR_BLOCK, RenderType::cutout);

        // Register boat models.
        registry.modelLayer(
            () -> new ModelLayerLocation(Charm.instance().makeId("boat/azalea"), "main"),
            BoatModel::createBodyModel);

        registry.modelLayer(
            () -> new ModelLayerLocation(Charm.instance().makeId("chest_boat/azalea"), "main"),
            ChestBoatModel::createBodyModel);

        // Register sign material.
        registry.signMaterial(AzaleaWood.WOOD_TYPE);
    }
}
