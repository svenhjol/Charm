package svenhjol.charm.module;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.helper.BiomeHelper;
import svenhjol.charm.base.helper.MobHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.CoralSquidsClient;
import svenhjol.charm.entity.CoralSquidEntity;
import svenhjol.charm.item.CoralSquidBucketItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Module(mod = Charm.MOD_ID, client = CoralSquidsClient.class, description = "Coral Squids spawn near coral in warm oceans.")
public class CoralSquids extends CharmModule {
    public static Identifier ID = new Identifier(Charm.MOD_ID, "coral_squid");
    public static Identifier EGG_ID = new Identifier(Charm.MOD_ID, "coral_squid_spawn_egg");

    public static CoralSquidBucketItem CORAL_SQUID_BUCKET;
    public static EntityType<CoralSquidEntity> CORAL_SQUID;
    public static Item SPAWN_EGG;

    @Config(name = "Drop chance", description = "Chance (out of 1.0) of a coral squid dropping coral when killed by the player.")
    public static double dropChance = 0.2D;

    @Config(name = "Spawn weight", description = "Chance of coral squids spawning in warm ocean biomes.")
    public static int spawnWeight = 50;

    @Override
    public void register() {
        // register to MC registry
        CORAL_SQUID = RegistryHandler.entity(ID, FabricEntityTypeBuilder
            .create(SpawnGroup.WATER_AMBIENT, CoralSquidEntity::new)
            .dimensions(EntityDimensions.fixed(0.54f, 0.54f))
            .build());

        SpawnRestrictionAccessor.callRegister(CORAL_SQUID, SpawnRestriction.Location.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CoralSquidEntity::canSpawn);

        // create a spawn egg for the squid
        SPAWN_EGG = RegistryHandler.item(EGG_ID, new SpawnEggItem(CORAL_SQUID, 0x0000FF, 0xFF00FF, (new Item.Settings()).group(ItemGroup.MISC)));

        // register the entity attributes
        MobHelper.setEntityAttributes(CORAL_SQUID, CoralSquidEntity.createSquidAttributes());

        // create a bucket item
        CORAL_SQUID_BUCKET = new CoralSquidBucketItem(this);
    }

    @Override
    public void init() {
        List<RegistryKey<Biome>> biomes = new ArrayList<>(Arrays.asList(BiomeKeys.WARM_OCEAN, BiomeKeys.DEEP_WARM_OCEAN));

        biomes.forEach(biomeKey -> {
            BiomeHelper.addSpawnEntry(biomeKey, SpawnGroup.WATER_AMBIENT, CORAL_SQUID, spawnWeight, 2, 4);
        });
    }
}
