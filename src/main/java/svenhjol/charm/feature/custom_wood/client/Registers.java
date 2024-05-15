package svenhjol.charm.feature.custom_wood.client;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.ChestType;
import svenhjol.charm.api.event.BlockItemRenderEvent;
import svenhjol.charm.feature.custom_wood.CustomWoodClient;
import svenhjol.charm.feature.custom_wood.common.CustomWoodHelper;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.List;
import java.util.Optional;

public final class Registers extends RegisterHolder<CustomWoodClient> {
    public Registers(CustomWoodClient feature) {
        super(feature);
        var registry = feature.registry();
        var chestTypes = List.of(ChestType.SINGLE, ChestType.LEFT, ChestType.RIGHT);

        // Bind the chest block entities to their custom renderers.
        feature.registry().blockEntityRenderer(feature().common().registers.chestBlockEntity,
            () -> BlockEntityRenderer::new);
        feature.registry().blockEntityRenderer(feature().common().registers.trappedChestBlockEntity,
            () -> BlockEntityRenderer::new);

        // Register client ops for each holder.
        feature.common().registers.holders.forEach(
            (material, holder) -> {
                var materialName = holder.getMaterialName();
                var woodType = holder.woodType();

                // BOAT: Register models.
                holder.boat().ifPresent(boat -> {
                    registry.modelLayer(
                        () -> new ModelLayerLocation(registry.id("boat/" + materialName), "main"),
                        BoatModel::createBodyModel);

                    registry.modelLayer(
                        () -> new ModelLayerLocation(registry.id("chest_boat/" + materialName), "main"),
                        ChestBoatModel::createBodyModel);
                });

                // CHEST: Add textures to the custom block entity renderer.
                holder.chest().ifPresent(chest -> {
                    var base = "normal";
                    for (var chestType : chestTypes) {
                        var chestTypeName = chestType == ChestType.SINGLE ? "" : "_" + chestType.getSerializedName().toLowerCase();
                        var textureId = new ResourceLocation(holder.ownerId(), "entity/chest/" + material.getSerializedName() + "_" + base + chestTypeName);

                        // Store the texture reference in the chest renderer.
                        BlockEntityRenderer.addTexture(material, chestType, textureId, false);
                    }
                });

                // LADDER: Cut out transparent areas.
                holder.ladder().ifPresent(ladder -> registry.blockRenderType(ladder.block, RenderType::cutout));

                // SIGN: Register material.
                holder.sign().ifPresent(sign -> registry.signMaterial(() -> woodType));

                // TRAPPED CHEST: Add textures to the custom block entity renderer.
                holder.trappedChest().ifPresent(trappedChest -> {
                    var base = "trapped";
                    for (var chestType : chestTypes) {
                        var chestTypeName = chestType == ChestType.SINGLE ? "" : "_" + chestType.getSerializedName().toLowerCase();
                        var textureId = new ResourceLocation(holder.ownerId(), "entity/chest/" + material.getSerializedName() + "_" + base + chestTypeName);

                        // Store the texture reference in the chest renderer.
                        BlockEntityRenderer.addTexture(material, chestType, textureId, true);
                    }
                });
            });
    }

    @Override
    public void onEnabled() {
        var registry = feature().registry();

        // Without this event handler the chest textures will be invisible.
        BlockItemRenderEvent.INSTANCE.handle(feature().handlers::renderChestBlockItem);

        // Build the creative menu.
        CustomWoodHelper.getCreativeTabItems().forEach(
            (mod, map) -> {
                CustomWoodHelper.BUILDING_BLOCKS.forEach(
                    name -> Optional.ofNullable(map.get(name)).ifPresent(
                        items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.BUILDING_BLOCKS, Items.ACACIA_BUTTON))));

                Optional.ofNullable(map.get(CustomWoodHelper.BARRELS)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.BARREL)));

                Optional.ofNullable(map.get(CustomWoodHelper.BOATS)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.TOOLS_AND_UTILITIES, Items.ACACIA_BOAT)));

                Optional.ofNullable(map.get(CustomWoodHelper.BOOKSHELVES)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.BOOKSHELF)));

                Optional.ofNullable(map.get(CustomWoodHelper.CHESTS)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.CHEST)));

                Optional.ofNullable(map.get(CustomWoodHelper.CHEST_BOATS)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.TOOLS_AND_UTILITIES, Items.ACACIA_CHEST_BOAT)));

                Optional.ofNullable(map.get(CustomWoodHelper.CHISELED_BOOKSHELVES)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.CHISELED_BOOKSHELF)));

                Optional.ofNullable(map.get(CustomWoodHelper.HANGING_SIGNS)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.ACACIA_HANGING_SIGN)));

                Optional.ofNullable(map.get(CustomWoodHelper.LEAVES)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.NATURAL_BLOCKS, Items.ACACIA_LEAVES)));

                Optional.ofNullable(map.get(CustomWoodHelper.LADDERS)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.NATURAL_BLOCKS, Items.LADDER)));

                Optional.ofNullable(map.get(CustomWoodHelper.LOGS)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.NATURAL_BLOCKS, Items.ACACIA_LOG)));

                Optional.ofNullable(map.get(CustomWoodHelper.SAPLINGS)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.NATURAL_BLOCKS, Items.ACACIA_SAPLING)));

                Optional.ofNullable(map.get(CustomWoodHelper.SIGNS)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.ACACIA_SIGN)));

                Optional.ofNullable(map.get(CustomWoodHelper.TRAPPED_CHESTS)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.TRAPPED_CHEST)));
            });
    }
}
