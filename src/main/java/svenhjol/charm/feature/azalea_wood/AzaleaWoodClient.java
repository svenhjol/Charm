package svenhjol.charm.feature.azalea_wood;

import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm.feature.custom_wood.CustomWood;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmFeature;

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
        var holder = CustomWood.getHolder(AzaleaMaterial.AZALEA);

        var door = holder.getDoor().orElseThrow();
        var trapdoor = holder.getTrapdoor().orElseThrow();

        // Cut out transparent areas of doors and trapdoors.
        registry.blockRenderType(door.block, RenderType::cutout);
        registry.blockRenderType(trapdoor.block, RenderType::cutout);
    }
}
