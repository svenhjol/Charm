package svenhjol.charm.world.module;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
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
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.WORLD)
public class Fumaroles extends MesonModule
{
    public static FumaroleBlock block;
    public static Feature<NoFeatureConfig> feature = null;
    public static Placement<FrequencyConfig> placement = null;

    @Override
    public void init()
    {
        block = new FumaroleBlock(this);
        feature = new FumaroleFeature(NoFeatureConfig::deserialize);
        placement = new FumarolePlacement(FrequencyConfig::deserialize);

        // TODO sort out registry
        ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "fumarole");
        RegistryHandler.addRegisterable(feature, ID);
        Registry.register(Registry.FEATURE, ID.toString(), feature);
        RegistryHandler.addRegisterable(placement, ID);
        Registry.register(Registry.DECORATOR, ID.toString(), placement);
    }

    @Override
    public void setup(FMLCommonSetupEvent event)
    {
        Biomes.NETHER.addFeature(Decoration.UNDERGROUND_DECORATION,
            Biome.createDecoratedFeature(feature,
                IFeatureConfig.NO_FEATURE_CONFIG,
                placement,
                new FrequencyConfig(8))
        );
    }
}
