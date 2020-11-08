package svenhjol.charm.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import svenhjol.charm.Charm;
import svenhjol.charm.event.AddEntityCallback;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.BiomeHelper;
import svenhjol.charm.base.iface.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Module(mod = Charm.MOD_ID, description = "Villages can spawn in swamps and jungles.")
public class MoreVillageBiomes extends CharmModule {
    @Override
    public void init() {
        List<RegistryKey<Biome>> plainsBiomeKeys = new ArrayList<>(Arrays.asList(
            BiomeKeys.JUNGLE, BiomeKeys.BAMBOO_JUNGLE, BiomeKeys.SWAMP
        ));

        List<RegistryKey<Biome>> taigaBiomeKeys = new ArrayList<>(Arrays.asList(
            BiomeKeys.SNOWY_TAIGA
        ));

        List<RegistryKey<Biome>> snowyBiomeKeys = new ArrayList<>(Arrays.asList(
            BiomeKeys.ICE_SPIKES
        ));

        for (RegistryKey<Biome> biomeKey : plainsBiomeKeys) {
            Biome biome = BiomeHelper.getBiomeFromBiomeKey(biomeKey);
            BiomeHelper.addStructureFeature(biome, ConfiguredStructureFeatures.VILLAGE_PLAINS);
        }

        for (RegistryKey<Biome> biomeKey : taigaBiomeKeys) {
            Biome biome = BiomeHelper.getBiomeFromBiomeKey(biomeKey);
            BiomeHelper.addStructureFeature(biome, ConfiguredStructureFeatures.VILLAGE_TAIGA);
        }

        for (RegistryKey<Biome> biomeKey : snowyBiomeKeys) {
            Biome biome = BiomeHelper.getBiomeFromBiomeKey(biomeKey);
            BiomeHelper.addStructureFeature(biome, ConfiguredStructureFeatures.VILLAGE_SNOWY);
        }

        AddEntityCallback.EVENT.register(this::changeVillagerSkin);
    }

    private ActionResult changeVillagerSkin(Entity entity) {
        if (!entity.world.isClient
            && entity instanceof VillagerEntity
            && entity.age == 0
        ) {
            VillagerEntity villager = (VillagerEntity) entity;
            VillagerData data = villager.getVillagerData();
            ServerWorld world = (ServerWorld)entity.world;

            if (data.getType() == VillagerType.PLAINS) {
                Biome biome = BiomeHelper.getBiome(world, villager.getBlockPos());
                Biome.Category category = biome.getCategory();

                if (category.equals(Biome.Category.JUNGLE) || category.equals(Biome.Category.SWAMP))
                    villager.setVillagerData(data.withType(VillagerType.forBiome(BiomeHelper.getBiomeKeyAtPosition(world, villager.getBlockPos()))));
            }
        }

        return ActionResult.PASS;
    }
}
