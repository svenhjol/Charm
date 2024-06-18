package svenhjol.charm.feature.core.custom_wood.holders;

import net.minecraft.core.dispenser.BoatDispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.Item;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;
import svenhjol.charm.charmony.helper.EnumHelper;

import java.util.Locale;
import java.util.function.Supplier;

public class BoatHolder {
    public final Supplier<BoatItem> boat;
    public final Supplier<BoatItem> chestBoat;

    public BoatHolder(CustomWoodHolder holder) {
        var modId = holder.ownerId();
        var materialName = holder.getMaterialName();
        var moddedMaterialName = (holder.ownerId() + "_" + materialName).toUpperCase(Locale.ROOT);
        var boatType = EnumHelper.getValueOrDefault(() -> Boat.Type.valueOf(moddedMaterialName), Boat.Type.OAK);

        boat = holder.ownerRegistry().item(materialName + "_boat", () -> new BoatItem(false, boatType, new Item.Properties().stacksTo(1)));
        chestBoat = holder.ownerRegistry().item(materialName + "_chest_boat", () -> new BoatItem(true, boatType, new Item.Properties().stacksTo(1)));

        holder.feature().registers.setItemForBoat(boatType, boat);
        holder.feature().registers.setItemForChestBoat(boatType, chestBoat);

        // It's too early to set the planks for the boat, so defer them here.
        holder.feature().registers.setPlanksForBoat(boatType, new ResourceLocation(modId, materialName + "_planks"));

        // Add default boat and chest boat dispenser behavior.
        holder.ownerRegistry().dispenserBehavior(boat, () -> new BoatDispenseItemBehavior(boatType));
        holder.ownerRegistry().dispenserBehavior(chestBoat, () -> new BoatDispenseItemBehavior(boatType, true));

        // Add to creative menu.
        holder.addItemToCreativeTab(boat, CustomType.BOAT);
        holder.addItemToCreativeTab(chestBoat, CustomType.CHEST_BOAT);
    }
}
