package svenhjol.charm.world.module;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.world.block.FumaroleBlock;
import svenhjol.charm.world.gen.feature.FumaroleFeature;
import svenhjol.charm.world.gen.placement.FumarolePlacement;
import svenhjol.meson.MesonModule;
import svenhjol.meson.handler.RegistryHandler;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import static net.minecraft.block.Blocks.NETHERRACK;

@Module(mod = Charm.MOD_ID, category = CharmCategories.WORLD,
    description = "Fumaroles are small columns of hot steam rising from the nether floor.\n" +
        "Sometimes they erupt, sending entities that are placed on them high into the air.")
public class Fumaroles extends MesonModule {
    public static FumaroleBlock block;
    public static Feature<BlockClusterFeatureConfig> feature = null;
    public static Placement<FrequencyConfig> placement = null;
    public static BlockClusterFeatureConfig config = null;

    @Config(name = "Eruption volume", description = "Volume of the eruption. 1.0 is full volume, 0.0 silent.")
    public static double eruptionVolume = 0.25D;

    @Override
    public void init() {
        block = new FumaroleBlock(this);
        BlockState state = block.getDefaultState();
        feature = new FumaroleFeature(BlockClusterFeatureConfig::deserialize);
        placement = new FumarolePlacement(FrequencyConfig::deserialize);
        config = (new BlockClusterFeatureConfig.Builder(
            new SimpleBlockStateProvider(state),
            new SimpleBlockPlacer())).tries(64).whitelist(ImmutableSet.of(NETHERRACK.getBlock())).func_227317_b_().build();

        ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "fumarole");
        RegistryHandler.registerFeature(feature, placement, ID);
    }

    @Override
    public void onCommonSetup(FMLCommonSetupEvent event) {
        Biomes.NETHER.addFeature(Decoration.UNDERGROUND_DECORATION,
            feature.withConfiguration(config)
                .withPlacement(placement.configure(new FrequencyConfig(8))));
    }
}
