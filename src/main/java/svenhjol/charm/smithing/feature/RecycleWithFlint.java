package svenhjol.charm.smithing.feature;

import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.meson.Feature;
import svenhjol.meson.Meson;

import java.util.HashMap;
import java.util.Map;

public class RecycleWithFlint extends Feature
{
    public static Map<Class<? extends Item>, Integer> tools = new HashMap<>();
    public static Map<EntityEquipmentSlot, Integer> armor = new HashMap<>();
    public static Map<String, String> materials = new HashMap<>();
    public static Map<String, String> horseArmor = new HashMap<>();

    public static int xpCost; // how much it costs in XP to recycle
    public static int horseArmorUnits; // how many ingots/items horse armour is worth

    @Override
    public String getDescription()
    {
        return "Combine a tool, weapon or armour with flint to break the item into its component materials.\n" +
                "For a tool or weapon the material will be whatever the head is made from.\n" +
                "To get the full amount of material back, the item must be completely repaired and the equivalent number of pieces of flint used.";
    }

    @Override
    public void setupConfig()
    {
        horseArmorUnits = propInt(
                "Horse armor material units",
                "How many units of material horse armour is worth.  Example: if set to 8, gold horse armor will return 8 gold ingots.",
                8
        );
        xpCost = propInt(
                "XP cost",
                "Amount of XP (levels) needed to break apart the item.",
                0
        );
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        tools.put(ItemPickaxe.class, 3);
        tools.put(ItemSpade.class, 1);
        tools.put(ItemAxe.class, 3);
        tools.put(ItemHoe.class, 2);
        tools.put(ItemSword.class, 2);
        tools.put(ItemShears.class, 2);

        armor.put(EntityEquipmentSlot.HEAD, 5);
        armor.put(EntityEquipmentSlot.CHEST, 8);
        armor.put(EntityEquipmentSlot.LEGS, 7);
        armor.put(EntityEquipmentSlot.FEET, 4);

        horseArmor.put("minecraft:diamond_horse_armor", "diamond");
        horseArmor.put("minecraft:golden_horse_armor", "gold");
        horseArmor.put("minecraft:iron_horse_armor", "iron");

        materials.put("minecraft:diamond", "minecraft:diamond");
        materials.put("minecraft:gold", "minecraft:gold_ingot");
        materials.put("minecraft:iron", "minecraft:iron_ingot");
        materials.put("minecraft:stone", "minecraft:cobblestone");
        materials.put("minecraft:wood", "minecraft:planks");
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event)
    {
        ItemStack in = event.getLeft();
        ItemStack combine = event.getRight();

        if (in.isEmpty() || combine.isEmpty()) return;

        Item i = in.getItem();
        Item c = combine.getItem();

        if (c != Items.FLINT) return;

        String material;
        float durability = 1.0f;

        if (in.getItemDamage() > 0 && in.getMaxDamage() > 0) {
            durability -= ((float)in.getItemDamage() / (float)in.getMaxDamage());
        }
        int units = 0;

        if (i instanceof ItemArmor) {

            // handle player armor
            ItemArmor a = (ItemArmor) i;
            ItemStack repairMaterial = a.getArmorMaterial().getRepairItemStack();
            material = repairMaterial.getItem().getRegistryName().toString();
            units = armor.get(a.armorType);
            Meson.debug(material, units);
        } else if (i.getRegistryName() != null && horseArmor.get(i.getRegistryName().toString()) != null) {

            // handle horse armour
            material = horseArmor.get(i.getRegistryName().toString());
            units = horseArmorUnits;
        } else {

            // handle player tools
            if (i instanceof ItemTool) {
                material = ((ItemTool) i).getToolMaterialName();
            } else if (i instanceof ItemSword) {
                material = ((ItemSword) i).getToolMaterialName();
            } else if (i instanceof ItemHoe) {
                material = ((ItemHoe) i).getMaterialName();
            } else if (i instanceof ItemShears) {
                material = "iron";
            } else {
                return;
            }

            if (tools.get(i.getClass()) != null) {
                units = tools.get(i.getClass());
            }
        }

        if (material != null) {
            if (!material.contains("minecraft:")) {
                material = "minecraft:" + material;
            }
            material = materials.get(material.toLowerCase());

            if (material != null) {
                Item itemMaterial;
                itemMaterial = Item.getByNameOrId(material);

                if (itemMaterial != null) {
                    int unitsReturned = (int) Math.floor(units * durability);
                    int materialCost = Math.min(unitsReturned, combine.getCount());
                    ItemStack out = new ItemStack(itemMaterial, materialCost);
                    event.setCost(xpCost);
                    event.setMaterialCost(materialCost);
                    event.setOutput(out);
                }
            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
