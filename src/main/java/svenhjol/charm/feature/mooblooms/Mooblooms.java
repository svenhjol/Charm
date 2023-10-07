package svenhjol.charm.feature.mooblooms;

import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony_api.event.EntityJoinEvent;
import svenhjol.charmony.base.CharmonyFeature;

import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Mooblooms are cow-like mobs that come in a variety of flower types.\n" +
    "They spawn flowers where they walk and can be milked for suspicious stew.")
public class Mooblooms extends CharmonyFeature {
    static final String ID = "moobloom";
    static Supplier<Item> spawnEggItem;
    static Supplier<EntityType<MoobloomEntity>> entity;
    static Supplier<SoundEvent> milkingSound;
    public static final TagKey<Biome> SPAWNS_COMMON_MOOBLOOMS
        = TagKey.create(Registries.BIOME, Charm.instance().makeId("spawns_common_mooblooms"));
    public static final TagKey<Biome> SPAWNS_CHERRY_BLOSSOM_MOOBLOOMS
        = TagKey.create(Registries.BIOME, Charm.instance().makeId("spawns_cherry_blossom_mooblooms"));
    public static final TagKey<Biome> SPAWNS_SUNFLOWER_MOOBLOOMS
        = TagKey.create(Registries.BIOME, Charm.instance().makeId("spawns_sunflower_mooblooms"));

    @Override
    public void register() {
        var registry = Charm.instance().registry();

        entity = registry.entity(ID, () -> EntityType.Builder
            .of(MoobloomEntity::new, MobCategory.CREATURE)
            .sized(0.9F, 1.4F)
            .clientTrackingRange(10));

        milkingSound = registry.soundEvent("moobloom_milk");

        spawnEggItem = registry.spawnEggItem("moobloom_spawn_egg", entity, 0xFFFF00, 0xFFFFFF, new Item.Properties());

        registry.biomeSpawn(holder -> holder.is(SPAWNS_COMMON_MOOBLOOMS),
            MobCategory.CREATURE, entity, 30, 1, 3);

        registry.biomeSpawn(holder -> holder.is(SPAWNS_CHERRY_BLOSSOM_MOOBLOOMS),
            MobCategory.CREATURE, entity, 5, 1, 1);

        registry.biomeSpawn(holder -> holder.is(SPAWNS_SUNFLOWER_MOOBLOOMS),
            MobCategory.CREATURE, entity, 10, 1, 2);

        registry.entitySpawnPlacement(entity, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MoobloomEntity::canSpawn);
        registry.entityAttributes(entity, MoobloomEntity::createAttributes);
    }

    @Override
    public void runWhenEnabled() {
        EntityJoinEvent.INSTANCE.handle(this::handleEntityJoin);
    }

    private void handleEntityJoin(Entity entity, Level level) {
        if (entity instanceof Bee bee) {
            var hasGoal = bee.getGoalSelector().getAvailableGoals().stream().anyMatch(
                goal -> goal.getGoal() instanceof BeeMoveToMoobloomGoal);

            if (!hasGoal) {
                bee.getGoalSelector().addGoal(4, new BeeMoveToMoobloomGoal(bee));
            }
        }
    }
}
