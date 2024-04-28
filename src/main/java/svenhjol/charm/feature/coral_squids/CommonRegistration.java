package svenhjol.charm.feature.coral_squids;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.Heightmap;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.Tags;

public class CommonRegistration extends Registration<CoralSquids> {
    static final String ID = "coral_squid";

    public CommonRegistration(CoralSquids feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();

        CoralSquids.entity = registry.entity(ID, () -> EntityType.Builder
            .of(CoralSquidEntity::new, MobCategory.WATER_AMBIENT)
            .sized(0.54f, 0.54f));

        CoralSquids.spawnEggItem = registry.spawnEggItem("coral_squid_spawn_egg", CoralSquids.entity, 0x0000ff, 0xff00ff, new Item.Properties());
        CoralSquids.bucketItem = registry.item("coral_squid_bucket", CoralSquidBucketItem::new);

        registry.biomeSpawn(holder -> holder.is(Tags.SPAWNS_CORAL_SQUIDS), MobCategory.WATER_AMBIENT, CoralSquids.entity, 50, 2, 4);
        registry.entitySpawnPlacement(CoralSquids.entity, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CoralSquidEntity::canSpawn);
        registry.entityAttributes(CoralSquids.entity, CoralSquidEntity::createSquidAttributes);
    }
}
