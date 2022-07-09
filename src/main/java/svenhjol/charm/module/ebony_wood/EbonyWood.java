package svenhjol.charm.module.ebony_wood;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.block.*;
import svenhjol.charm.enums.CharmWoodMaterial;
import svenhjol.charm.item.CharmBoatItem;
import svenhjol.charm.item.CharmSignItem;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.bookcases.BookcaseBlock;
import svenhjol.charm.module.bookcases.Bookcases;
import svenhjol.charm.module.ebony_wood.EbonyBlocks.*;
import svenhjol.charm.module.ebony_wood.EbonyItems.EbonyBoatItem;
import svenhjol.charm.module.ebony_wood.EbonyItems.EbonySignItem;
import svenhjol.charm.module.extra_boats.ExtraBoats;
import svenhjol.charm.module.variant_barrels.VariantBarrelBlock;
import svenhjol.charm.module.variant_barrels.VariantBarrels;
import svenhjol.charm.module.variant_bookshelves.VariantBookshelfBlock;
import svenhjol.charm.module.variant_bookshelves.VariantBookshelves;
import svenhjol.charm.module.variant_chests.VariantChestBlock;
import svenhjol.charm.module.variant_chests.VariantChests;
import svenhjol.charm.module.variant_chests.VariantTrappedChestBlock;
import svenhjol.charm.module.variant_ladders.VariantLadderBlock;
import svenhjol.charm.module.variant_ladders.VariantLadders;
import svenhjol.charm.registry.CommonRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

@CommonModule(mod = Charm.MOD_ID, description = "Ebony is a very dark grey wood. Saplings can be found in Woodland Mansion chests.")
public class EbonyWood extends CharmModule {
    private static final Map<ResourceLocation, Float> LOOT_TABLES = new HashMap<>();

    public static ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "ebony");
    public static Holder<ConfiguredFeature<TreeConfiguration, ?>> TREE;
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
    public static CharmWoodenSlabBlock SLAB;
    public static CharmWoodenStairBlock STAIRS;
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
    public static LootItemFunctionType LOOT_FUNCTION;
    public static String EBONY = CharmWoodMaterial.EBONY.getSerializedName();

    @Override
    public void register() {
        // must init these first, other blocks depend on them being registered
        SIGN_TYPE = CommonRegistry.signType(ID);
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
        BOOKSHELF = VariantBookshelves.registerBookshelf(this, CharmWoodMaterial.EBONY);
        CHEST = VariantChests.registerChest(this, CharmWoodMaterial.EBONY);
        LADDER = VariantLadders.registerLadder(this, CharmWoodMaterial.EBONY);
        TRAPPED_CHEST = VariantChests.registerTrappedChest(this, CharmWoodMaterial.EBONY);

        var treeConfiguration = new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(LOG.defaultBlockState()),
            new ForkingTrunkPlacer(5, 6, 2),
            BlockStateProvider.simple(LEAVES.defaultBlockState()),
            new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(2), 4),
            new TwoLayersFeatureSize(1, 0, 2, OptionalInt.of(3))
        ).build();

        TREE = FeatureUtils.register(ID.toString(), Feature.TREE, treeConfiguration);
        BOOKCASE = Bookcases.registerBookcase(this, CharmWoodMaterial.EBONY);
        ExtraBoats.registerBoat(EBONY, BOAT);

        LOOT_FUNCTION = CommonRegistry.lootFunctionType(
            new ResourceLocation(Charm.MOD_ID, "ebony_sapling_loot"),
            new LootItemFunctionType(new EbonySaplingLootFunction.Serializer()));
    }

    @Override
    public void runWhenEnabled() {
        CommonRegistry.addBlocksToBlockEntity(BlockEntityType.SIGN, SIGN_BLOCK, WALL_SIGN_BLOCK);
        LootTableEvents.MODIFY.register(this::handleLootTables);
        registerLootTable(BuiltInLootTables.WOODLAND_MANSION, 0.25F);
        AxeItem.STRIPPABLES.put(LOG, STRIPPED_LOG);
        AxeItem.STRIPPABLES.put(WOOD, STRIPPED_WOOD);
    }

    private void handleLootTables(ResourceManager resourceManager, LootTables lootTables, ResourceLocation id, LootTable.Builder builder, LootTableSource lootTableSource) {
        if (LOOT_TABLES.containsKey(id)) {
            var chance = LOOT_TABLES.get(id);
            var lootPool = LootPool.lootPool()
                .when(LootItemRandomChanceCondition.randomChance(chance))
                .setRolls(UniformGenerator.between(1.0f, 3.0f))
                .add(LootItem.lootTableItem(Items.AIR)
                    .setWeight(1).apply(() -> new EbonySaplingLootFunction(new LootItemCondition[0])));

            builder.withPool(lootPool);
        }
    }

    public static void registerLootTable(ResourceLocation id, float chance) {
        LOOT_TABLES.put(id, chance);
    }
}
