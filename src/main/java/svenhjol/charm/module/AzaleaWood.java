package svenhjol.charm.module;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.SignType;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.*;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.helper.RegistryHelper;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.base.item.CharmBoatItem;
import svenhjol.charm.base.item.CharmSignItem;
import svenhjol.charm.block.*;
import svenhjol.charm.client.AzaleaWoodClient;
import svenhjol.charm.enums.CharmWoodMaterial;
import svenhjol.charm.item.AzaleaItems;
import svenhjol.charm.mixin.accessor.ConfiguredFeaturesAccessor;
import svenhjol.charm.mixin.accessor.TreeFeatureConfigAccessor;

@Module(mod = Charm.MOD_ID, client = AzaleaWoodClient.class, description = "Azalea wood is obtainable from naturally occurring azalea trees or by growing azalea saplings.")
public class AzaleaWood extends CharmModule {
    public static Identifier ID = new Identifier(Charm.MOD_ID, "azalea");

    public static SignType SIGN_TYPE;

    public static CharmWoodenButtonBlock BUTTON;
    public static CharmDoorBlock DOOR;
    public static CharmFenceBlock FENCE;
    public static CharmFenceGateBlock FENCE_GATE;
    public static CharmLogBlock LOG;
    public static CharmPlanksBlock PLANKS;
    public static CharmPressurePlate PRESSURE_PLATE;
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
        PLANKS = new AzaleaBlocks.AzaleaPlanksBlock(this);

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

        BOAT = new AzaleaItems.AzaleaBoatItem(this);
        SIGN_ITEM = new AzaleaItems.AzaleaSignItem(this);

        BARREL = VariantBarrels.registerBarrel(this, CharmWoodMaterial.AZALEA);
        BOOKCASE = Bookcases.registerBookcase(this, CharmWoodMaterial.AZALEA);
        BOOKSHELF = VariantBookshelves.registerBookshelf(this, CharmWoodMaterial.AZALEA);
        CHEST = VariantChests.registerChest(this, CharmWoodMaterial.AZALEA);
        LADDER = VariantLadders.registerLadder(this, CharmWoodMaterial.AZALEA);
        TRAPPED_CHEST = VariantChests.registerTrappedChest(this, CharmWoodMaterial.AZALEA);
    }

    @Override
    public void init() {
        ConfiguredFeature<TreeFeatureConfig, ?> feature = ConfiguredFeaturesAccessor.getAzaleaTree();
        TreeFeatureConfig config = feature.getConfig();
        ((TreeFeatureConfigAccessor)config).setTrunkProvider(new SimpleBlockStateProvider(AzaleaWood.LOG.getDefaultState()));
        RegistryHelper.addBlocksToBlockEntity(BlockEntityType.SIGN, SIGN_BLOCK, WALL_SIGN_BLOCK);
    }
}
