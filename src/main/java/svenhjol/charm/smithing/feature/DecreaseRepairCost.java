package svenhjol.charm.smithing.feature;

import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.meson.Feature;

import java.util.HashMap;
import java.util.Map;

public class DecreaseRepairCost extends Feature
{
    public static Map<Item, Class<? extends Item>> tools = new HashMap<Item, Class<? extends Item>>();
    public static Map<Item, EntityEquipmentSlot> armor = new HashMap<Item, EntityEquipmentSlot>();

    public static int decreaseCost; // how much the golden item will decrease repair cost by
    public static int xpCost; // the base XP cost to activate

    @Override
    public String getDescription()
    {
        return "Combine an item with its golden version on an anvil to reduce the original item's repair cost.";
    }

    @Override
    public void setupConfig()
    {
        decreaseCost = propInt(
                "Decrease repair amount",
                "Amount that a golden item will decrease another item's repair cost.",
                2
        );
        xpCost = propInt(
                "XP cost",
                "Amount of XP (levels) to use a golden item to decrease another item's repair cost.",
                0
        );
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        tools.put(Items.GOLDEN_PICKAXE, ItemPickaxe.class);
        tools.put(Items.GOLDEN_SHOVEL, ItemSpade.class);
        tools.put(Items.GOLDEN_AXE, ItemAxe.class);
        tools.put(Items.GOLDEN_HOE, ItemHoe.class);
        tools.put(Items.GOLDEN_SWORD, ItemSword.class);
        armor.put(Items.GOLDEN_HELMET, EntityEquipmentSlot.HEAD);
        armor.put(Items.GOLDEN_CHESTPLATE, EntityEquipmentSlot.CHEST);
        armor.put(Items.GOLDEN_LEGGINGS, EntityEquipmentSlot.LEGS);
        armor.put(Items.GOLDEN_BOOTS, EntityEquipmentSlot.FEET);
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event)
    {
        ItemStack in = event.getLeft();
        ItemStack combine = event.getRight();

        if (in.isEmpty() || combine.isEmpty()) return;

        Item i = in.getItem();
        Item c = combine.getItem();

        // item to combine must be a listed tool or armor item
        if (tools.get(c) == null && armor.get(c) == null) return;

        // check if the input item can have its cost decreased by golden version
        boolean canDecrease;
        if (i instanceof ItemArmor) {
            canDecrease = ((ItemArmor)i).armorType == armor.get(c);
        } else {
            canDecrease = i.getClass() == tools.get(c);
        }
        if (!canDecrease) return;

        ItemStack out = in.copy();

        // get the current repair cost of the item and apply it to the item if there is already a repair cost
        if (in.getTagCompound() != null && !in.getTagCompound().isEmpty()) {
            int cost;
            cost = in.getTagCompound().getInteger("RepairCost");
            out.setRepairCost(Math.max(0, cost - decreaseCost));
        }

        event.setCost(xpCost);
        event.setOutput(out);
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}