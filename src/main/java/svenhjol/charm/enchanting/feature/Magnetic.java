package svenhjol.charm.enchanting.feature;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.charm.enchanting.enchantment.EnchantmentMagnetic;
import svenhjol.charm.enchanting.message.MessageMagneticPickup;
import svenhjol.meson.Feature;
import svenhjol.meson.handler.ClientHandler;
import svenhjol.meson.handler.NetworkHandler;
import svenhjol.meson.helper.EnchantmentHelper;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.helper.SoundHelper;

import java.util.ArrayList;
import java.util.List;

public class Magnetic extends Feature
{
    public static EnchantmentMagnetic enchantment;
    public static int minEnchantability;
    public static PlayerInteractionManager manager;
    public static List<EntityItem> drops = new ArrayList<>();

    @Override
    public String getDescription()
    {
        return "Tools with the Magnetic enchantment automatically add dropped items to your inventory.";
    }

    @Override
    public void configure()
    {
        super.configure();

        minEnchantability = 15;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        enchantment = new EnchantmentMagnetic();
        NetworkHandler.register(MessageMagneticPickup.class, Side.CLIENT);
    }

    @SubscribeEvent
    public void onEntityCreated(EntityJoinWorldEvent event)
    {
        if (!event.getWorld().isRemote
            && event.getEntity() instanceof EntityItem
            && !event.isCanceled()
            && manager != null
        ) {
            EntityItem item = (EntityItem)event.getEntity();
            drops.add(item);
            event.setCanceled(true);
        }
    }

    public static void startCollectingDrops(PlayerInteractionManager manager)
    {
        if (EnchantmentHelper.hasEnchantment(enchantment, manager.player.getHeldItemMainhand())) {
            Magnetic.manager = manager;
        }
    }

    public static void stopCollectingDrops()
    {
        if (manager != null && !drops.isEmpty()) {
            ArrayList<EntityItem> copy = new ArrayList<>(drops);
            drops.clear();

            EntityPlayerMP player = manager.player;
            for (EntityItem drop : copy) {
                EntityItem fake = new EntityItem(player.world, player.posX, player.posY, player.posZ);
                fake.setItem(drop.getItem());
                if (!MinecraftForge.EVENT_BUS.post(new EntityItemPickupEvent(player, fake))) {
                    boolean result = PlayerHelper.addOrDropStack(player, drop.getItem());
                    if (result) {
                        NetworkHandler.INSTANCE.sendTo(new MessageMagneticPickup(player.getPosition()), player);
                    }
                }
            }

            manager = null;
        }
    }

    @SideOnly(Side.CLIENT)
    public static void effectPickup(BlockPos pos)
    {
        SoundHelper.playSoundAtPos(ClientHandler.getWorldClient(), pos, SoundEvents.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
