package svenhjol.charm.world.module;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.world.block.NetherGoldDepositBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ForgeHelper;
import svenhjol.meson.iface.Module;

import static net.minecraft.world.gen.feature.Feature.ORE;

@Module(mod = Charm.MOD_ID, category = CharmCategories.WORLD,
    description = "Gold deposits spawn in the Nether that can be broken to receive gold nuggets.")
public class NetherGoldDeposits extends MesonModule
{
    public static NetherGoldDepositBlock block;
    public static int veinSize = 5;
    public static int clusterCount = 12;
    public static float hardness = 3.0F;
    public static float resistance = 10.0F;

    @Override
    public boolean isEnabled()
    {
        return super.isEnabled() && !ForgeHelper.isModLoaded("nethergoldore");
    }

    @Override
    public void init()
    {
        block = new NetherGoldDepositBlock(this);
    }

    @Override
    public void setup(FMLCommonSetupEvent event)
    {
        for (Biome biome : ForgeRegistries.BIOMES.getValues()) {
            if (biome.getCategory() != Biome.Category.NETHER) continue;

            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                Biome.createDecoratedFeature(ORE,
                    new OreFeatureConfig(
                        OreFeatureConfig.FillerBlockType.NETHERRACK,
                        block.getDefaultState(),
                        veinSize
                    ),
                    Placement.COUNT_RANGE,
                    new CountRangeConfig(7, 10, 0, 128))
            );
        }
    }
}
