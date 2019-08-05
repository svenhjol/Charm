package svenhjol.charm.enchanting.feature;

import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.meson.Feature;
import svenhjol.charm.enchanting.enchantment.EnchantmentCurseBreak;

public class CurseBreak extends Feature
{
    public static EnchantmentCurseBreak enchantment;
    public static int xpCost; // amount of XP required to break the curse

    @Override
    public String getDescription()
    {
        return "Combine the Curse Break enchanted book with a cursed item on an anvil to remove the curse.";
    }

    @Override
    public void configure()
    {
        super.configure();

        xpCost = propInt(
                "XP cost",
                "Amount of XP (levels) to remove a curse from an item",
                1
        );
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        enchantment = new EnchantmentCurseBreak();
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event)
    {
        enchantment.onAnvilUpdate(event);
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
