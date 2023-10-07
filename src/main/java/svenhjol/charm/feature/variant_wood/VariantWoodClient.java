package svenhjol.charm.feature.variant_wood;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import svenhjol.charm.CharmClient;
import svenhjol.charm.feature.variant_wood.block.VariantChestBlock;
import svenhjol.charm.feature.variant_wood.block.VariantTrappedChestBlock;
import svenhjol.charm.feature.variant_wood.entity.VariantChestBlockEntity;
import svenhjol.charm.feature.variant_wood.entity.VariantTrappedChestBlockEntity;
import svenhjol.charm.feature.variant_wood.recipe.VariantChestBoatRecipe;
import svenhjol.charm.feature.variant_wood.registry.CustomChest;
import svenhjol.charm.feature.variant_wood.registry.CustomChestBoat;
import svenhjol.charm.feature.variant_wood.registry.CustomTrappedChest;
import svenhjol.charm.feature.variant_wood.renderer.VariantChestBlockEntityRenderer;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.iface.IClientRegistry;
import svenhjol.charmony_api.event.BlockItemRenderEvent;

import java.util.ArrayList;
import java.util.Optional;

@ClientFeature(feature = VariantWood.class)
public class VariantWoodClient extends CharmonyFeature {
    private VariantChestBlockEntity cachedNormalChest;
    private VariantTrappedChestBlockEntity cachedTrappedChest;
    private static final int DEFAULT_CHEST_BOAT_LAYER_COLOR = 0xdf9f43;

    @Override
    public void register() {
        var registry = CharmClient.instance().registry();

        registerChests(registry);
        registerChestBoats(registry);

        // Add items to the creative menu.
        if (isEnabled()) {
            if (VariantWood.variantBarrels) {
                VariantWood.BARRELS.forEach((material, barrel) -> {
                    registry.itemTab(barrel.item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.BARREL);
                    registry.itemTab(barrel.item, CreativeModeTabs.REDSTONE_BLOCKS, Items.REDSTONE_ORE);
                });
            }

            if (VariantWood.variantBookshelves) {
                VariantWood.BOOKSHELVES.forEach((material, bookshelf) ->
                    registry.itemTab(bookshelf.item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.BOOKSHELF));
            }

            if (VariantWood.variantChests) {
                VariantWood.CHESTS.forEach((material, chest) -> {
                    registry.itemTab(chest.item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.CHEST);
                    registry.itemTab(chest.item, CreativeModeTabs.REDSTONE_BLOCKS, Items.REDSTONE_ORE);
                });
                VariantWood.TRAPPED_CHESTS.forEach((material, trappedChest) ->
                    registry.itemTab(trappedChest.item, CreativeModeTabs.REDSTONE_BLOCKS, Items.REDSTONE_ORE));
            }

            if (VariantWood.variantChiseledBookshelves) {
                VariantWood.CHISELED_BOOKSHELVES.forEach((material, chiseledBookshelf) -> {
                    registry.itemTab(chiseledBookshelf.item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.CHISELED_BOOKSHELF);
                    registry.itemTab(chiseledBookshelf.item, CreativeModeTabs.REDSTONE_BLOCKS, Items.REDSTONE_ORE);
                });
            }

            if (VariantWood.variantLadders) {
                VariantWood.LADDERS.forEach((material, ladder) ->
                    registry.itemTab(ladder.item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.LADDER));
            }
        }
    }

    private void registerChests(IClientRegistry registry) {
        // Bind the chest block entities to their custom renderers.
        registry.blockEntityRenderer(CustomChest.blockEntity,
            () -> VariantChestBlockEntityRenderer::new);
        registry.blockEntityRenderer(CustomTrappedChest.blockEntity,
            () -> VariantChestBlockEntityRenderer::new);
    }

    private void registerChestBoats(IClientRegistry registry) {
        // Assign a handler method to each of the chest boat item icons.
        registry.itemColor(this::handleChestBoatLayerColor,
            new ArrayList<>(CustomChestBoat.boatPairs.values()));
    }

    @Override
    public void runAlways() {
        var client = CharmClient.instance();

        runChests(client);
        runLadders(client);
    }

    private void runChests(CharmClient client) {
        // Cache the chest block entities for fast lookup by the renderer.
        cachedNormalChest = new VariantChestBlockEntity(BlockPos.ZERO, Blocks.CHEST.defaultBlockState());
        cachedTrappedChest = new VariantTrappedChestBlockEntity(BlockPos.ZERO, Blocks.TRAPPED_CHEST.defaultBlockState());

        BlockItemRenderEvent.INSTANCE.handle(this::handleRenderChestBlockItem);

        VariantWood.CHESTS.forEach((material, chest) -> {
            String[] bases = {"trapped", "normal"};
            ChestType[] chestTypes = {ChestType.SINGLE, ChestType.LEFT, ChestType.RIGHT};

            for (var base : bases) {
                for (var chestType : chestTypes) {
                    var chestTypeName = chestType == ChestType.SINGLE ? "" : "_" + chestType.getSerializedName().toLowerCase();
                    var textureId = client.makeId("entity/chest/" + material.getSerializedName() + "_" + base + chestTypeName);

                    // Store the texture reference in the chest renderer.
                    VariantChestBlockEntityRenderer.addTexture(material, chestType, textureId, base.equals("trapped"));
                }
            }
        });
    }

    private void runLadders(CharmClient client) {
        // Cut out transparent areas of the ladder.
        VariantWood.LADDERS.forEach(
            (material, ladder) -> client.registry().blockRenderType(ladder.block, RenderType::cutout));
    }

    private Optional<BlockEntity> handleRenderChestBlockItem(ItemStack itemStack, Block block) {
        if (block instanceof VariantChestBlock chest) {
            cachedNormalChest.setMaterial(chest.getMaterial());
            return Optional.of(cachedNormalChest);

        } else if (block instanceof VariantTrappedChestBlock trappedChest) {
            cachedTrappedChest.setMaterial(trappedChest.getMaterial());
            return Optional.of(cachedTrappedChest);
        }

        return Optional.empty();
    }

    private int handleChestBoatLayerColor(ItemStack stack, int layer) {
        if (layer == 0) return -1;

        var tag = stack.getTag();
        if (tag != null && tag.contains(VariantChestBoatRecipe.CHEST_TYPE_TAG)) {
            var type = tag.getString(VariantChestBoatRecipe.CHEST_TYPE_TAG);
            return CustomChestBoat.layerColors.getOrDefault(type, DEFAULT_CHEST_BOAT_LAYER_COLOR);
        }

        return DEFAULT_CHEST_BOAT_LAYER_COLOR;
    }
}
