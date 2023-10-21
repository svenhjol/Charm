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
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_wood.block.VariantChestBlock;
import svenhjol.charm.feature.variant_wood.block.VariantTrappedChestBlock;
import svenhjol.charm.feature.variant_wood.entity.VariantChestBlockEntity;
import svenhjol.charm.feature.variant_wood.entity.VariantTrappedChestBlockEntity;
import svenhjol.charm.feature.variant_wood.registry.*;
import svenhjol.charm.feature.variant_wood.renderer.VariantChestBlockEntityRenderer;
import svenhjol.charmony.base.Mods;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.iface.IClientMod;
import svenhjol.charmony.iface.IClientRegistry;
import svenhjol.charmony_api.event.BlockItemRenderEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class VariantWoodClient extends ClientFeature {
    private VariantChestBlockEntity cachedNormalChest;
    private VariantTrappedChestBlockEntity cachedTrappedChest;

    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return VariantWood.class;
    }

    @Override
    public void register() {
        var registry = mod().registry();

        registerChests(registry);

        // Add items to the creative menu.
        if (isEnabled()) {
            if (VariantWood.variantBarrels) {
                var barrels = new ArrayList<>(VariantWood.BARRELS.values());
                Collections.reverse(barrels);

                for (CustomBarrel barrel : barrels) {
                    registry.itemTab(barrel.item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.BARREL);
                }
            }

            if (VariantWood.variantBookshelves) {
                var bookshelves = new ArrayList<>(VariantWood.BOOKSHELVES.values());
                Collections.reverse(bookshelves);

                for (CustomBookshelf bookshelf : bookshelves) {
                    registry.itemTab(bookshelf.item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.BOOKSHELF);
                }
            }

            if (VariantWood.variantChests) {
                var chests = new ArrayList<>(VariantWood.CHESTS.values());
                var trappedChests = new ArrayList<>(VariantWood.TRAPPED_CHESTS.values());

                Collections.reverse(chests);
                Collections.reverse(trappedChests);

                for (CustomTrappedChest trappedChest : trappedChests) {
                    registry.itemTab(trappedChest.item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.CHEST);
                }
                for (CustomChest chest : chests) {
                    registry.itemTab(chest.item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.CHEST);
                }
            }

            if (VariantWood.variantChiseledBookshelves) {
                var chiseledBookshelves = new ArrayList<>(VariantWood.CHISELED_BOOKSHELVES.values());
                Collections.reverse(chiseledBookshelves);

                for (CustomChiseledBookshelf chiseledBookshelf : chiseledBookshelves) {
                    registry.itemTab(chiseledBookshelf.item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.CHISELED_BOOKSHELF);
                }
            }

            if (VariantWood.variantLadders) {
                var ladders = new ArrayList<>(VariantWood.LADDERS.values());
                Collections.reverse(ladders);

                for (CustomLadder ladder : ladders) {
                    registry.itemTab(ladder.item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.LADDER);
                }
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

    @Override
    public void runAlways() {
        var client = Mods.client(Charm.ID);

        runChests(client);
        runLadders(client);
    }

    private void runChests(IClientMod client) {
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
                    var textureId = client.id("entity/chest/" + material.getSerializedName() + "_" + base + chestTypeName);

                    // Store the texture reference in the chest renderer.
                    VariantChestBlockEntityRenderer.addTexture(material, chestType, textureId, base.equals("trapped"));
                }
            }
        });
    }

    private void runLadders(IClientMod client) {
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
}
