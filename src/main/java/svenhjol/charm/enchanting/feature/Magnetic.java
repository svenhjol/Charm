package svenhjol.charm.enchanting.feature;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import svenhjol.charm.enchanting.enchantment.MagneticEnchantment;
import svenhjol.charm.enchanting.message.MessageMagneticPickup;
import svenhjol.meson.Feature;
import svenhjol.meson.MesonEnchantment;
import svenhjol.meson.handler.PacketHandler;
import svenhjol.meson.helper.*;

import java.util.HashMap;
import java.util.Map;

public class Magnetic extends Feature
{
    public static ForgeConfigSpec.ConfigValue<Integer> range;
    public static Map<BlockPos, PlayerEntity> dropmap = new HashMap<>();
    public static MesonEnchantment enchantment;

    @Override
    public void configure()
    {
        super.configure();

        range =  builder
            .comment("Drops within this range of the player will be picked up.")
            .define("Attraction range", 6);
    }

    @Override
    public void init()
    {
        super.init();

        enchantment = new MagneticEnchantment();
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
            int r = range.get();
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
    public boolean hasSubscriptions()
    {
        return true;
    }

    @Override
    public void registerMessages()
    {
        PacketHandler.HANDLER.registerMessage(
            PacketHandler.index++,
            MessageMagneticPickup.class,
            MessageMagneticPickup::encode,
            MessageMagneticPickup::decode,
            MessageMagneticPickup.Handler::handle
        );
    }

    @Override
    public void registerEnchantments(IForgeRegistry<Enchantment> registry)
    {
        registry.register(enchantment);
    }

    @OnlyIn(Dist.CLIENT)
    public static void effectPickup(BlockPos pos)
    {
        ClientWorld world = ClientHelper.getClientWorld();
        SoundHelper.playSoundAtPos(pos, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS,0.5F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
    }
}
