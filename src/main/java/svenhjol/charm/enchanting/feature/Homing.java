package svenhjol.charm.enchanting.feature;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.enchanting.enchantment.EnchantmentHoming;
import svenhjol.meson.Feature;

public class Homing extends Feature
{
    public static EnchantmentHoming enchantment;
    public static int minEnchantability;
    public static int range; // range (in blocks) of checking cube around player
    public static int damage; // as a division of the max damage of the tool

    @Override
    public String getDescription()
    {
        return "A hoe with the Homing enchantment is attracted to ore of the same ingots that make up the head of the hoe.\n" +
                "Right click underground.  If you hear a sound, you can follow it to the ore.";
    }

    @Override
    public void configure()
    {
        super.configure();

        // internal
        minEnchantability = 15;
        damage = 200;
        range = 6;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        enchantment = new EnchantmentHoming();
    }

    @SubscribeEvent
    public void onBlockInteract(PlayerInteractEvent.RightClickBlock event)
    {
        enchantment.onBlockInteract(event);
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
