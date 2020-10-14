package svenhjol.charm.module;

import net.fabricmc.fabric.mixin.object.builder.DefaultAttributeRegistryAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import svenhjol.charm.Charm;
import svenhjol.charm.client.CoralSquidsClient;
import svenhjol.charm.entity.CoralSquidEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.BiomeHelper;
import svenhjol.meson.iface.Module;

@Module(description = "Coral Squids spawn around coral in warm oceans.")
public class CoralSquids extends MesonModule {
    public static Identifier ID = new Identifier(Charm.MOD_ID, "coral_squid");
    public static EntityType<CoralSquidEntity> CORAL_SQUID;

    public CoralSquidsClient client;

    @Override
    public void register() {
        CORAL_SQUID = EntityType.Builder.create(CoralSquidEntity::new, SpawnGroup.WATER_CREATURE)
            .setDimensions(0.8F, 0.8F)
            .maxTrackingRange(8)
            .build(ID.getPath());

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
        Biome biome = BiomeHelper.getBiomeFromBiomeKey(BiomeKeys.WARM_OCEAN);
        BiomeHelper.addSpawnEntry(biome, SpawnGroup.WATER_CREATURE, CORAL_SQUID, 100, 8, 12);
    }
}
