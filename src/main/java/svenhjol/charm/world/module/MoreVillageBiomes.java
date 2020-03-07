package svenhjol.charm.world.module;

import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.villager.IVillagerType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.BiomeHelper;
import svenhjol.meson.iface.Module;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.world.gen.feature.Feature.VILLAGE;

@Module(mod = Charm.MOD_ID, category = CharmCategories.WORLD, hasSubscriptions = true,
    description = "Villages can spawn in swamps, jungles and bedrock-edition biomes.\n" +
        "Villagers spawning in these villages will use their correct biome texture.")
public class MoreVillageBiomes extends MesonModule
{
    public static List<Biome> plainsBiomes = Arrays.asList(Biomes.JUNGLE, Biomes.SWAMP, Biomes.SUNFLOWER_PLAINS);
    public static List<Biome> taigaBiomes = Arrays.asList(Biomes.TAIGA_HILLS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA_HILLS);

    @Override
    public void onCommonSetup(FMLCommonSetupEvent event)
    {
        taigaBiomes.forEach(biome -> biome.addStructure(VILLAGE, new VillageConfig("village/taiga/town_centers", 6)));

        // there isn't dedicated structure pieces for jungles and swamps so just use plains
        plainsBiomes.forEach(biome -> biome.addStructure(VILLAGE, new VillageConfig("village/plains/town_centers", 6)));
    }

    @SubscribeEvent
    public void onVillagerSpawn(EntityJoinWorldEvent event)
    {
        if (!event.getWorld().isRemote
            && event.getEntity() instanceof VillagerEntity
            && event.getEntity().addedToChunk
            && event.getEntity().ticksExisted == 0
        ) {
            VillagerEntity villager = (VillagerEntity)event.getEntity();
            VillagerData data = villager.getVillagerData();

            if (data.getType() == IVillagerType.PLAINS) {
                Biome biome = BiomeHelper.getBiomeAtPos(event.getWorld(), event.getEntity().getPosition());

                if (plainsBiomes.contains(biome)) {
                    villager.setVillagerData(data.withType(IVillagerType.byBiome(biome)));
                }
            }
        }
    }
}
