package svenhjol.charm.module.entity_spawners;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.Module;

@Module(mod = Charm.MOD_ID, alwaysEnabled = true, client = EntitySpawnersClient.class, description = "Spawns entities when a player is within range.")
public class EntitySpawners extends CharmModule {
    public static final Identifier ID = new Identifier(Charm.MOD_ID, "entity_spawner");
    public static EntitySpawnerBlock ENTITY_SPAWNER;
    public static BlockEntityType<EntitySpawnerBlockEntity> BLOCK_ENTITY;

    @Config(name = "Trigger distance", description = "Player will trigger EntitySpawner blocks when closer than this distance.")
    public static int triggerDistance = 16;

    @Override
    public void register() {
        ENTITY_SPAWNER = new EntitySpawnerBlock(this);
        BLOCK_ENTITY = RegistryHelper.blockEntity(ID, EntitySpawnerBlockEntity::new, ENTITY_SPAWNER);
    }
}
