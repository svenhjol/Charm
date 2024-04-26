package svenhjol.charm.feature.custom_wood;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.foundation.Register;

import java.util.Optional;

public class ClientRegister extends Register<CustomWoodClient> {
    public ClientRegister(CustomWoodClient feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();

        CustomWood.getHolders().forEach(
            (material, holder) -> {
                var materialName = holder.getMaterialName();
                var woodType = holder.woodType();

                // Register boat models.
                holder.getBoat().ifPresent(boat -> {
                    registry.modelLayer(
                        () -> new ModelLayerLocation(registry.id("boat/" + materialName), "main"),
                        BoatModel::createBodyModel);

                    registry.modelLayer(
                        () -> new ModelLayerLocation(registry.id("chest_boat/" + materialName), "main"),
                        ChestBoatModel::createBodyModel);
                });

                // Register sign material.
                holder.getSign().ifPresent(sign -> registry.signMaterial(() -> woodType));
            });
    }

    @Override
    public void onEnabled() {
        var registry = feature.registry();

        // Build the creative menu.
        CustomWoodHelper.getCreativeTabItems().forEach(
            (mod, map) -> {
                CustomWoodHelper.BUILDING_BLOCKS.forEach(
                    name -> Optional.ofNullable(map.get(name)).ifPresent(
                        item -> registry.itemTab(item, CreativeModeTabs.BUILDING_BLOCKS, Items.ACACIA_BUTTON)));

                Optional.ofNullable(map.get(CustomWoodHelper.BOATS)).ifPresent(
                    item -> registry.itemTab(item, CreativeModeTabs.TOOLS_AND_UTILITIES, Items.ACACIA_BOAT));

                Optional.ofNullable(map.get(CustomWoodHelper.CHEST_BOATS)).ifPresent(
                    item -> registry.itemTab(item, CreativeModeTabs.TOOLS_AND_UTILITIES, Items.ACACIA_CHEST_BOAT));

                Optional.ofNullable(map.get(CustomWoodHelper.HANGING_SIGNS)).ifPresent(
                    item -> registry.itemTab(item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.ACACIA_HANGING_SIGN));

                Optional.ofNullable(map.get(CustomWoodHelper.LEAVES)).ifPresent(
                    item -> registry.itemTab(item, CreativeModeTabs.NATURAL_BLOCKS, Items.ACACIA_LEAVES));

                Optional.ofNullable(map.get(CustomWoodHelper.LOGS)).ifPresent(
                    item -> registry.itemTab(item, CreativeModeTabs.NATURAL_BLOCKS, Items.ACACIA_LOG));

                Optional.ofNullable(map.get(CustomWoodHelper.SAPLINGS)).ifPresent(
                    item -> registry.itemTab(item, CreativeModeTabs.NATURAL_BLOCKS, Items.ACACIA_SAPLING));

                Optional.ofNullable(map.get(CustomWoodHelper.SIGNS)).ifPresent(
                    item -> registry.itemTab(item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.ACACIA_SIGN));
            });
    }
}
