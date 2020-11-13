package svenhjol.charm.module;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.EntitySpawnerBlock;
import svenhjol.charm.blockentity.EntitySpawnerBlockEntity;
import svenhjol.charm.client.EntitySpawnersClient;

@Module(mod = Charm.MOD_ID, client = EntitySpawnersClient.class, description = "Spawns entities when a player is within range.", alwaysEnabled = true)
public class EntitySpawner extends CharmModule {
    public static final Identifier ID = new Identifier(Charm.MOD_ID, "entity_spawner");
    public static EntitySpawnerBlock ENTITY_SPAWNER;
    public static BlockEntityType<EntitySpawnerBlockEntity> BLOCK_ENTITY;

    @Config(name = "Trigger distance", description = "Player will trigger EntitySpawner blocks when closer than this distance.")
    public static int triggerDistance = 16;

    @Override
    public void register() {
        ENTITY_SPAWNER = new EntitySpawnerBlock(this);
        BLOCK_ENTITY = RegistryHandler.blockEntity(ID, EntitySpawnerBlockEntity::new, ENTITY_SPAWNER);
    }
}
