package svenhjol.charm.module;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.CountExtraDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.LargeOakFoliagePlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.trunk.LargeOakTrunkPlacer;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.*;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.helper.BiomeHelper;
import svenhjol.charm.base.helper.RegistryHelper;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.base.item.CharmBoatItem;
import svenhjol.charm.base.item.CharmSignItem;
import svenhjol.charm.block.*;
import svenhjol.charm.client.EbonyWoodClient;
import svenhjol.charm.enums.CharmWoodMaterial;

import java.util.OptionalInt;

import static svenhjol.charm.block.EbonyBlocks.*;
import static svenhjol.charm.item.EbonyItems.*;

@Module(mod = Charm.MOD_ID, client = EbonyWoodClient.class)
public class EbonyWood extends CharmModule {
    public static Identifier ID = new Identifier(Charm.MOD_ID, "ebony");
    public static ConfiguredFeature<?, ?> TREE;
    public static SignType SIGN_TYPE;

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
        SIGN_TYPE = RegistryHandler.signType(ID);
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

        ConfiguredFeature<?, ?> configuredFeature = Feature.TREE.configure(new TreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(LOG.getDefaultState()),
            new LargeOakTrunkPlacer(3, 6, 0),
            new SimpleBlockStateProvider(LEAVES.getDefaultState()),
            new LargeOakFoliagePlacer(ConstantIntProvider.create(2), ConstantIntProvider.create(4), 3), new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(3))
        ).ignoreVines().build())
            .decorate(ConfiguredFeatures.Decorators.SQUARE_HEIGHTMAP_OCEAN_FLOOR_NO_WATER)
            .decorate(Decorator.COUNT_EXTRA.configure(new CountExtraDecoratorConfig(1, 0.05F, 1)));

        TREE = RegistryHandler.configuredFeature(ID, configuredFeature);
    }

    @Override
    public void init() {
        RegistryHelper.addBlocksToBlockEntity(BlockEntityType.SIGN, SIGN_BLOCK, WALL_SIGN_BLOCK);
        BiomeHelper.addFeatureToBiomeCategories(TREE, Biome.Category.SAVANNA, GenerationStep.Feature.VEGETAL_DECORATION);
    }
}
