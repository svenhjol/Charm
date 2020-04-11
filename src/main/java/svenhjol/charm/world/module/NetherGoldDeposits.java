package svenhjol.charm.world.module;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.ConfiguredPlacement;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.base.compat.NetherModCompat;
import svenhjol.charm.world.block.NetherGoldDepositBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ForgeHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.WORLD,
    description = "Gold deposits spawn in the Nether that can be broken to receive gold nuggets.")
public class NetherGoldDeposits extends MesonModule {
    public static NetherGoldDepositBlock block;

    @Config(name = "Vein size")
    public static int veinSize = 4;

    @Config(name = "Cluster count")
    public static int clusterCount = 10;

    public static ConfiguredPlacement<CountRangeConfig> placement = null;

    public static OreFeatureConfig config = null;

    @Override
    public boolean shouldRunSetup() {
        return !ForgeHelper.isModLoaded("nethergoldore");
    }

    @Override
    public void init() {
        block = new NetherGoldDepositBlock(this);
        placement = Placement.COUNT_RANGE.configure(new CountRangeConfig(clusterCount, 10, 20, 128));
        config = new OreFeatureConfig(
            NetherModCompat.getNetherrackTaggedFillerBlockType(),
            block.getDefaultState(),
            veinSize
        );
    }

    @Override
    public void onCommonSetup(FMLCommonSetupEvent event) {
        for (Biome biome : ForgeRegistries.BIOMES.getValues()) {
            if (biome.getCategory() != Biome.Category.NETHER) continue;
            biome.addFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Feature.ORE.withConfiguration(config).withPlacement(placement));
        }
    }
}
