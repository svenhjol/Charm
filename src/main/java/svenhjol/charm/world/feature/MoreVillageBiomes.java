package svenhjol.charm.world.feature;

import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.villager.IVillagerType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.WorldHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.world.gen.feature.Feature.VILLAGE;

public class MoreVillageBiomes extends Feature
{
    public static List<Biome> biomes = new ArrayList<>();

    @Override
    public void configure()
    {
        super.configure();

        biomes.addAll(Arrays.asList(Biomes.JUNGLE, Biomes.SWAMP));
    }

    @Override
    public void init()
    {
        super.init();

        // there isn't dedicated structure pieces for jungles and swamps so just use plains
        biomes.forEach(biome -> biome.addStructure(VILLAGE, new VillageConfig("village/plains/town_centers", 6)));
    }

    @SubscribeEvent
    public void onSpawn(EntityJoinWorldEvent event)
    {
        if (!event.getWorld().isRemote
            && event.getEntity() instanceof VillagerEntity
            && event.getEntity().addedToChunk
            && event.getEntity().ticksExisted == 0
        ) {
            VillagerEntity villager = (VillagerEntity)event.getEntity();
            VillagerData data = villager.getVillagerData();

            if (data.getType() == IVillagerType.PLAINS) {
                Biome biome = WorldHelper.getBiomeAtPos(event.getWorld(), event.getEntity().getPosition());

                if (biomes.contains(biome)) {
                    villager.setVillagerData(data.withType(IVillagerType.byBiome(biome)));
                }
            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
