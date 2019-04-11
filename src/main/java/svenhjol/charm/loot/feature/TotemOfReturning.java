package svenhjol.charm.loot.feature;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.base.CharmLootTables;
import svenhjol.charm.loot.item.ItemTotemOfReturning;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.ItemHelper;
import svenhjol.meson.helper.LootHelper;

public class TotemOfReturning extends Feature
{
    public static Item totem;
    public static int numberOfUses; // default number of times the totem can be used before it expires
    public static boolean addToLoot; // add to loot tables
    public static boolean addToIllusioner;

    @Override
    public String getDescription()
    {
        return "Right-click with a Totem of Returning to bind it to a location in the world.\n" +
                "The next time you use the totem, you will be returned to that location, destroying the totem in the process.";
    }

    @Override
    public void setupConfig()
    {
        addToLoot = propBoolean(
            "Add to loot",
            "Add the totem to mansions, mineshafts and nether fortress loot.",
            true
        );
        addToIllusioner = propBoolean(
            "Add as Illusioner drop",
            "If Illusioners are enabled, add the totem as a possible drop.",
            true
        );

        // internal
        numberOfUses = 1;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        totem = new ItemTotemOfReturning();
        ItemHelper.availableTotems.add(new ItemStack(totem));
    }

    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event)
    {
        if (!addToLoot) return;
        int weight = 0;
        int quality = 0;
        LootFunction[] functions = new LootFunction[0];
        LootCondition[] conditions = new LootCondition[0];

        if (event.getName().equals(LootTableList.CHESTS_WOODLAND_MANSION)) { weight = 14; }
        if (event.getName().equals(LootTableList.CHESTS_ABANDONED_MINESHAFT)) { weight = 14; }
        if (event.getName().equals(LootTableList.CHESTS_NETHER_BRIDGE)) { weight = 14; }
        if (event.getName().equals(CharmLootTables.VILLAGE_PRIEST)) { weight = 4; }
        if (event.getName().equals(CharmLootTables.TREASURE_VALUABLE)) { weight = 12; }
        if (event.getName().equals(CharmLootTables.TREASURE_RARE)) { weight = 16; }

        if (weight > 0) {
            LootHelper.addToLootTable(event.getTable(), totem, weight, quality, functions, conditions);
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}