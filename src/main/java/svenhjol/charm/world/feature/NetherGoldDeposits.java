package svenhjol.charm.world.feature;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import svenhjol.charm.world.block.NetherGoldDepositBlock;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.ForgeHelper;

import static net.minecraft.world.gen.feature.Feature.ORE;

public class NetherGoldDeposits extends Feature
{
    public static NetherGoldDepositBlock block;
    public static BlockItem blockItem;
    public static int veinSize;
    public static int clusterCount;
    public static float hardness;
    public static float resistance;

    @Override
    public String getDescription()
    {
        return "Gold deposits spawn in the Nether that can be broken to receive gold nuggets.";
    }

    @Override
    public void configure()
    {
        super.configure();

        // internal
        veinSize = 5;
        clusterCount = 12;
        hardness = 3.0f;
        resistance = 10.0f;
    }

    @Override
    public boolean isEnabled()
    {
        return super.isEnabled() && !ForgeHelper.isModLoaded("nethergoldore");
    }

    @Override
    public void init()
    {
        super.init();
        block = new NetherGoldDepositBlock();
        blockItem = block.getBlockItem();

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

    @Override
    public void onRegisterBlocks(IForgeRegistry<Block> registry)
    {
        registry.register(block);
    }

    @Override
    public void onRegisterItems(IForgeRegistry<Item> registry)
    {
        registry.register(blockItem);
    }
}
