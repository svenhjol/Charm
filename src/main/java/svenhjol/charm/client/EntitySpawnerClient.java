package svenhjol.charm.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.module.EntitySpawner;

public class EntitySpawnerClient extends CharmClientModule {
    public EntitySpawnerClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        BlockRenderLayerMap.INSTANCE.putBlock(EntitySpawner.ENTITY_SPAWNER, RenderLayer.getCutout());
    }
}
