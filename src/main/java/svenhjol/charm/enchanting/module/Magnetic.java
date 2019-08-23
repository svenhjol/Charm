package svenhjol.charm.enchanting.module;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.enchanting.enchantment.MagneticEnchantment;
import svenhjol.charm.enchanting.message.MessageMagneticPickup;
import svenhjol.meson.MesonEnchantment;
import svenhjol.meson.MesonModule;
import svenhjol.meson.handler.PacketHandler;
import svenhjol.meson.helper.ClientHelper;
import svenhjol.meson.helper.EnchantmentsHelper;
import svenhjol.meson.helper.SoundHelper;
import svenhjol.meson.helper.WorldHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.HashMap;
import java.util.Map;

@Module(mod = Charm.MOD_ID, category = CharmCategories.ENCHANTING, hasSubscriptions = true)
public class Magnetic extends MesonModule
{
    @Config(name = "Attraction range", description = "Drops within this range of the player will be picked up.")
    public static int range = 6;

    public static Map<BlockPos, PlayerEntity> dropmap = new HashMap<>();
    public static MesonEnchantment enchantment;

    @Override
    public void init()
    {
        enchantment = new MagneticEnchantment(this);
    }

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent event)
    {
        if (EnchantmentsHelper.hasEnchantment(Magnetic.enchantment, event.getPlayer().getHeldItemMainhand())) {
            dropmap.put(event.getPos(), event.getPlayer());
        } else {
            dropmap.values().remove(event.getPlayer());
        }
    }

    @SubscribeEvent
    public void onEntityCreate(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof ItemEntity && !event.getWorld().isRemote) {
            int r = range;
            BlockPos foundPos = null;

            for (BlockPos pos : dropmap.keySet()) {
                double dist = WorldHelper.getDistanceSq(pos, event.getEntity().getPosition());
                if (dist <= r) {
                    foundPos = pos;
                    break;
                }
            }

            if (foundPos != null) {
                PlayerEntity player = dropmap.get(foundPos);
                ItemEntity fake = new ItemEntity(player.world, player.posX, player.posY, player.posZ);
                ItemStack item = ((ItemEntity) event.getEntity()).getItem();
                fake.setItem(item);
                dropmap.remove(foundPos);

                if (!MinecraftForge.EVENT_BUS.post(new EntityItemPickupEvent(player, fake))) {
                    if (player.inventory.addItemStackToInventory(item)) {
                        PacketHandler.sendTo(new MessageMagneticPickup(player.getPosition()), (ServerPlayerEntity)player);
                        event.setCanceled(true);
                    }
                }

            }
        }
    }

    @Override
    public void setup(FMLCommonSetupEvent event)
    {
        PacketHandler.HANDLER.registerMessage(
            PacketHandler.index++,
            MessageMagneticPickup.class,
            MessageMagneticPickup::encode,
            MessageMagneticPickup::decode,
            MessageMagneticPickup.Handler::handle
        );
    }

    @OnlyIn(Dist.CLIENT)
    public static void effectPickup(BlockPos pos)
    {
        ClientWorld world = ClientHelper.getClientWorld();
        SoundHelper.playSoundAtPos(pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS,0.5F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
    }
}
