package svenhjol.charm.module.entity_spawners;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.module.entity_spawners.EntitySpawnerBlock;
import svenhjol.charm.module.entity_spawners.EntitySpawnerBlockEntity;
import svenhjol.charm.module.entity_spawners.EntitySpawnersClient;

@Module(mod = Charm.MOD_ID, alwaysEnabled = true, client = EntitySpawnersClient.class, description = "Spawns entities when a player is within range.")
public class EntitySpawners extends CharmModule {
    public static final ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "entity_spawner");
    public static svenhjol.charm.module.entity_spawners.EntitySpawnerBlock ENTITY_SPAWNER;
    public static BlockEntityType<svenhjol.charm.module.entity_spawners.EntitySpawnerBlockEntity> BLOCK_ENTITY;

    @Config(name = "Trigger distance", description = "Player will trigger EntitySpawner blocks when closer than this distance.")
    public static int triggerDistance = 16;

    @Override
    public void register() {
        ENTITY_SPAWNER = new EntitySpawnerBlock(this);
        BLOCK_ENTITY = RegistryHelper.blockEntity(ID, EntitySpawnerBlockEntity::new, ENTITY_SPAWNER);
    }
}
