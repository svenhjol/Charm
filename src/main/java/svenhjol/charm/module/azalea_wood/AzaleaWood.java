package svenhjol.charm.module.azalea_wood;

import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.block.*;
import svenhjol.charm.enums.CharmWoodMaterial;
import svenhjol.charm.item.CharmSignItem;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.bookcases.BookcaseBlock;
import svenhjol.charm.module.bookcases.Bookcases;
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

@CommonModule(mod = Charm.MOD_ID, description = "Azalea wood is obtainable from naturally occurring azalea trees or by growing azalea saplings.")
public class AzaleaWood extends CharmModule {
    public static ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "azalea");

    public static WoodType SIGN_TYPE;
    public static CharmWoodenButtonBlock BUTTON;
    public static CharmDoorBlock DOOR;
    public static CharmFenceBlock FENCE;
    public static CharmFenceGateBlock FENCE_GATE;
    public static CharmLogBlock LOG;
    public static CharmPlanksBlock PLANKS;
    public static CharmPressurePlate PRESSURE_PLATE;
    public static CharmSignBlock SIGN_BLOCK;
    public static CharmWoodenSlabBlock SLAB;
    public static CharmWoodenStairBlock STAIRS;
    public static CharmLogBlock STRIPPED_LOG;
    public static CharmLogBlock STRIPPED_WOOD;
    public static CharmTrapdoorBlock TRAPDOOR;
    public static CharmWallSignBlock WALL_SIGN_BLOCK;
    public static CharmLogBlock WOOD;
    public static VariantBarrelBlock BARREL;
    public static VariantBookshelfBlock BOOKSHELF;
    public static VariantChestBlock CHEST;
    public static VariantLadderBlock LADDER;
    public static VariantTrappedChestBlock TRAPPED_CHEST;

    public static BoatItem BOAT;
    public static BoatItem CHEST_BOAT;
    public static Boat.Type BOAT_TYPE;

    public static CharmSignItem SIGN_ITEM;
    public static BookcaseBlock BOOKCASE;

    @Override
    public void register() {
        // must init these first, other blocks depend on them being registered
        SIGN_TYPE = CommonRegistry.signType(ID);
        PLANKS = new AzaleaBlocks.AzaleaPlanksBlock(this);

        // Enum references planks before they're registered. Re-register here.
        BOAT_TYPE = Boat.Type.valueOf(CharmWoodMaterial.AZALEA.name());
        BOAT_TYPE.planks = PLANKS;

        BUTTON = new AzaleaBlocks.AzaleaButtonBlock(this);
        DOOR = new AzaleaBlocks.AzaleaDoorBlock(this);
        FENCE = new AzaleaBlocks.AzaleaFenceBlock(this);
        FENCE_GATE = new AzaleaBlocks.AzaleaFenceGateBlock(this);
        LOG = new AzaleaBlocks.AzaleaLogBlock(this);
        STRIPPED_LOG = new AzaleaBlocks.StrippedAzaleaLogBlock(this);
        PRESSURE_PLATE = new AzaleaBlocks.AzaleaPressurePlateBlock(this);
        SIGN_BLOCK = new AzaleaBlocks.AzaleaSignBlock(this);
        SLAB = new AzaleaBlocks.AzaleaSlabBlock(this);
        STAIRS = new AzaleaBlocks.AzaleaStairsBlock(this);
        TRAPDOOR = new AzaleaBlocks.AzaleaTrapdoorBlock(this);
        WALL_SIGN_BLOCK = new AzaleaBlocks.AzaleaWallSignBlock(this);
        WOOD = new AzaleaBlocks.AzaleaWoodBlock(this);
        STRIPPED_WOOD = new AzaleaBlocks.StrippedAzaleaWoodBlock(this);
        SIGN_ITEM = new AzaleaItems.AzaleaSignItem(this);

        BARREL = VariantBarrels.registerBarrel(this, CharmWoodMaterial.AZALEA);
        BOOKSHELF = VariantBookshelves.registerBookshelf(this, CharmWoodMaterial.AZALEA);
        CHEST = VariantChests.registerChest(this, CharmWoodMaterial.AZALEA);
        LADDER = VariantLadders.registerLadder(this, CharmWoodMaterial.AZALEA);
        TRAPPED_CHEST = VariantChests.registerTrappedChest(this, CharmWoodMaterial.AZALEA);
        BOOKCASE = Bookcases.registerBookcase(this, CharmWoodMaterial.AZALEA);

        BOAT = new BoatItem(false, BOAT_TYPE, new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TRANSPORTATION));
        CHEST_BOAT = new BoatItem(true, BOAT_TYPE, new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TRANSPORTATION));
        ExtraBoats.registerBoat(ID, BOAT_TYPE, BOAT, CHEST_BOAT);
        VariantChests.CHEST_LAYER_COLORS.put(CharmWoodMaterial.AZALEA.getSerializedName(), 0xffaf9f);
    }

    @Override
    public void runWhenEnabled() {
        ConfiguredFeature<TreeConfiguration, ?> feature = TreeFeatures.AZALEA_TREE.value();
        feature.config().trunkProvider = new SimpleStateProvider(AzaleaWood.LOG.defaultBlockState());

        CommonRegistry.addBlocksToBlockEntity(BlockEntityType.SIGN, SIGN_BLOCK, WALL_SIGN_BLOCK);
        AxeItem.STRIPPABLES.put(LOG, STRIPPED_LOG);
        AxeItem.STRIPPABLES.put(WOOD, STRIPPED_WOOD);
    }
}
