package svenhjol.charm.feature.azalea_wood;

import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.feature.custom_wood.CustomWood;
import svenhjol.charm.foundation.Register;

public class ClientRegister extends Register<AzaleaWoodClient> {
    public ClientRegister(AzaleaWoodClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();
        var holder = CustomWood.getHolder(AzaleaMaterial.AZALEA);

        var door = holder.getDoor().orElseThrow();
        var trapdoor = holder.getTrapdoor().orElseThrow();

        // Cut out transparent areas of doors and trapdoors.
        registry.blockRenderType(door.block, RenderType::cutout);
        registry.blockRenderType(trapdoor.block, RenderType::cutout);
    }
}
