package svenhjol.charm.feature.variant_wood.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.ChestType;
import svenhjol.charm.api.event.BlockItemRenderEvent;
import svenhjol.charm.feature.variant_wood.VariantWood;
import svenhjol.charm.feature.variant_wood.VariantWoodClient;
import svenhjol.charm.feature.variant_wood.common.*;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.ArrayList;
import java.util.Collections;

public final class Registers extends RegisterHolder<VariantWoodClient> {
    public Registers(VariantWoodClient feature) {
        super(feature);

        // Bind the chest block entities to their custom renderers.
        feature.registry().blockEntityRenderer(feature.common.registers.chestBlockEntity,
            () -> BlockEntityRenderer::new);
        feature.registry().blockEntityRenderer(feature.common.registers.trappedChestBlockEntity,
            () -> BlockEntityRenderer::new);
    }

    @Override
    public void onEnabled() {
        var commonRegisters = feature().common.registers;
        var registry = feature().registry();

        // Add items to the creative menu.
        if (VariantWood.variantBarrels) {
            var barrels = new ArrayList<>(commonRegisters.barrels.values());
            Collections.reverse(barrels);

            for (VariantBarrel barrel : barrels) {
                registry.itemTab(barrel.item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.BARREL);
            }
        }

        if (VariantWood.variantBookshelves) {
            var bookshelves = new ArrayList<>(commonRegisters.bookshelves.values());
            Collections.reverse(bookshelves);

            for (VariantBookshelf bookshelf : bookshelves) {
                registry.itemTab(bookshelf.item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.BOOKSHELF);
            }
        }

        if (VariantWood.variantChests) {
            var chests = new ArrayList<>(commonRegisters.chests.values());
            var trappedChests = new ArrayList<>(commonRegisters.trappedChests.values());

            Collections.reverse(chests);
            Collections.reverse(trappedChests);

            for (VariantTrappedChest trappedChest : trappedChests) {
                registry.itemTab(trappedChest.item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.CHEST);
            }
            for (VariantChest chest : chests) {
                registry.itemTab(chest.item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.CHEST);
            }
        }

        if (VariantWood.variantChiseledBookshelves) {
            var chiseledBookshelves = new ArrayList<>(commonRegisters.chiseledBookshelves.values());
            Collections.reverse(chiseledBookshelves);

            for (VariantChiseledBookshelf chiseledBookshelf : chiseledBookshelves) {
                registry.itemTab(chiseledBookshelf.item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.CHISELED_BOOKSHELF);
            }
        }

        if (VariantWood.variantLadders) {
            var ladders = new ArrayList<>(commonRegisters.ladders.values());
            Collections.reverse(ladders);

            for (VariantLadder ladder : ladders) {
                registry.itemTab(ladder.item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.LADDER);
            }
        }

        // Without this event handler the chest texture will be invisible.
        BlockItemRenderEvent.INSTANCE.handle(feature().handlers::renderChestBlockItem);

        commonRegisters.chests.forEach((material, chest) -> {
            String[] bases = {"trapped", "normal"};
            ChestType[] chestTypes = {ChestType.SINGLE, ChestType.LEFT, ChestType.RIGHT};

            for (var base : bases) {
                for (var chestType : chestTypes) {
                    var chestTypeName = chestType == ChestType.SINGLE ? "" : "_" + chestType.getSerializedName().toLowerCase();
                    var textureId = new ResourceLocation(chest.modId, "entity/chest/" + material.getSerializedName() + "_" + base + chestTypeName);

                    // Store the texture reference in the chest renderer.
                    BlockEntityRenderer.addTexture(material, chestType, textureId, base.equals("trapped"));
                }
            }
        });

        // Cut out transparent areas of the ladder.
        commonRegisters.ladders.forEach(
            (material, ladder) -> registry.blockRenderType(ladder.block, RenderType::cutout));
    }
}