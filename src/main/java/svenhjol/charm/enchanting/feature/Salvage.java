package svenhjol.charm.enchanting.feature;

import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.meson.Feature;
import svenhjol.charm.enchanting.enchantment.EnchantmentSalvage;

public class Salvage extends Feature
{
    public static EnchantmentSalvage enchantment;
    public static int minEnchantability;

    @Override
    public String getDescription()
    {
        return "An item with the Salvage enchantment does not disappear when its durability is depleted, giving you a chance to get it repaired.\n" +
                "If the item runs out of durability the player will drop it and must be picked up again.  Watch out for lava.";
    }

    @Override
    public void setupConfig()
    {
        // internal
        minEnchantability = 5;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        enchantment = new EnchantmentSalvage();
    }

    @SubscribeEvent
    public void onDestroy(PlayerDestroyItemEvent event)
    {
        enchantment.onDestroy(event);
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
