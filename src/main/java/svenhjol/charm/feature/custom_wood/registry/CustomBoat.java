package svenhjol.charm.feature.custom_wood.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import svenhjol.charm.feature.custom_wood.CustomWoodHelper;
import svenhjol.charm.feature.custom_wood.CustomWoodHolder;
import svenhjol.charmony.item.CharmBoatItem;

import java.util.Locale;
import java.util.function.Supplier;

public class CustomBoat {
    public final Supplier<CharmBoatItem> boat;
    public final Supplier<CharmBoatItem> chestBoat;

    public CustomBoat(CustomWoodHolder holder) {
        var modId = holder.getModId();
        var materialName = holder.getMaterialName();
        var boatType = Boat.Type.valueOf((holder.getModId() + "_" + materialName).toUpperCase(Locale.ROOT));

        boat = holder.getRegistry().item(materialName + "_boat", () -> new CharmBoatItem(holder.getFeature(), false, boatType));
        chestBoat = holder.getRegistry().item(materialName + "_chest_boat", () -> new CharmBoatItem(holder.getFeature(), true, boatType));

        CustomWoodHelper.setItemForBoat(boatType, boat);
        CustomWoodHelper.setItemForChestBoat(boatType, chestBoat);

        // It's too early to set the planks for the boat, so defer them here.
        CustomWoodHelper.setPlanksForBoat(boatType, new ResourceLocation(modId, materialName + "_planks"));

        holder.addCreativeTabItem(CustomWoodHelper.BOATS, boat);
        holder.addCreativeTabItem(CustomWoodHelper.CHEST_BOATS, boat);
    }
}
