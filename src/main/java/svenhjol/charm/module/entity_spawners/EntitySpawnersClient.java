package svenhjol.charm.module.entity_spawners;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;

public class EntitySpawnersClient extends CharmClientModule {
    public EntitySpawnersClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        BlockRenderLayerMap.INSTANCE.putBlock(EntitySpawners.ENTITY_SPAWNER, RenderLayer.getCutout());
    }
}
