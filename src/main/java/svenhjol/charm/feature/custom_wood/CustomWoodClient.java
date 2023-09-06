package svenhjol.charm.feature.custom_wood;

import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmFeature;

import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;

@ClientFeature(priority = -10)
public class CustomWoodClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(CustomWood.class));
    }

    @Override
    public void register() {
        var registry = CharmClient.instance().registry();

        if (!isEnabled()) return;

        CustomWood.getHolders().forEach(
            (material, holder) -> {
                var materialName = holder.getMaterialName();
                var woodType = holder.getWoodType();

                // Register boat models.
                holder.getBoat().ifPresent(boat -> {
                    registry.modelLayer(
                        () -> new ModelLayerLocation(Charm.instance().makeId("boat/" + materialName), "main"),
                        BoatModel::createBodyModel);

                    registry.modelLayer(
                        () -> new ModelLayerLocation(Charm.instance().makeId("chest_boat/" + materialName), "main"),
                        ChestBoatModel::createBodyModel);
                });

                // Register sign material.
                holder.getSign().ifPresent(
                    sign -> registry.signMaterial(() -> woodType));

            });

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
