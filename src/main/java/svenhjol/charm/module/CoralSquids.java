package svenhjol.charm.module;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Module(mod = Charm.MOD_ID, client = CoralSquidsClient.class, description = "Coral Squids spawn near coral in warm oceans.")
public class CoralSquids extends CharmModule {
    public static Identifier ID = new Identifier(Charm.MOD_ID, "coral_squid");
    public static Identifier EGG_ID = new Identifier(Charm.MOD_ID, "coral_squid_spawn_egg");

    public static EntityType<CoralSquidEntity> CORAL_SQUID;
    public static Item SPAWN_EGG;

    @Config(name = "Drop chance", description = "Chance (out of 1.0) of a coral squid dropping coral when killed by the player.")
    public static double dropChance = 0.2D;

    @Override
    public void register() {
        // register to MC registry
        CORAL_SQUID = RegistryHandler.entity(ID, EntityType.Builder.create(CoralSquidEntity::new, SpawnGroup.WATER_CREATURE)
            .setDimensions(0.54F, 0.54F)
            .maxTrackingRange(8)
            .build(ID.getPath()));

        // create a spawn egg for the squid
        SPAWN_EGG = RegistryHandler.item(EGG_ID, new SpawnEggItem(CORAL_SQUID, 0x0000FF, 0xFF00FF, (new Item.Settings()).group(ItemGroup.MISC)));

        // register the entity attributes
        MobHelper.setEntityAttributes(CORAL_SQUID, CoralSquidEntity.createSquidAttributes());
    }

    @Override
    public void init() {
        List<RegistryKey<Biome>> biomes = new ArrayList<>(Arrays.asList(BiomeKeys.WARM_OCEAN, BiomeKeys.DEEP_WARM_OCEAN));

        biomes.forEach(biomeKey -> {
            Biome biome = BiomeHelper.getBiomeFromBiomeKey(biomeKey);
            BiomeHelper.addSpawnEntry(biome, SpawnGroup.WATER_AMBIENT, CORAL_SQUID, 40, 5, 6);
        });
    }
}
