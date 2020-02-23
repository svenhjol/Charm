package svenhjol.charm.smithing.module;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.HashMap;
import java.util.Map;

@Module(mod = Charm.MOD_ID, category = CharmCategories.SMITHING, hasSubscriptions = true,
    description = "Combine an item with its golden version on an anvil to reduce the original item's repair cost.")
public class DecreaseRepairCost extends MesonModule
{
    public static Map<Item, Class<? extends Item>> tools = new HashMap<>();
    public static Map<Item, EquipmentSlotType> armor = new HashMap<>();

    @Config(name = "Repair cost decrease", description = "The tool repair cost will be decreased by this number.")
    public static int decreaseAmount = 2;

    @Config(name = "XP cost", description = "Number of levels required to remove a curse from an item.")
    public static int xpCost = 0;

    @Override
    public void init()
    {
        tools.put(Items.GOLDEN_PICKAXE, PickaxeItem.class);
        tools.put(Items.GOLDEN_SHOVEL, ShovelItem.class);
        tools.put(Items.GOLDEN_AXE, AxeItem.class);
        tools.put(Items.GOLDEN_HOE, HoeItem.class);
        tools.put(Items.GOLDEN_SWORD, SwordItem.class);
        armor.put(Items.GOLDEN_HELMET, EquipmentSlotType.HEAD);
        armor.put(Items.GOLDEN_CHESTPLATE, EquipmentSlotType.CHEST);
        armor.put(Items.GOLDEN_LEGGINGS, EquipmentSlotType.LEGS);
        armor.put(Items.GOLDEN_BOOTS, EquipmentSlotType.FEET);
    }

    @Override
    public void onCommonSetup(FMLCommonSetupEvent event)
    {
        if (!Meson.isModuleEnabled("charm:no_anvil_minimum_xp") && xpCost == 0)
            xpCost = 1;
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event)
    {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        ItemStack out;

        if (left.isEmpty() || right.isEmpty()) return;

        Item l = left.getItem();
        Item r = right.getItem();

        // item to combine must be a listed tool or armor item
        if (tools.get(r) == null && armor.get(r) == null) return;

        // check if the input item can have its cost decreased by golden version
        boolean decrease;

        if (l instanceof ArmorItem) {
            decrease = ((ArmorItem)l).getEquipmentSlot() == armor.get(r);
        } else {
            decrease = l.getClass() == tools.get(r);
        }
        if (!decrease) return;

        out = left.copy();

        // get the current repair cost of the item and apply it to the item if there is already a repair cost
        if (left.getRepairCost() > 0) {
            int cost;
            cost = left.getRepairCost();
            out.setRepairCost(Math.max(0, cost - decreaseAmount));
        }

        event.setCost(xpCost);
        event.setOutput(out);
    }
}
