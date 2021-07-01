package svenhjol.charm.module.entity_spawners;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.loader.ClientModule;

@svenhjol.charm.annotation.ClientModule(module = EntitySpawners.class)
public class EntitySpawnersClient extends ClientModule {

    @Override
    public void register() {
        BlockRenderLayerMap.INSTANCE.putBlock(EntitySpawners.ENTITY_SPAWNER, RenderType.cutout());
    }
}
