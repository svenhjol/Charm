package svenhjol.charm.enchanting.feature;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.enchanting.enchantment.EnchantmentMagnetic;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.EnchantmentHelper;
import svenhjol.meson.helper.PlayerHelper;

public class Magnetic extends Feature
{
    public static EnchantmentMagnetic enchantment;
    public static int minEnchantability;

    @Override
    public void setupConfig()
    {
        minEnchantability = 10;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        enchantment = new EnchantmentMagnetic();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onHarvest(BlockEvent.HarvestDropsEvent event)
    {
        EntityPlayer player = event.getHarvester();

        if (player != null && player.world != null && !player.world.isRemote) {
            ItemStack held = player.getHeldItemMainhand();
            if (EnchantmentHelper.hasEnchantment(enchantment, held)) {
                PlayerHelper.addOrDropStacks(player, event.getDrops());
                event.setDropChance(0);
            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
