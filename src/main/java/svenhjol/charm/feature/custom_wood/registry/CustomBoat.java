package svenhjol.charm.feature.custom_wood.registry;

import net.minecraft.core.dispenser.BoatDispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import svenhjol.charm.feature.custom_wood.CustomWoodHelper;
import svenhjol.charm.feature.custom_wood.CustomWoodHolder;
import svenhjol.charm.foundation.helper.EnumHelper;

import java.util.Locale;
import java.util.function.Supplier;

public class CustomBoat {
    public final Supplier<BoatItem> boat;
    public final Supplier<BoatItem> chestBoat;

    public CustomBoat(CustomWoodHolder holder) {
        var modId = holder.getModId();
        var materialName = holder.getMaterialName();
        var moddedMaterialName = (holder.getModId() + "_" + materialName).toUpperCase(Locale.ROOT);
        var boatType = EnumHelper.getValueOrDefault(() -> Boat.Type.valueOf(moddedMaterialName), Boat.Type.OAK);

        boat = holder.getRegistry().item(materialName + "_boat", () -> new BoatItem(false, boatType, new Item.Properties().stacksTo(1)));
        chestBoat = holder.getRegistry().item(materialName + "_chest_boat", () -> new BoatItem(true, boatType, new Item.Properties().stacksTo(1)));

        CustomWoodHelper.setItemForBoat(boatType, boat);
        CustomWoodHelper.setItemForChestBoat(boatType, chestBoat);

        // It's too early to set the planks for the boat, so defer them here.
        CustomWoodHelper.setPlanksForBoat(boatType, new ResourceLocation(modId, materialName + "_planks"));

        // Add default boat and chest boat dispenser behavior.
        holder.getRegistry().dispenserBehavior(boat, () -> new BoatDispenseItemBehavior(boatType));
        holder.getRegistry().dispenserBehavior(chestBoat, () -> new BoatDispenseItemBehavior(boatType, true));

        // Add to creative menu.
        holder.addCreativeTabItem(CustomWoodHelper.BOATS, boat);
        holder.addCreativeTabItem(CustomWoodHelper.CHEST_BOATS, chestBoat);
    }
}
