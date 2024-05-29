package svenhjol.charm.feature.core.custom_wood.client;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.ChestType;
import svenhjol.charm.charmony.event.BlockItemRenderEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.core.custom_wood.CustomWoodClient;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;

import java.util.List;
import java.util.Optional;

public final class Registers extends RegisterHolder<CustomWoodClient> {
    public Registers(CustomWoodClient feature) {
        super(feature);
        var registry = feature.registry();
        var chestTypes = List.of(ChestType.SINGLE, ChestType.LEFT, ChestType.RIGHT);

        // Register client ops for each holder.
        feature.linked().registers.holders.forEach(
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
                        CustomBlockEntityRenderer.addTexture(material, chestType, textureId, false);
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
                        CustomBlockEntityRenderer.addTexture(material, chestType, textureId, true);
                    }
                });
            });

        // Bind the chest block entities to their custom renderers.
        feature.registry().blockEntityRenderer(feature().linked().registers.chestBlockEntity,
            () -> CustomBlockEntityRenderer::new);
        feature.registry().blockEntityRenderer(feature().linked().registers.trappedChestBlockEntity,
            () -> CustomBlockEntityRenderer::new);
    }

    @Override
    public void onEnabled() {
        var registry = feature().registry();

        // Without this event handler the chest textures will be invisible.
        BlockItemRenderEvent.INSTANCE.handle(feature().handlers::renderChestBlockItem);

        // Build the creative menu.
        feature().linked().registers.getCreativeTabItems().forEach(
            (mod, map) -> {
                CustomType.BUILDING_BLOCKS.forEach(
                    name -> Optional.ofNullable(map.get(name)).ifPresent(
                        items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.BUILDING_BLOCKS, Items.ACACIA_BUTTON))));

                Optional.ofNullable(map.get(CustomType.BARREL)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.BARREL)));

                Optional.ofNullable(map.get(CustomType.BOAT)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.TOOLS_AND_UTILITIES, Items.ACACIA_BOAT)));

                Optional.ofNullable(map.get(CustomType.BOOKSHELF)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.BOOKSHELF)));

                Optional.ofNullable(map.get(CustomType.CHEST)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.CHEST)));

                Optional.ofNullable(map.get(CustomType.CHEST_BOAT)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.TOOLS_AND_UTILITIES, Items.ACACIA_CHEST_BOAT)));

                Optional.ofNullable(map.get(CustomType.CHISELED_BOOKSHELF)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.CHISELED_BOOKSHELF)));

                Optional.ofNullable(map.get(CustomType.HANGING_SIGN)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.ACACIA_HANGING_SIGN)));

                Optional.ofNullable(map.get(CustomType.LEAVES)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.NATURAL_BLOCKS, Items.ACACIA_LEAVES)));

                Optional.ofNullable(map.get(CustomType.LADDER)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.NATURAL_BLOCKS, Items.LADDER)));

                Optional.ofNullable(map.get(CustomType.LOG)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.NATURAL_BLOCKS, Items.ACACIA_LOG)));

                Optional.ofNullable(map.get(CustomType.SAPLING)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.NATURAL_BLOCKS, Items.ACACIA_SAPLING)));

                Optional.ofNullable(map.get(CustomType.SIGN)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.ACACIA_SIGN)));

                Optional.ofNullable(map.get(CustomType.TRAPPED_CHEST)).ifPresent(
                    items -> items.forEach(item -> registry.itemTab(item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.TRAPPED_CHEST)));
            });
    }
}
