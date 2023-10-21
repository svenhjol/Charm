package svenhjol.charm.feature.azalea_wood;

import net.minecraft.client.renderer.RenderType;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.feature.custom_wood.CustomWood;

public class AzaleaWoodClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return AzaleaWood.class;
    }

    @Override
    public void register() {
        var registry = mod().registry();
        var holder = CustomWood.getHolder(AzaleaMaterial.AZALEA);

        var door = holder.getDoor().orElseThrow();
        var trapdoor = holder.getTrapdoor().orElseThrow();

        // Cut out transparent areas of doors and trapdoors.
        registry.blockRenderType(door.block, RenderType::cutout);
        registry.blockRenderType(trapdoor.block, RenderType::cutout);
    }
}
