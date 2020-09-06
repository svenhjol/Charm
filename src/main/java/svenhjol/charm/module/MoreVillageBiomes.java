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
import svenhjol.meson.event.AddEntityCallback;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.BiomeHelper;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Module(description = "Villages can spawn in swamps and jungles.")
public class MoreVillageBiomes extends MesonModule {
    @Override
    public void init() {
        if (enabled) {
            List<RegistryKey<Biome>> biomeKeys = new ArrayList<>(Arrays.asList(
                BiomeKeys.JUNGLE, BiomeKeys.SWAMP
            ));

            for (RegistryKey<Biome> biomeKey : biomeKeys) {
                Biome biome = BiomeHelper.getBiomeFromBiomeKey(biomeKey);
                BiomeHelper.addStructureFeature(biome, ConfiguredStructureFeatures.VILLAGE_PLAINS);
            }

            AddEntityCallback.EVENT.register((entity -> {
                changeVillagerSkin(entity);
                return ActionResult.PASS;
            }));
        }
    }

    private void changeVillagerSkin(Entity entity) {
        if (!entity.world.isClient
            && entity instanceof VillagerEntity
            && entity.updateNeeded
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
    }
}
