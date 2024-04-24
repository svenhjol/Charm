package svenhjol.charm.feature.variant_wood;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import svenhjol.charm.api.event.BlockItemRenderEvent;
import svenhjol.charm.foundation.Register;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class ClientRegister extends Register<VariantWoodClient> {
    private VariantChestBlockEntity cachedNormalChest;
    private VariantTrappedChestBlockEntity cachedTrappedChest;

    public ClientRegister(VariantWoodClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        // Bind the chest block entities to their custom renderers.
        feature.registry().blockEntityRenderer(CustomChest.blockEntity,
            () -> VariantChestBlockEntityRenderer::new);
        feature.registry().blockEntityRenderer(CustomTrappedChest.blockEntity,
            () -> VariantChestBlockEntityRenderer::new);
    }

    @Override
    public void onEnabled() {
        var registry = feature.registry();

        // Add items to the creative menu.
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

        // Without this event handler the chest texture will be invisible.
        BlockItemRenderEvent.INSTANCE.handle(this::handleRenderChestBlockItem);

        // Cache the chest block entities for fast lookup by the renderer.
        cachedNormalChest = new VariantChestBlockEntity(BlockPos.ZERO, Blocks.CHEST.defaultBlockState());
        cachedTrappedChest = new VariantTrappedChestBlockEntity(BlockPos.ZERO, Blocks.TRAPPED_CHEST.defaultBlockState());

        VariantWood.CHESTS.forEach((material, chest) -> {
            String[] bases = {"trapped", "normal"};
            ChestType[] chestTypes = {ChestType.SINGLE, ChestType.LEFT, ChestType.RIGHT};

            for (var base : bases) {
                for (var chestType : chestTypes) {
                    var chestTypeName = chestType == ChestType.SINGLE ? "" : "_" + chestType.getSerializedName().toLowerCase();
                    var textureId = new ResourceLocation(chest.modId, "entity/chest/" + material.getSerializedName() + "_" + base + chestTypeName);

                    // Store the texture reference in the chest renderer.
                    VariantChestBlockEntityRenderer.addTexture(material, chestType, textureId, base.equals("trapped"));
                }
            }
        });

        // Cut out transparent areas of the ladder.
        VariantWood.LADDERS.forEach(
            (material, ladder) -> registry.blockRenderType(ladder.block, RenderType::cutout));
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
