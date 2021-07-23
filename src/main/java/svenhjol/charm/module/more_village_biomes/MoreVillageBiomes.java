package svenhjol.charm.module.more_village_biomes;

import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.event.AddEntityEvent;
import svenhjol.charm.helper.BiomeHelper;
import svenhjol.charm.loader.CharmModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CommonModule(mod = Charm.MOD_ID, description = "Villages can spawn in swamps and jungles.")
public class MoreVillageBiomes extends CharmModule {
    @Override
    public void runWhenEnabled() {
        List<ResourceKey<Biome>> plainsBiomeKeys = new ArrayList<>(Arrays.asList(
            Biomes.JUNGLE, Biomes.BAMBOO_JUNGLE, Biomes.SWAMP
        ));

        List<ResourceKey<Biome>> taigaBiomeKeys = new ArrayList<>(Arrays.asList(
            Biomes.SNOWY_TAIGA,
            Biomes.GIANT_SPRUCE_TAIGA
        ));

        List<ResourceKey<Biome>> snowyBiomeKeys = new ArrayList<>(Arrays.asList(
            Biomes.ICE_SPIKES
        ));

        for (ResourceKey<Biome> biomeKey : plainsBiomeKeys) {
            BiomeHelper.addStructureToBiome(StructureFeatures.VILLAGE_PLAINS, biomeKey);
        }

        for (ResourceKey<Biome> biomeKey : taigaBiomeKeys) {
            BiomeHelper.addStructureToBiome(StructureFeatures.VILLAGE_TAIGA, biomeKey);
        }

        for (ResourceKey<Biome> biomeKey : snowyBiomeKeys) {
            BiomeHelper.addStructureToBiome(StructureFeatures.VILLAGE_SNOWY, biomeKey);
        }

        AddEntityEvent.EVENT.register(this::changeVillagerSkin);
    }

    private InteractionResult changeVillagerSkin(Entity entity) {
        if (!entity.level.isClientSide
            && entity instanceof Villager villager
            && entity.tickCount == 0
        ) {
            VillagerData data = villager.getVillagerData();
            ServerLevel world = (ServerLevel)entity.level;

            if (data.getType() == VillagerType.PLAINS) {
                Biome biome = BiomeHelper.getBiome(world, villager.blockPosition());
                Biome.BiomeCategory category = biome.getBiomeCategory();

                if (category.equals(Biome.BiomeCategory.JUNGLE) || category.equals(Biome.BiomeCategory.SWAMP))
                    villager.setVillagerData(data.setType(VillagerType.byBiome(BiomeHelper.getBiomeKeyAtPosition(world, villager.blockPosition()))));
            }
        }

        return InteractionResult.PASS;
    }
}
