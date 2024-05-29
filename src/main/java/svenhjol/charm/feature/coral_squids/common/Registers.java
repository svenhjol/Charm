package svenhjol.charm.feature.coral_squids.common;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.levelgen.Heightmap;
import svenhjol.charm.feature.coral_squids.CoralSquids;
import svenhjol.charm.charmony.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<CoralSquids> {
    public final Supplier<SpawnEggItem> spawnEggItem;
    public final Supplier<Item> bucketItem;
    public final Supplier<EntityType<CoralSquid>> entity;
    public final Supplier<SoundEvent> coralSquidBucketFill;

    public Registers(CoralSquids feature) {
        super(feature);
        var registry = feature.registry();

        entity = registry.entity("coral_squid", () -> EntityType.Builder
            .of(CoralSquid::new, MobCategory.WATER_AMBIENT)
            .sized(0.54f, 0.54f));

        spawnEggItem = registry.spawnEggItem("coral_squid_spawn_egg", entity, 0x0000ff, 0xff00ff, new Item.Properties());
        bucketItem = registry.item("coral_squid_bucket", BucketItem::new);
        coralSquidBucketFill = registry.soundEvent("coral_squid_bucket_fill");

        registry.biomeSpawn(holder -> holder.is(Tags.SPAWNS_CORAL_SQUIDS), MobCategory.WATER_AMBIENT, entity, 50, 2, 4);
        registry.entitySpawnPlacement(entity, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CoralSquid::canSpawn);
        registry.entityAttributes(entity, CoralSquid::createSquidAttributes);
    }
}
