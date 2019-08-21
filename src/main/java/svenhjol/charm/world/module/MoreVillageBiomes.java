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
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.WorldHelper;
import svenhjol.meson.iface.Module;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.world.gen.feature.Feature.VILLAGE;

@Module(category = CharmCategories.WORLD, hasSubscriptions = true)
public class MoreVillageBiomes extends MesonModule
{
    public static List<Biome> biomes = Arrays.asList(Biomes.JUNGLE, Biomes.SWAMP);

    @Override
    public void setup(FMLCommonSetupEvent event)
    {
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
}
