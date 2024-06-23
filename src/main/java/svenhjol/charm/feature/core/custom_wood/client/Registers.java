package svenhjol.charm.feature.core.custom_wood.client;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.state.properties.ChestType;
import svenhjol.charm.charmony.event.BlockItemRenderEvent;
import svenhjol.charm.charmony.event.ClientEntityJoinEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.core.custom_wood.CustomWoodClient;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class Registers extends RegisterHolder<CustomWoodClient> {
    public static final Map<CustomType, List<ResourceKey<CreativeModeTab>>> CUSTOM_TYPE_CREATIVE_TABS = new LinkedHashMap<>();

    public Registers(CustomWoodClient feature) {
        super(feature);
        var registry = feature.registry();
        var chestTypes = List.of(ChestType.SINGLE, ChestType.LEFT, ChestType.RIGHT);
        var holders = feature.linked().registers.holders;
        var materials = holders.keySet();

        for (var material : materials) {
            var holder = holders.get(material);
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
        }

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
        
        // When the player enters the game this will apply fixes to custom sign items.
        ClientEntityJoinEvent.INSTANCE.handle(feature().handlers::clientEntityJoin);
        
        // Build the creative menu.
        var table = feature().linked().registers.getItemCreativeTabs();
        var map = table.rowMap();

        for (var item : map.keySet()) {
            for (var entry : map.get(item).entrySet()) {
                var after = entry.getKey();
                var customType = entry.getValue();
                var tabs = CUSTOM_TYPE_CREATIVE_TABS.get(customType);

                for (var tab : tabs) {
                    registry.itemTab(item.get(), tab, after.get());
                }
            }
        }
    }

    static {
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.BARREL, List.of(CreativeModeTabs.FUNCTIONAL_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.BOAT, List.of(CreativeModeTabs.TOOLS_AND_UTILITIES));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.BOOKSHELF, List.of(CreativeModeTabs.FUNCTIONAL_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.BUTTON, List.of(CreativeModeTabs.FUNCTIONAL_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.CHEST, List.of(CreativeModeTabs.FUNCTIONAL_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.CHEST_BOAT, List.of(CreativeModeTabs.TOOLS_AND_UTILITIES));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.CHISELED_BOOKSHELF, List.of(CreativeModeTabs.FUNCTIONAL_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.DOOR, List.of(CreativeModeTabs.BUILDING_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.FENCE, List.of(CreativeModeTabs.BUILDING_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.GATE, List.of(CreativeModeTabs.BUILDING_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.HANGING_SIGN, List.of(CreativeModeTabs.FUNCTIONAL_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.LADDER, List.of(CreativeModeTabs.FUNCTIONAL_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.LEAVES, List.of(CreativeModeTabs.NATURAL_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.LOG, List.of(CreativeModeTabs.BUILDING_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.PLANKS, List.of(CreativeModeTabs.BUILDING_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.PRESSURE_PLATE, List.of(CreativeModeTabs.BUILDING_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.SAPLING, List.of(CreativeModeTabs.NATURAL_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.SIGN, List.of(CreativeModeTabs.FUNCTIONAL_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.SLAB, List.of(CreativeModeTabs.BUILDING_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.STAIRS, List.of(CreativeModeTabs.BUILDING_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.STRIPPED_LOG, List.of(CreativeModeTabs.BUILDING_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.STRIPPED_WOOD, List.of(CreativeModeTabs.BUILDING_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.TRAPDOOR, List.of(CreativeModeTabs.BUILDING_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.TRAPPED_CHEST, List.of(CreativeModeTabs.FUNCTIONAL_BLOCKS));
        CUSTOM_TYPE_CREATIVE_TABS.put(CustomType.WOOD, List.of(CreativeModeTabs.BUILDING_BLOCKS));
    }
}
