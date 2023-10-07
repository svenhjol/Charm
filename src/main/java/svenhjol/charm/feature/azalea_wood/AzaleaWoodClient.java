package svenhjol.charm.feature.azalea_wood;

import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.feature.custom_wood.CustomWood;

@ClientFeature(mod = CharmClient.MOD_ID, feature = AzaleaWood.class)
public class AzaleaWoodClient extends CharmonyFeature {
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
