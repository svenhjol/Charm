package svenhjol.charm.module;

import net.fabricmc.fabric.mixin.object.builder.DefaultAttributeRegistryAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import svenhjol.charm.Charm;
import svenhjol.charm.client.CoralSquidsClient;
import svenhjol.charm.entity.CoralSquidEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.BiomeHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Module(description = "Coral Squids spawn around coral in warm oceans.")
public class CoralSquids extends MesonModule {
    public static Identifier ID = new Identifier(Charm.MOD_ID, "coral_squid");
    public static Identifier EGG_ID = new Identifier(Charm.MOD_ID, "coral_squid_spawn_egg");

    public static EntityType<CoralSquidEntity> CORAL_SQUID;
    public static Item SPAWN_EGG;

    public CoralSquidsClient client;

    @Config(name = "Drop chance", description = "Chance (out of 1.0) of a coral squid dropping coral when killed by the player.")
    public static double dropChance = 0.2D;

    @Override
    public void register() {
        CORAL_SQUID = EntityType.Builder.create(CoralSquidEntity::new, SpawnGroup.WATER_CREATURE)
            .setDimensions(0.4F, 0.4F)
            .maxTrackingRange(8)
            .build(ID.getPath());

        SPAWN_EGG = new SpawnEggItem(CORAL_SQUID, 0xFF00FF, 0x0000FF, (new Item.Settings()).group(ItemGroup.MISC));
        Registry.register(Registry.ITEM, EGG_ID, SPAWN_EGG);

        Registry.register(Registry.ENTITY_TYPE, ID, CORAL_SQUID);
        DefaultAttributeRegistryAccessor.getRegistry()
            .put(CORAL_SQUID, CoralSquidEntity.createSquidAttributes().build());
    }

    @Override
    public void clientRegister() {
        client = new CoralSquidsClient(this);
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
