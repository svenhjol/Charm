package svenhjol.charm.module;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.helper.BiomeHelper;
import svenhjol.charm.base.helper.MobHelper;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.MoobloomsClient;
import svenhjol.charm.entity.MoobloomEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Module(mod = Charm.MOD_ID, client = MoobloomsClient.class)
public class Mooblooms extends CharmModule {
    public static Identifier ID = new Identifier(Charm.MOD_ID, "moobloom");
    public static EntityType<MoobloomEntity> MOOBLOOM;

    @Override
    public void register() {
        MOOBLOOM = RegistryHandler.entity(ID, FabricEntityTypeBuilder
            .create(SpawnGroup.CREATURE, MoobloomEntity::new)
            .dimensions(EntityDimensions.fixed(0.9F, 1.4F))
            .trackRangeBlocks(10)
            .build());

        MobHelper.setEntityAttributes(MOOBLOOM, CowEntity.createCowAttributes());
    }

    @Override
    public void init() {
        List<RegistryKey<Biome>> biomes = new ArrayList<>(Collections.singletonList(BiomeKeys.FLOWER_FOREST));

        biomes.forEach(biomeKey -> {
            Biome biome = BiomeHelper.getBiomeFromBiomeKey(biomeKey);
            BiomeHelper.addSpawnEntry(biome, SpawnGroup.CREATURE, MOOBLOOM, 10, 2, 4);
        });
    }
}
