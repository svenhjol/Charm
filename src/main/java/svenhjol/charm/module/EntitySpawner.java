package svenhjol.charm.module;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.Charm;
import svenhjol.charm.block.EntitySpawnerBlock;
import svenhjol.charm.blockentity.EntitySpawnerBlockEntity;

@Module(mod = Charm.MOD_ID, description = "Spawns entities when a player is within range.", alwaysEnabled = true)
public class EntitySpawner extends CharmModule {
    public static final Identifier ID = new Identifier(Charm.MOD_ID, "entity_spawner");
    public static EntitySpawnerBlock ENTITY_SPAWNER;
    public static BlockEntityType<EntitySpawnerBlockEntity> BLOCK_ENTITY;

    @Config(name = "Trigger distance", description = "Player will trigger EntitySpawner blocks when closer than this distance.")
    public static int triggerDistance = 16;

    @Override
    public void register() {
        ENTITY_SPAWNER = new EntitySpawnerBlock(this);
        BLOCK_ENTITY = BlockEntityType.Builder.create(EntitySpawnerBlockEntity::new, ENTITY_SPAWNER).build(null);
        Registry.register(Registry.BLOCK_ENTITY_TYPE, ID, BLOCK_ENTITY);
    }

    @Override
    public void clientRegister() {
        BlockRenderLayerMap.INSTANCE.putBlock(ENTITY_SPAWNER, RenderLayer.getCutout());
    }
}
