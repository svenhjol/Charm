package svenhjol.charm.enchanting.feature;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.charm.enchanting.enchantment.EnchantmentMagnetic;
import svenhjol.charm.enchanting.message.MessageMagneticPickup;
import svenhjol.meson.Feature;
import svenhjol.meson.Meson;
import svenhjol.meson.handler.NetworkHandler;
import svenhjol.meson.helper.EnchantmentHelper;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.helper.SoundHelper;
import svenhjol.meson.helper.WorldHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * @see net.minecraftforge.items.ItemHandlerHelper
 */
public class Magnetic extends Feature
{
    public static EnchantmentMagnetic enchantment;
    public static int minEnchantability;
    public static Map<BlockPos, EntityPlayer> dropmap = new HashMap<>();

    @Override
    public String getDescription()
    {
        return "Tools with the Magnetic enchantment automatically pick up drops.";
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
    public void onBreak(BlockEvent.BreakEvent event)
    {
        if (EnchantmentHelper.hasEnchantment(Magnetic.enchantment, event.getPlayer().getHeldItemMainhand())) {
            dropmap.put(event.getPos(), event.getPlayer());
        } else {
            dropmap.values().remove(event.getPlayer());
        }
        Meson.log( dropmap.size() );
    }

    @SubscribeEvent
    public void onEntityCreate(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityItem && !event.getWorld().isRemote) {
            int range = 5;
            BlockPos got = null;

            for (BlockPos pos : dropmap.keySet()) {
                double dist = WorldHelper.getDistanceSq(pos, event.getEntity().getPosition());
                if (dist <= range) {
                    got = pos;
                    break;
                }
            }

            if (got != null) {
                EntityPlayer player = dropmap.get(got);
                EntityItem fake = new EntityItem(player.world, player.posX, player.posY, player.posZ);
                ItemStack item = ((EntityItem) event.getEntity()).getItem();
                fake.setItem(item);
                dropmap.remove(got);

                if (!MinecraftForge.EVENT_BUS.post(new EntityItemPickupEvent(player, fake))) {
                    if (PlayerHelper.addOrDropStack(player, item)) {
                        NetworkHandler.INSTANCE.sendTo(new MessageMagneticPickup(player.getPosition()), (EntityPlayerMP)player);
                    }
                }

                event.setCanceled(true);
            }
        }
    }

//    @SubscribeEvent
//    public void onPlayerTick(PlayerTickEvent event)
//    {
//        if (event.phase == Phase.START
//            && event.side.isServer()
//            && EnchantmentHelper.hasEnchantment(enchantment, event.player.getHeldItemMainhand())
//        ) {
//            double x = event.player.posX;
//            double y = event.player.posY;
//            double z = event.player.posZ;
//
//            List<EntityItem> items = event.player.world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(
//                x - range, y - range, z - range, x + range, y + range, z + range));
//
//            for (EntityItem item : items) {
//                if (item.getItem().isEmpty() || item.isDead) continue;
//                item.setPosition(x, y, z);
//            }
//        }
//    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public static void effectPickup(BlockPos pos)
    {
        WorldClient world = Minecraft.getMinecraft().world;
        SoundHelper.playSoundAtPos(world, pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS,0.85F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
    }
}
