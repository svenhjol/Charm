package svenhjol.charm.feature.mooblooms.common;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.levelgen.Heightmap;
import svenhjol.charm.api.event.EntityJoinEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.mooblooms.Mooblooms;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<Mooblooms> {
//    public static final String ID = "moobloom";
    public final Supplier<SpawnEggItem> spawnEggItem;
    public final Supplier<EntityType<Moobloom>> entity;
    public final Supplier<SoundEvent> milkingSound;

    public Registers(Mooblooms feature) {
        super(feature);

        var registry = feature().registry();

        entity = registry.entity("moobloom", () -> EntityType.Builder
            .of(Moobloom::new, MobCategory.CREATURE)
            .sized(0.9F, 1.4F)
            .clientTrackingRange(10));

        milkingSound = registry.soundEvent("moobloom_milk");

        spawnEggItem = registry.spawnEggItem("moobloom_spawn_egg", entity,
            0xFFFF00, 0xFFFFFF, new Item.Properties());

        registry.biomeSpawn(holder -> holder.is(Tags.SPAWNS_COMMON_MOOBLOOMS),
            MobCategory.CREATURE, entity, 30, 1, 3);

        registry.biomeSpawn(holder -> holder.is(Tags.SPAWNS_CHERRY_BLOSSOM_MOOBLOOMS),
            MobCategory.CREATURE, entity, 5, 1, 1);

        registry.biomeSpawn(holder -> holder.is(Tags.SPAWNS_SUNFLOWER_MOOBLOOMS),
            MobCategory.CREATURE, entity, 10, 1, 2);

        registry.entitySpawnPlacement(entity, SpawnPlacementTypes.ON_GROUND,
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
            Moobloom::canSpawn);

        registry.entityAttributes(entity, Moobloom::createAttributes);
    }

    @Override
    public void onEnabled() {
        EntityJoinEvent.INSTANCE.handle(feature().handlers::entityJoin);
    }
}
