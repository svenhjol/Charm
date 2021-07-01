package svenhjol.charm.module.ebony_wood;

import net.minecraft.data.worldgen.Features;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.ChanceDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.block.*;
import svenhjol.charm.enums.CharmWoodMaterial;
import svenhjol.charm.helper.BiomeHelper;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.helper.ToolHelper;
import svenhjol.charm.item.CharmBoatItem;
import svenhjol.charm.item.CharmSignItem;
import svenhjol.charm.loader.CharmCommonModule;
import svenhjol.charm.module.bookcases.BookcaseBlock;
import svenhjol.charm.module.bookcases.Bookcases;
import svenhjol.charm.module.ebony_wood.EbonyBlocks.*;
import svenhjol.charm.module.ebony_wood.EbonyItems.EbonyBoatItem;
import svenhjol.charm.module.ebony_wood.EbonyItems.EbonySignItem;
import svenhjol.charm.module.variant_barrels.VariantBarrelBlock;
import svenhjol.charm.module.variant_barrels.VariantBarrels;
import svenhjol.charm.module.variant_bookshelves.VariantBookshelfBlock;
import svenhjol.charm.module.variant_bookshelves.VariantBookshelves;
import svenhjol.charm.module.variant_chests.VariantChestBlock;
import svenhjol.charm.module.variant_chests.VariantChests;
import svenhjol.charm.module.variant_chests.VariantTrappedChestBlock;
import svenhjol.charm.module.variant_ladders.VariantLadderBlock;
import svenhjol.charm.module.variant_ladders.VariantLadders;

import java.util.OptionalInt;

@CommonModule(mod = Charm.MOD_ID, description = "Ebony is a very dark grey wood. Ebony trees can be found in savanna biomes.")
public class EbonyWood extends CharmCommonModule {
    @Config(name = "Spawn chance", description = "Chance (per number of chunks) of an ebony tree spawning.")
    public static int spawnChance = 3;

    public static ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "ebony");
    public static ResourceLocation DECORATION_ID = new ResourceLocation(Charm.MOD_ID, "ebony_decoration");
    public static ConfiguredFeature<?, ?> TREE;
    public static ConfiguredFeature<?, ?> TREE_DECORATION;

    public static WoodType SIGN_TYPE;

    public static CharmWoodenButtonBlock BUTTON;
    public static CharmDoorBlock DOOR;
    public static CharmFenceBlock FENCE;
    public static CharmFenceGateBlock FENCE_GATE;
    public static CharmLogBlock LOG;
    public static CharmLeavesBlock LEAVES;
    public static CharmPlanksBlock PLANKS;
    public static CharmPressurePlate PRESSURE_PLATE;
    public static CharmSaplingBlock SAPLING;
    public static CharmSignBlock SIGN_BLOCK;
    public static CharmSlabBlock SLAB;
    public static CharmStairsBlock STAIRS;
    public static CharmLogBlock STRIPPED_LOG;
    public static CharmLogBlock STRIPPED_WOOD;
    public static CharmTrapdoorBlock TRAPDOOR;
    public static CharmWallSignBlock WALL_SIGN_BLOCK;
    public static CharmLogBlock WOOD;

    public static VariantBarrelBlock BARREL;
    public static BookcaseBlock BOOKCASE;
    public static VariantBookshelfBlock BOOKSHELF;
    public static VariantChestBlock CHEST;
    public static VariantLadderBlock LADDER;
    public static VariantTrappedChestBlock TRAPPED_CHEST;

    public static CharmBoatItem BOAT;
    public static CharmSignItem SIGN_ITEM;

    @Override
    public void register() {
        // must init these first, other blocks depend on them being registered
        SIGN_TYPE = RegistryHelper.signType(ID);
        PLANKS = new EbonyPlanksBlock(this);

        BUTTON = new EbonyButtonBlock(this);
        DOOR = new EbonyDoorBlock(this);
        FENCE = new EbonyFenceBlock(this);
        FENCE_GATE = new EbonyFenceGateBlock(this);
        LOG = new EbonyLogBlock(this);
        STRIPPED_LOG = new StrippedEbonyLogBlock(this);
        LEAVES = new EbonyLeavesBlock(this);
        PRESSURE_PLATE = new EbonyPressurePlateBlock(this);
        SAPLING = new EbonySaplingBlock(this);
        SIGN_BLOCK = new EbonySignBlock(this);
        SLAB = new EbonySlabBlock(this);
        STAIRS = new EbonyStairsBlock(this);
        TRAPDOOR = new EbonyTrapdoorBlock(this);
        WALL_SIGN_BLOCK = new EbonyWallSignBlock(this);
        WOOD = new EbonyWoodBlock(this);
        STRIPPED_WOOD = new StrippedEbonyWoodBlock(this);

        BOAT = new EbonyBoatItem(this);
        SIGN_ITEM = new EbonySignItem(this);

        BARREL = VariantBarrels.registerBarrel(this, CharmWoodMaterial.EBONY);
        BOOKCASE = Bookcases.registerBookcase(this, CharmWoodMaterial.EBONY);
        BOOKSHELF = VariantBookshelves.registerBookshelf(this, CharmWoodMaterial.EBONY);
        CHEST = VariantChests.registerChest(this, CharmWoodMaterial.EBONY);
        LADDER = VariantLadders.registerLadder(this, CharmWoodMaterial.EBONY);
        TRAPPED_CHEST = VariantChests.registerTrappedChest(this, CharmWoodMaterial.EBONY);

        ConfiguredFeature<?, ?> configuredFeature = Feature.TREE.configured(new TreeConfiguration.TreeConfigurationBuilder(
            new SimpleStateProvider(LOG.defaultBlockState()),
            new FancyTrunkPlacer(3, 6, 0),
            new SimpleStateProvider(LEAVES.defaultBlockState()),
            new SimpleStateProvider(SAPLING.defaultBlockState()),
            new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 3), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(3))
        ).build());

        TREE = RegistryHelper.configuredFeature(ID, configuredFeature);
        TREE_DECORATION = RegistryHelper.configuredFeature(DECORATION_ID, TREE
            .decorated(Features.Decorators.HEIGHTMAP_WITH_TREE_THRESHOLD_SQUARED)
            .decorated(FeatureDecorator.CHANCE.configured(new ChanceDecoratorConfiguration(spawnChance))));
    }

    @Override
    public void run() {
        RegistryHelper.addBlocksToBlockEntity(BlockEntityType.SIGN, SIGN_BLOCK, WALL_SIGN_BLOCK);
        BiomeHelper.addFeatureToBiomeCategories(TREE_DECORATION, Biome.BiomeCategory.SAVANNA, GenerationStep.Decoration.VEGETAL_DECORATION);
        ToolHelper.addStrippableLog(LOG, STRIPPED_LOG);
        ToolHelper.addStrippableLog(WOOD, STRIPPED_WOOD);
    }
}
