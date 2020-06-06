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
import svenhjol.charm.world.block.PigIronOreBlock;
import svenhjol.charm.world.item.PigIronNuggetItem;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.WORLD,
    description = "Pig iron ore can be broken for pig iron nuggets or smelted into regular iron.")
public class NetherPigIron extends MesonModule {
    public static PigIronOreBlock block;
    public static PigIronNuggetItem item;

    @Config(name = "Vein size")
    public static int veinSize = 6;

    @Config(name = "Cluster count")
    public static int clusterCount = 30;

    public static ConfiguredPlacement<CountRangeConfig> placement = null;

    public static OreFeatureConfig config = null;

    @Override
    public void init() {
        block = new PigIronOreBlock(this);
        item = new PigIronNuggetItem(this);
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
