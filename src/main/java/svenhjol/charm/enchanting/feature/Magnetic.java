package svenhjol.charm.enchanting.feature;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import svenhjol.charm.enchanting.enchantment.EnchantmentMagnetic;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.EnchantmentHelper;
import svenhjol.meson.helper.PlayerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Magnetic extends Feature
{
    public static EnchantmentMagnetic enchantment;
    public static int minEnchantability;
    public static List<UUID> harvesting = new ArrayList<>();
    public static List<EntityItem> drops = new ArrayList<>();

    @Override
    public void setupConfig()
    {
        minEnchantability = 15;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        enchantment = new EnchantmentMagnetic();
    }

    @SubscribeEvent
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

    @SubscribeEvent
    public void onEntityCreated(EntityJoinWorldEvent event)
    {
        if (!event.getWorld().isRemote
            && event.getEntity() instanceof EntityItem
            && !event.isCanceled()
            && !harvesting.isEmpty()
        ) {
            EntityItem item = (EntityItem)event.getEntity();
            drops.add(item);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.START
            && event.player != null
            && event.player.world.getWorldTime() % 10 == 0
            && harvesting.isEmpty()
        ) {
            drops.clear();
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
